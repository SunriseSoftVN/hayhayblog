package vn.myfeed.parser.processor

import vn.myfeed.parser.model.{TextElement, Article}
import org.apache.commons.lang3.StringUtils

/**
 * The Class TagBaseFilter.
 *
 * @author Nguyen Duc Dung
 * @since 12/26/12 12:24 PM
 *
 */
class TagBaseFilter extends Processor {
  def process(implicit article: Article) {
    article.potentialElements.foreach(potentialElement => {
      val tagName = potentialElement.tagName
      val elementClass = potentialElement.className
      //The potential element should be near element has same tag
      article.elements
        .filter(el => el != potentialElement && !el.isPotential && Math.abs(potentialElement.index - el.index) == 1)
        .foreach(el => {
        if (el.tagName == tagName
          && el.className == elementClass
          && (StringUtils.isNotBlank(elementClass) || el.parent == potentialElement.parent)) {
          el match {
            case textEl : TextElement if textEl.wordCount >= 2 => el.isPotential = true
            case _ =>
          }
        }

        if (el.tagName == tagName
          && el.parent == potentialElement.parent
          && el.isSimilarClassName(potentialElement)) {
          el.isPotential = true
        }

      })
    })
  }
}
