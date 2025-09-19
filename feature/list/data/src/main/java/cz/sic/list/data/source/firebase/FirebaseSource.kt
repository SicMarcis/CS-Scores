package cz.sic.list.data.source.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import cz.sic.list.data.source.BaseSource
import cz.sic.list.data.source.db.model.toEntity
import cz.sic.list.domain.model.Score
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.ktx.toObjects
import cz.sic.list.data.source.firebase.model.ScoreEntry
import cz.sic.list.data.source.firebase.model.toDomain
import cz.sic.list.data.source.firebase.model.toEntry

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
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects<ScoreEntry>()
            }
            .map { it.map { it.toDomain() } }

    suspend fun saveScore(score: Score) {
        storage.collection(COLLECTION)
            .document(score.id.toString())
            .set(score.toEntry())
            .await()
    }
}