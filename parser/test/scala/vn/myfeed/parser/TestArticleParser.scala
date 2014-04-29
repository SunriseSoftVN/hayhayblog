package vn.myfeed.parser

import org.junit.Test
import vn.myfeed.parser.model.Article
import java.io.ByteArrayInputStream
import scala.slick.driver.H2Driver.simple._
import Database.threadLocalSession
import vn.myfeed.parser.h2.Db
import org.expecty.Expecty
import grizzled.slf4j.Logging
import org.apache.http.client.methods.HttpGet
import http.HttpClientBuilder

/**
 * The Class TestArticleParser.
 *
 * @author Nguyen Duc Dung
 * @since 6/15/13 4:18 AM
 *
 */
class TestArticleParser extends Logging {

  val expect = new Expecty(failEarly = false)

  val httpclient = HttpClientBuilder.build()

  @Test
  def testParserWithUrl() {
    val url = "http://gamek.vn/esport/trang-bi-lien-minh-huyen-thoai-moi-xuat-hien-o-ban-do-vgh-bang-cau-manh-hay-yeu-20130728085746885.chn"
    val response = httpclient.execute(new HttpGet(url))
    if (response.getStatusLine.getStatusCode == 200) {
      val parser = new ArticleParser
      val article = parser.parse(
        input = response.getEntity.getContent,
        url = url,
        title = "Trang bị Liên Minh Huyền Thoại mới xuất hiện ở bản đồ VGH - Băng Cầu mạnh hay yếu",
        rssHtml = None
      )
      println(article.text)
    }
  }

  @Test
  def testParserWithDb() {
    var passed = true
    Db.database.withSession {
      Query(Article).foreach {
        case (url, html, title, text, contentHtml) => {
          val input = new ByteArrayInputStream(html)
          val parser = new ArticleParser
          val article = parser.parse(input, url)
          if (article.title != title || article.text != text || article.contentHtml != contentHtml) {
            println(url)
            passed = false
          }
        }
      }
    }

    expect {
      passed == true
    }
  }

}
