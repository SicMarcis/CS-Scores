package cz.sic.data.source.db

import cz.sic.data.source.LocalSource
import cz.sic.data.source.db.model.toDomain
import cz.sic.data.source.db.model.toEntity
import cz.sic.domain.model.Score
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomSource(
    val storage: ScoreDao
): LocalSource<Score> {

    override suspend fun getData(): List<Score> =
        storage.getAllScores()
            .map { it.toDomain() }

    override suspend fun save(score: Score) {
        storage.insert(score.toEntity())
    }

    override suspend fun delete(id: Long) {
        storage.deleteById(id)
    }

    override fun observe(): Flow<List<Score>> =
        storage.observeAll()
            .map { list -> list.map { it.toDomain() } }

    override suspend fun deleteAll() {
        storage.deleteAll()
    }

    override suspend fun getItem(id: Long): Score? {
        return storage.getById(id)?.toDomain()
    }
}