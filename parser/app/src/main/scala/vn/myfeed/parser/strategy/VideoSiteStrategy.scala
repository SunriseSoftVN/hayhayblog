package vn.myfeed.parser.strategy

import vn.myfeed.parser.model.Article
import vn.myfeed.parser.processor.youtube.VideoSiteExtractor
import vn.myfeed.parser.util.VideoSitePattern

/**
 * The Class VideoSiteStrategy.
 *
 * @author Nguyen Duc Dung
 * @since 6/19/13 10:47 PM
 *
 */
class VideoSiteStrategy extends Strategy {

  val processors = List(new VideoSiteExtractor)

  def isRight(implicit article: Article) = article.baseUrl != null && VideoSitePattern.matches(article.doc.baseUri())

}
