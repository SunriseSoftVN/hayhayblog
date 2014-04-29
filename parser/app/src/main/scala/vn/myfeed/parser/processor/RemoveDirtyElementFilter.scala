package vn.myfeed.parser.processor

import vn.myfeed.parser.model.Article
import vn.myfeed.parser.util.DirtyElementPattern
import collection.JavaConversions._

/**
 * The Class RemoveDirtyElementFilter. This filter should only using for manual mode,
 * because i might take bad effect result of auto mode.
 *
 * @author Nguyen Duc Dung
 * @since 12/29/12 5:53 PM
 *
 */
class RemoveDirtyElementFilter extends Processor {

  def process(implicit article: Article) {
    val removeElements = article.containerElement.getAllElements.filter(element => {
      DirtyElementPattern.matchClass(element) || DirtyElementPattern.matchId(element)
    })

    removeElements.foreach(_.remove())
  }
}
