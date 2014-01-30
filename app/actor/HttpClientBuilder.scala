package actor

import org.apache.http.params.{CoreConnectionPNames, CoreProtocolPNames, BasicHttpParams}
import org.apache.http.client.params.{CookiePolicy, ClientPNames}
import org.apache.http.conn.scheme.{PlainSocketFactory, Scheme, SchemeRegistry}
import javax.net.ssl.{X509TrustManager, SSLContext}
import java.security.cert.X509Certificate
import java.security.SecureRandom
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.impl.conn.PoolingClientConnectionManager
import org.apache.http.impl.client._

/**
 * The Class HttpClientBuilder.
 *
 * @author Nguyen Duc Dung
 * @since 7/6/13 9:10 PM
 *
 */
object HttpClientBuilder {

  def build(pooling: Boolean = true, timeout: Int = 1000 * 60) = {
    val httpParams = new BasicHttpParams()
    httpParams.setParameter(CoreProtocolPNames.USER_AGENT, UserAgents.random)
    //Set time out 30s
    httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout)
    httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout)
    httpParams.setParameter(CoreConnectionPNames.TCP_NODELAY, true)
    httpParams.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
    httpParams.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8")
    httpParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY)
    httpParams.setParameter("Cache-Control", "max-age=0")

    val client = if (pooling) {
      val schemeRegistry = new SchemeRegistry
      schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory))
      //SSL connection
      val sslCtx = SSLContext.getInstance("TLS")
      val tm = new X509TrustManager() {
        def checkClientTrusted(p1: Array[X509Certificate], p2: String) {}
        def checkServerTrusted(p1: Array[X509Certificate], p2: String) {}
        def getAcceptedIssuers = Array[X509Certificate]()
      }
      sslCtx.init(null, Array(tm), new SecureRandom())
      val sslSocketFactory = new SSLSocketFactory(sslCtx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
      schemeRegistry.register(new Scheme("https", 443, sslSocketFactory))
      val cm = new PoolingClientConnectionManager(schemeRegistry)
      cm.setMaxTotal(20000)
      cm.setDefaultMaxPerRoute(500)

      new DefaultHttpClient(cm, httpParams)
    } else {
      new DefaultHttpClient(httpParams)
    }

    client.asInstanceOf[AbstractHttpClient].setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
    client.getParams.setParameter("http.conn-manager.timeout", 120000L)
    client.getParams.setParameter("http.protocol.wait-for-continue", 10000L)
    client.addResponseInterceptor(new ResponseInterceptor)
    new DecompressingHttpClient(new AutoRetryHttpClient(client))
  }

}
