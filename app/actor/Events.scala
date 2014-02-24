package actor

import model.Blog

/**
 * The Class Events.
 *
 * @author Nguyen Duc Dung
 * @since 10/7/13 5:29 PM
 *
 */
case object Start

case object Clean

case class Crawl(blog: Blog)

case class Fetch(url: String)

case class Image(url: String)

case class FetchResult(image: Option[Image])

