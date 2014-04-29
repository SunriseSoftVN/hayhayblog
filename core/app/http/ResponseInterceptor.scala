package http

import org.apache.http.{HttpResponse, HttpResponseInterceptor}
import org.apache.http.protocol.HttpContext
import org.apache.http.impl.client.{RedirectLocations, DefaultRedirectStrategy}
import collection.JavaConversions._
import org.apache.commons.lang3.StringUtils

/**
 * The Class ResponseInterceptor.
 *
 * @author Nguyen Duc Dung
 * @since 5/9/13 6:44 AM
 *
 */
class ResponseInterceptor extends HttpResponseInterceptor {
  def process(response: HttpResponse, context: HttpContext) {
    val entity = response.getEntity
    val redirectLocation = context.getAttribute(DefaultRedirectStrategy.REDIRECT_LOCATIONS)
    if (redirectLocation != null) {
      val uris = redirectLocation.asInstanceOf[RedirectLocations].getAll
      if (!uris.isEmpty) {
        val url = uris.last.toString
        if (StringUtils.isNotBlank(url)) {
          response.setHeader("moveToUrl", url)
        }
      }
    }
  }
}
