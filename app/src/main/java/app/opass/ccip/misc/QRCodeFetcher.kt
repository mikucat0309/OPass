package app.opass.ccip.misc

import coil.ImageLoader
import coil.decode.DataSource
import coil.decode.ImageSource
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.request.Options
import okio.Buffer
import org.koin.core.component.KoinComponent
import qrcode.QRCode

class QRCodeFetcher(private val data: QString, private val options: Options) :
    Fetcher, KoinComponent {

  override suspend fun fetch(): FetchResult {
    val buffer = Buffer()
    QRCode(data.value).render().writeImage(buffer.outputStream())
    return SourceResult(
        ImageSource(buffer, options.context),
        "image/png",
        DataSource.MEMORY,
    )
  }

  object Factory : Fetcher.Factory<QString> {
    override fun create(data: QString, options: Options, imageLoader: ImageLoader): Fetcher {
      return QRCodeFetcher(data, options)
    }
  }
}

class QString(val value: String)
