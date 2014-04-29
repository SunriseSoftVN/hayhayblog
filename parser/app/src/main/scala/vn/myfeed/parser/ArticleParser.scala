package vn.myfeed.parser

import model.Article
import org.apache.commons.lang3.StringUtils
import vn.myfeed.parser.strategy.{VideoSiteStrategy, DefaultStrategy}
import java.io.InputStream

/**
 * The Class ArticleParser.
 *
 * @author Nguyen Duc Dung
 * @since 12/23/12 1:45 AM
 *
 */
class ArticleParser {

  //The order of strategy is very important, the parser only using the first match strategy
  val strategies = List(
    new VideoSiteStrategy,
    new DefaultStrategy
  )

  /**
   * Parse a html document to an article
   * @param input
   * @return
   */
  def parse(input: InputStream, url: String): Article = parse(input, "", None, url)

  /**
   * Parse a html document with special title to an article
   * @param input
   * @param title
   * @param rssHtml
   * @param url
   * @return
   */
  def parse(input: InputStream, title: String, rssHtml: Option[String], url: String) = {
    implicit val article = new Article(input, url)
    if (StringUtils.isNotBlank(title)) article.title = title
    article.rssHtml = rssHtml
    strategies.find(_.isRight).map(_.process)
    article
  }

}
