package vn.myfeed.parser.processor

import vn.myfeed.parser.model.{LinkElement, ArticleElement, Article}

/**
 * This class base on distance of two potential element to find another potential element.
 *
 * @param maxDistance distance between two elements
 *
 * @author Nguyen Duc Dung
 * @since 12/24/12 7:26 AM
 *
 */
class DistanceBaseFilter(maxDistance: Int = 2) extends Processor {
  def process(implicit article: Article) {
    var lastPotentialElement: Option[ArticleElement] = None
    article.elements.foreach(element => {
      if (element.isPotential) {
        lastPotentialElement.map(lastElement => {
          val distance = element.index - lastElement.index
          if (1 < distance && distance <= maxDistance) {
            for (i <- lastElement.index to element.index) {
              val el = article.elements(i)
              if(!el.isInstanceOf[LinkElement]) el.isPotential = true
            }
          }
        })
        lastPotentialElement = Some(element)
      }
    })
  }
}
