package app.opass.ccip.schedule

import kotlinx.datetime.LocalDateTime


typealias SessionId = String
typealias SpeakerId = String
typealias RoomId = String
typealias SessionTypeId = String
typealias SessionTagId = String

data class Schedule(
    val sessions: List<Session>,
    val speakers: List<Speaker>,
    val sessionTypes: List<SessionType>,
    val rooms: List<Room>,
    val tags: List<SessionTag>,
)

data class Session(
    val id: SessionId,
    val type: SessionType,
    val room: Room,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val title: String,
    val speakers: List<Speaker>,
    val description: String = "",
    val language: String? = null,
    val tags: List<SessionTag> = emptyList(),
    val coWrite: String? = null,
    val qa: String? = null,
    val slide: String? = null,
    val record: String? = null,
    val uri: String? = null,
)

data class Speaker(
    val id: SpeakerId,
    val name: String,
    val bio: String,
)

data class SessionType(
    val id: SessionTypeId,
    val name: String,
)

data class Room(
    val id: RoomId,
    val name: String,
)

data class SessionTag(
    val id: SessionTagId,
    val name: String,
)

