package cz.sic.data.source.db

import cz.sic.data.source.BaseSource
import cz.sic.data.source.db.model.toDomain
import cz.sic.data.source.db.model.toEntity
import cz.sic.domain.model.Score
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class RoomSource(
    val storage: ScoreDao
): BaseSource<Score> {

    override suspend fun getData(): List<Score> =
        storage.getAllScores()
            .map { it.toDomain() }

    suspend fun saveScore(score: Score) {
        storage.insert(score.toEntity())
    }

    suspend fun deleteScore(id: Int) {
        storage.deleteById(id)
    }

    fun observeScores(): Flow<List<Score>> =
        storage.observeAll()
            .map { list -> list.map { it.toDomain() } }

    suspend fun deleteAllScores() {
        storage.deleteAll()
    }
}