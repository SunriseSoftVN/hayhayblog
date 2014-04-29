package vn.myfeed.parser.processor

import vn.myfeed.parser.model.Article
import vn.myfeed.parser.util.DirtyImagePattern
import utils.String2Int

/**
 * The Class ImageFilter.
 *
 * @author Nguyen Duc Dung
 * @since 12/24/12 9:15 PM
 *
 */
class DirtyImageFilter extends Processor {

  def process(implicit article: Article) {
    article.mediaElements.foreach(element => {
      if (DirtyImagePattern.matches(element.src)) {
        element.isPotential = false
        element.isDirty = true
      }

      //Size is too small
      element.jsoupEl.attr("width") match {
        case String2Int(width) => if (width < 20) {
          element.isPotential = false
          element.isDirty = true
        }
        case _ => // do nothing
      }

      element.jsoupEl.attr("height") match {
        case String2Int(height) => if (height < 20) {
          element.isPotential = false
          element.isDirty = true
        }
        case _ => // do nothing
      }

    })

  }
}
