package cz.sic.data.source.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.sic.domain.model.Score

@Entity("score")
data class ScoreEntity(
    @PrimaryKey val id: Long = System.currentTimeMillis(),
    val name: String,
    val address: String,
    val duration: Int,
)

fun ScoreEntity.toDomain(): Score =
    Score(
        id = id,
        name = name,
        address = address,
        duration = duration,
    )

fun Score.toEntity(): ScoreEntity =
    ScoreEntity(
        id = id,
        name = name,
        address = address,
        duration = duration,
    )
