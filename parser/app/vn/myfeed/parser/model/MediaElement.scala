package vn.myfeed.parser.model

import org.jsoup.nodes.Element
import vn.myfeed.parser.util.VideoSitePattern

/**
 * The Class ImageBlock.
 *
 * @author Nguyen Duc Dung
 * @since 12/23/12 1:04 AM
 *
 */
class MediaElement(_jsoupElement: Element)(implicit article: Article) extends ArticleElement(_jsoupElement) {

  def text = ""

  def src = jsoupEl.attr("src")

  /**
   * @return 100 when the element is potential.
   */
  override def score = if (isPotential) if (tagName == "iframe") 1000 else 100 else super.score

  override def toString = src
}

object MediaElementMatcher {
  def unapply(jsoupElement: Element) = jsoupElement.tagName.toLowerCase match {
    case "img" | "object" | "embed" => Some(jsoupElement)
    case "iframe" => {
      val src = jsoupElement.attr("src")
      if (VideoSitePattern.matches(src)) Some(jsoupElement) else None
    }
    case _ => None
  }
}