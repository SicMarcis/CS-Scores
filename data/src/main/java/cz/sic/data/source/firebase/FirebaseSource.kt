package cz.sic.data.source.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import cz.sic.data.source.RemoteSource
import cz.sic.data.source.firebase.model.ScoreEntry
import cz.sic.data.source.firebase.model.toDomain
import cz.sic.data.source.firebase.model.toEntry
import cz.sic.domain.model.Score
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseSource(
    private val storage: FirebaseFirestore
): RemoteSource<Score> {

    private val COLLECTION = "scores"

    override suspend fun getData(): List<Score> {
        return storage.collection(COLLECTION)
            .get()
            .await()
            .toObjects<ScoreEntry>()
            .map { it.toDomain() }

    }

    override fun observe(): Flow<List<Score>> =
        callbackFlow {
            val collection = storage.collection("scores")

            val listenerRegistration = collection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val scores = snapshot.toObjects< ScoreEntry>()
                    trySend(scores.map { it.toDomain() }) // Offer the latest data to the flow
                }
            }

            awaitClose {
                listenerRegistration.remove()
            }
        }

    override suspend fun deleteAll() {
        val collectionRef = storage.collection(COLLECTION)
        val querySnapshot = collectionRef.get().await()

        // Firestore allows a maximum of 500 operations in a single batch.
        // We chunk the documents to handle collections of any size.
        querySnapshot.documents.chunked(500).forEach { chunk ->
            val batch = storage.batch()
            for (document in chunk) {
                batch.delete(document.reference)
            }
            batch.commit().await()
        }
    }

    override suspend fun save(score: Score) {
        storage.collection(COLLECTION)
            .document(score.id.toString())
            .set(score.toEntry())
            .await()
    }

    override suspend fun getItem(id: Long): Score? {
        val document = storage.collection(COLLECTION)
            .document(id.toString())
            .get()
            .await()
        return if (document.exists()) {
            document.toObject(ScoreEntry::class.java)?.toDomain()
        } else {
            null
        }
    }

    override suspend fun delete(id: Long) {
        storage.collection(COLLECTION)
            .document(id.toString())
            .delete()
            .await()
    }
}