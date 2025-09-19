package cz.sic.list.data.source.firebase.model

import cz.sic.list.domain.model.Score

data class ScoreEntry(
    val id: Long = 0,
    val name: String = "",
    val address: String = "",
    val duration: Int = 0,
)

fun ScoreEntry.toDomain(): Score =
    Score(
        id = id,
        name = name,
        address = address,
        duration = duration,
    )

fun Score.toEntry(): ScoreEntry =
    ScoreEntry(
        id = id,
        name = name,
        address = address,
        duration = duration,
    )