package parser

/**
 * The Class RssFixer.
 *
 * @author Nguyen Duc Dung
 * @since 8/27/13, 7:36 PM
 *
 */
object RssFixer {

  def fix(rss: String) = rss
    .replaceAll("pubdate>", "pubDate>")
    .replaceAll("updateddate>", "updatedDate>")

}
