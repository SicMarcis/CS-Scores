package cz.sic.list.domain.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Score(
    val id: Long = System.currentTimeMillis(),// = Uuid.random().toString(),
    val name: String,
    val address: String,
    val duration: Int,
)

enum class Store {
    Local,
    Remote,
    Any
}
