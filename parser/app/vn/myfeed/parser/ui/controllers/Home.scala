package vn.myfeed.parser.ui.controllers

import play.api.mvc.{Action, Controller}
import vn.myfeed.parser.ArticleParser
import play.api.libs.json.Json
import play.api.data.Form
import play.api.data.Forms._
import vn.myfeed.parser.model.Article
import scala.slick.driver.H2Driver.simple._
import Database.threadLocalSession
import vn.myfeed.parser.h2.Db
import org.apache.http.client.methods.HttpGet
import org.apache.http.util.EntityUtils
import http.HttpClientBuilder

/**
 * The Class Home.
 *
 * @author Nguyen Duc Dung
 * @since 6/12/13 10:39 PM
 *
 */
object Home extends Controller {

  var lastResult: Option[Article] = None

  val httpClient = HttpClientBuilder.build()

  def index = Action {
    Ok(vn.myfeed.parser.ui.views.html.home())
  }

  def parseUrl = Action {
    implicit request =>
      Form("url" -> nonEmptyText).bindFromRequest.fold(
        error => {
          lastResult = None
          NotFound
        },
        url => {
          var downloadTime = System.currentTimeMillis()
          val httpGet = new HttpGet(url)
          val response = httpClient.execute(httpGet)
          downloadTime = System.currentTimeMillis() - downloadTime

          if (response.getStatusLine.getStatusCode == 200) {

            var parseTime = System.currentTimeMillis()
            val parser = new ArticleParser
            val article = parser.parse(response.getEntity.getContent, url)
            parseTime = System.currentTimeMillis() - parseTime

            println(s"Download: $downloadTime ms")
            println(s"Parse: $parseTime ms")

            lastResult = Some(article)

            EntityUtils.consume(response.getEntity)

            Ok(Json.obj(
              "title" -> article.title,
              "text" -> article.contentHtml
            ))
          } else {
            println(response.getStatusLine.getStatusCode)
            EntityUtils.consume(response.getEntity)
            BadRequest
          }
        }
      )
  }

  def saveUrl = Action {
    Db.database.withSession {
      lastResult.map(result => {
        val q = for (a <- Article if a.url === result.url) yield a
        q.firstOption.map(r => {
          println("Updated")
          q.update(result.url, result.bytes, result.title, result.text, result.contentHtml)
        }).getOrElse {
          println("Saved")
          Article.insert(result.url, result.bytes, result.title, result.text, result.contentHtml)
        }
      })
    }
    Redirect("/parser")
  }

  def deleteUrl = Action {
    Db.database.withSession {
      lastResult.map(result => {
        val q = for (a <- Article if a.url === result.url) yield a
        q.delete
        println("Deleted")
      })
    }
    Redirect("/parser")
  }


}
