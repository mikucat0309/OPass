@file:UseSerializers(UrlSerializer::class)
@file:Suppress("PropertyName")

package app.opass.ccip.schedule

import app.opass.ccip.i18n.LocalizedString
import app.opass.ccip.misc.UrlSerializer
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.net.URL

@Serializable
data class ScheduleDto(
    val sessions: List<SessionDto>,
    val speakers: List<SpeakerDto>,
    val session_types: List<SessionTypeDto>,
    val rooms: List<RoomDto>,
    val tags: List<SessionTagDto>
) {
    fun toSchedule(): Schedule {
        val speakerList = speakers.map { it.toSpeaker() }
        val typeList = session_types.map { it.toSessionType() }
        val roomList = rooms.map { it.toRoom() }
        val tagList = tags.map { it.toSessionTag() }
        val speakerMap = speakerList.associateBy { it.id }
        val typeMap = typeList.associateBy { it.id }
        val roomMap = roomList.associateBy { it.id }
        val tagMap = tagList.associateBy { it.id }
        val sessionMap = sessions.map { sessionDto ->
            sessionDto.run {
                Session(
                    id = id,
                    type = typeMap[type]!!,
                    room = roomMap[room]!!,
                    start = start.toLocalDateTime(TimeZone.of("UTC+8")),
                    end = end.toLocalDateTime(TimeZone.of("UTC+8")),
                    title = LocalizedString(
                        mapOf(
                            "zh" to zh.title,
                            "en" to en.title
                        )
                    ),
                    speakers = speakers.map { speakerMap[it]!! },
                    description = LocalizedString(
                        mapOf(
                            "zh" to zh.description,
                            "en" to en.description
                        )
                    ),
                    language = language,
                    tags = tags.map { tagMap[it]!! },
                    coWrite = co_write,
                    qa = qa,
                    slide = slide,
                    record = record,
                    uri = uri
                )
            }
        }
        return Schedule(
            sessionMap,
            speakerList,
            typeList,
            roomList,
            tagList
        )
    }
}

@Serializable
data class SessionDto(
    val id: SessionId,
    val type: SessionTypeId,
    val room: RoomId,
    val start: Instant,
    val end: Instant,
    val zh: I18n,
    val en: I18n,
    val speakers: List<SpeakerId>,
    val tags: List<SessionTagId>,
    val language: String? = null,
    val co_write: String? = null,
    val qa: String? = null,
    val slide: String? = null,
    val record: String? = null,
    val uri: String? = null
) {
    @Serializable
    data class I18n(
        val title: String,
        val description: String
    )
}

@Serializable
data class SpeakerDto(
    val id: SpeakerId,
    val avatar: URL,
    val zh: I18n,
    val en: I18n
) {
    @Serializable
    data class I18n(
        val name: String,
        val bio: String
    )

    fun toSpeaker(): Speaker {
        return Speaker(
            id = id,
            name = LocalizedString(
                mapOf(
                    "zh" to zh.name,
                    "en" to en.name
                )
            ),
            bio = LocalizedString(
                mapOf(
                    "zh" to zh.bio,
                    "en" to en.bio
                )
            )
        )
    }
}

@Serializable
data class RoomDto(
    val id: RoomId,
    val zh: I18n,
    val en: I18n
) {
    @Serializable
    data class I18n(
        val name: String
    )

    fun toRoom(): Room {
        return Room(
            id = id,
            name = LocalizedString(
                mapOf(
                    "zh" to zh.name,
                    "en" to en.name
                )
            )
        )
    }
}

@Serializable
data class SessionTagDto(
    val id: SessionTagId,
    val zh: I18n,
    val en: I18n
) {
    @Serializable
    data class I18n(
        val name: String
    )

    fun toSessionTag(): SessionTag {
        return SessionTag(
            id = id,
            name = LocalizedString(
                mapOf(
                    "zh" to zh.name,
                    "en" to en.name
                )
            )
        )
    }
}

@Serializable
data class SessionTypeDto(
    val id: SessionTypeId,
    val zh: I18n,
    val en: I18n
) {
    @Serializable
    data class I18n(
        val name: String
    )

    fun toSessionType(): SessionType {
        return SessionType(
            id = id,
            name = LocalizedString(
                mapOf(
                    "zh" to zh.name,
                    "en" to en.name
                )
            )
        )
    }
}
