package cz.sic.domain.model

import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
data class Score(
    val id: Long = System.currentTimeMillis(),// = Uuid.random().toString(),
    val name: String,
    val address: String,
    val duration: Int,
)


