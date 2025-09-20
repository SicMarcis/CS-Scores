package cz.sic.data.source.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.snapshots
import cz.sic.data.source.BaseSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.ktx.toObjects
import cz.sic.data.source.firebase.model.ScoreEntry
import cz.sic.data.source.firebase.model.toDomain
import cz.sic.data.source.firebase.model.toEntry
import cz.sic.domain.model.Score

class FirebaseSource(
    private val storage: FirebaseFirestore
): BaseSource<Score> {

    private val COLLECTION = "scores"

    override suspend fun getData(): List<Score> {
        return storage.collection(COLLECTION)
            .get()
            .await()
            .toObjects<ScoreEntry>()
            .map { it.toDomain() }

    }

    fun observeScores(): Flow<List<Score>> =
        storage.collection(COLLECTION)
            .snapshots(MetadataChanges.INCLUDE)
            .map { snapshot ->
                snapshot.toObjects<ScoreEntry>()
            }
            .map {
                it.map { it.toDomain() }
            }

    suspend fun saveScore(score: Score) {
        storage.collection(COLLECTION)
            .document(score.id.toString())
            .set(score.toEntry())
            .await()
    }

    suspend fun getScore(id: Long): Score? {
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
}