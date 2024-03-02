package app.opass.ccip.test.source.ccip

import app.opass.ccip.misc.I18nText
import app.opass.ccip.model.Announcement
import app.opass.ccip.model.Attendee
import app.opass.ccip.model.Scenario
import app.opass.ccip.source.ccip.AnnouncementDto
import app.opass.ccip.source.ccip.AttendeeDto
import app.opass.ccip.source.ccip.ScenarioDto
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.net.URL
import java.util.Locale
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant

class CcipDtoTest :
    StringSpec({
      val i18nTextDto =
          mapOf(
              "en" to "English",
              "zh" to "中文",
          )
      val i18nText =
          I18nText(
              Locale.ENGLISH to "English",
              Locale.CHINESE to "中文",
          )
      val urlDto = "https://example.com/"
      val url = URL(urlDto)
      val instantDto = 0L
      val instant = LocalDateTime(1970, 1, 1, 0, 0, 0).toInstant(TimeZone.UTC)

      "Unpack AnnouncementDto" {
        val dto =
            AnnouncementDto(
                instantDto,
                "English",
                "中文",
                urlDto,
            )
        val expected =
            Announcement(
                instant,
                i18nText,
                url,
            )
        dto.unpack().shouldBe(expected)
      }

      val scenarioDto =
          ScenarioDto(
              "checkin",
              1,
              i18nTextDto,
              instantDto,
              instantDto + 2,
              3,
          )
      val expectedScenario =
          Scenario(
              "checkin",
              1,
              i18nText,
              instant,
              instant.plus(2, DateTimeUnit.SECOND),
              DateTimePeriod(seconds = 3),
          )

      "Unpack ScenarioDto" { scenarioDto.unpack().shouldBe(expectedScenario) }

      "Unpack AttendeeDto" {
        val token = "7bab41e5-7755-459a-b65d-17448c5f40d3"
        val dto =
            AttendeeDto(
                "Test 2087",
                "User 0",
                instantDto,
                "attendee",
                listOf(
                    scenarioDto,
                ),
                token,
            )
        val expected =
            Attendee(
                "Test 2087",
                token,
                null,
                "User 0",
                instant,
                "attendee",
                listOf(
                    expectedScenario,
                ),
            )
        dto.unpack(token).shouldBe(expected)
      }
    })
