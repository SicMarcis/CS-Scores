package cz.sic.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Store {
    @SerialName("local")
    Local,
    @SerialName("remote")
    Remote,
    @SerialName("any")
    Any
}