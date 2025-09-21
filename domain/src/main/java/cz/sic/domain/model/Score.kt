package cz.sic.domain.model

data class Score(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val address: String,
    val duration: Int,
)


