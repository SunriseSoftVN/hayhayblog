package vn.myfeed.parser.processor

import vn.myfeed.parser.model.Article

/**
 * The Class Processor.
 *
 * @author Nguyen Duc Dung
 * @since 12/23/12 3:35 AM
 *
 */
trait Processor {
  def process(implicit article: Article)
}