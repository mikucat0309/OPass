package app.opass.ccip.source.ccip

import app.opass.ccip.I18nText
import app.opass.ccip.model.Announcement
import app.opass.ccip.source.mockJsonHttpClient
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.net.URL
import java.util.Locale
import kotlinx.datetime.Instant

class RemoteCcipClientTest :
    StringSpec({
      coroutineTestScope = true

      val httpClient =
          mockJsonHttpClient(
              "/announcement" to
                  """
                [
                    {
                        "_id": {
                            "${'$'}oid": "631476d6bb5c7523fc5acefc"
                        },
                        "datetime": 1662285526,
                        "msg_zh": "公告",
                        "msg_en": "announcement",
                        "uri": "https://httpbin.org/get",
                        "role": [
                            "audience",
                            "staff",
                            "speaker"
                        ]
                    }
                ]
                """
                      .trimIndent(),
          )
      val remoteCcipClient = RemoteCcipClient(httpClient)
      remoteCcipClient.baseUrl = "https://example.com"

      "Get announcements" {
        val expected =
            listOf(
                Announcement(
                    Instant.fromEpochSeconds(1662285526L),
                    I18nText(
                        Locale.ENGLISH to "announcement",
                        Locale.CHINESE to "公告",
                    ),
                    URL("https://httpbin.org/get"),
                ),
            )
        val announcements = remoteCcipClient.getAnnouncements(null).getOrThrow()
        announcements.shouldBe(expected)
      }
    })
