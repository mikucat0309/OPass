package app.opass.ccip

import android.annotation.SuppressLint
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

@SuppressLint("CustomX509TrustManager")
object TrustAllManager : X509TrustManager {

  @Suppress("EmptyFunctionBlock")
  @SuppressLint("TrustAllX509TrustManager")
  override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

  @Suppress("EmptyFunctionBlock")
  @SuppressLint("TrustAllX509TrustManager")
  override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

  override fun getAcceptedIssuers() = emptyArray<X509Certificate>()
}
