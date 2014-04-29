package http

import scala.util.Random

/**
 * The Class FirefoxUserAgent.
 *
 * @author Nguyen Duc Dung
 * @since 5/7/13 11:20 PM
 *
 */
case class UserAgent(value: String)

object UserAgents {

  val list = List(FireFoxUserAgent, ChromiumUserAgent, Ie7UserAgent, OperaUserAgent)

  def random = {
    val index = Random.nextInt(list.size)
    list(index).value
  }
}

object FireFoxUserAgent extends UserAgent("Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")

object ChromiumUserAgent extends UserAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Ubuntu Chromium/25.0.1364.160 Chrome/25.0.1364.160 Safari/537.22")

object Ie7UserAgent extends UserAgent("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)")

object OperaUserAgent extends UserAgent("Opera/9.20 (Windows NT 6.0; U; en)")

object HayHayBlogUserAgent extends UserAgent("HayHayBlog/1.0 (http://hayhayblog.com)")

