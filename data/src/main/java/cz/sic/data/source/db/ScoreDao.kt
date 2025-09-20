package cz.sic.data.source.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.sic.data.source.db.model.ScoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {

    @Query("SELECT * FROM score")
    suspend fun getAllScores(): List<ScoreEntity>

    @Query("SELECT * FROM score")
    fun observeAll(): Flow<List<ScoreEntity>>

    @Query("SELECT * FROM score WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): ScoreEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(score: ScoreEntity)

    @Query("DELETE FROM score WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM score")
    suspend fun deleteAll()
}