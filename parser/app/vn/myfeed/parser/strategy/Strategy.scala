package vn.myfeed.parser.strategy

import vn.myfeed.parser.model.Article
import vn.myfeed.parser.processor.Processor

/**
 * The Class Controller.
 *
 * @author Nguyen Duc Dung
 * @since 12/23/12 3:37 AM
 *
 */
trait Strategy extends Processor {

  val processors: List[Processor]

  def process(implicit article: Article) {
    processors.foreach(_.process)
  }

  def isRight(implicit article: Article): Boolean

}