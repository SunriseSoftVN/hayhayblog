package vn.myfeed.parser.model

import org.jsoup.nodes.Element
import org.apache.commons.lang3.StringUtils

/**
 * The Class LinkElement.
 *
 * @author Nguyen Duc Dung
 * @since 12/23/12 1:06 AM
 *
 */
class LinkElement(_jsoupElement: Element)(implicit article: Article) extends ArticleElement(_jsoupElement) {

  def text = jsoupEl.text()

  /**
   * Link element should be minus score
   * @return
   */
  override def score = -200

  override def toString = jsoupEl.attr("href")
}

object LinkElementMatcher {
  def unapply(jsoupElement: Element): Option[Element] = if(jsoupElement.tagName.toLowerCase == "a") {
    val imgElements = jsoupElement.select("img")
    if(imgElements.size == 1) {
      //It look like a gallery
      val href = jsoupElement.attr("href")
      val imgElement = imgElements.get(0)
      val src = imgElement.attr("src")
      if(StringUtils.isNotBlank(href) && href == src) {
        // Skip this element because this is an gallery.
        return None
      }
    }
    Some(jsoupElement)
  } else {
    None
  }
}