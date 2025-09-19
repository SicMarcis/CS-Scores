package cz.sic.list.domain.model

data class ScoreWithStore(
    val score: Score,
    val store: Store
)

fun Score.toScoreWithStore(store: Store) =
    ScoreWithStore(
        score = this,
        store = store
    )