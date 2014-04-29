package vn.myfeed.parser.processor

import vn.myfeed.parser.model.{LinkElement, ArticleElement, MediaElement, Article}
import vn.myfeed.parser.util.VideoSitePattern

/**
 * Thi class will mark an image what has the previous or next potential element is become potential.
 *
 * @author Nguyen Duc Dung
 * @since 12/24/12 9:07 AM
 *
 */
class MediaBaseFilter(minTitleLength: Int = 2) extends Processor {
  def process(implicit article: Article) {
    var previous: Option[ArticleElement] = None
    var next: Option[ArticleElement] = None
    for (i <- 0 until article.elements.size) {
      if (i - 1 >= 0) previous = Some(article.elements(i - 1))
      if (i + 1 <= article.elements.size - 1) next = Some(article.elements(i + 1))

      val element = article.elements(i)

      if(element.isInstanceOf[MediaElement]) {
        previous.map(prevElement => {
          next.map(nextElement => {
            if(prevElement.isInstanceOf[MediaElement]
              && nextElement.isInstanceOf[MediaElement]) {
              prevElement.isPotential = true
              element.isPotential = true
              nextElement.isPotential = true
            }
          })
        })
      }

      if (!element.isPotential && element.isInstanceOf[MediaElement]) {
        previous.map(prevElement => if (prevElement.isPotential) element.isPotential = true)
        next.map(nextElement => if (nextElement.isPotential) element.isPotential = true)
      }

      if (element.isPotential && element.isInstanceOf[MediaElement]) {
        //The image title should mark as a potential element.
        next.map(nextElement => if (element.isPotential && !nextElement.isInstanceOf[LinkElement]
          && nextElement.text.length >= minTitleLength) {
          nextElement.isPotential = true
        })
      }
    }

    //Marking media element form known video site is potential
    article.mediaElements.foreach(el => {
      val tagName = el.tagName
      val src = el.src
      if(tagName == "iframe" && VideoSitePattern.matches(src)) {
        el.isPotential = true
      }
    })
  }
}
