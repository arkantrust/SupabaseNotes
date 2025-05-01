package app.ddulce.supabasenotes

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Int,
    val body: String,
)
