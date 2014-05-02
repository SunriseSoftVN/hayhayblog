package controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.OptionalAuthElement
import auth.AuthConfigImpl
import dao.{EmailDao, BlogDao, ArticleDao}
import com.mongodb.casbah.commons.MongoDBObject
import model.{Email, SortMode}
import org.joda.time.DateTime
import play.api.data.Form
import play.api.data.Forms._

object HomeCtr extends Controller with OptionalAuthElement with AuthConfigImpl with MainTemplate {

  def index(page: Int) = StackAction(implicit request => {
    val blogIds = BlogDao.canShowInHomePage.map(_._id)
    val itemDisplay = 20
    val skip = (page - 1) * itemDisplay
    val query = MongoDBObject("blogId" -> MongoDBObject("$in" -> blogIds))
    val totalRow = ArticleDao.count(query)
    val sortMode = request.getQueryString("sort").getOrElse(SortMode.newest)

    val sort = if (sortMode == SortMode.newest) {
      MongoDBObject("publishedDate" -> -1)
    } else {
      //only in 7 days
      query.put("publishedDate", MongoDBObject("$gt" -> DateTime.now().minusDays(7)))
      MongoDBObject("commentTotal" -> -1, "clicked" -> -1, "publishedDate" -> -1)
    }

    var totalPage = totalRow / itemDisplay
    if (totalRow - totalPage * itemDisplay > 0) {
      totalPage += 1
    }
    val articles = ArticleDao.find(query)
      .skip(skip)
      .limit(itemDisplay)
      .sort(sort)
      .toList

    renderOk(views.html.index(articles, totalPage, sortMode))
  })


  def landing = StackAction(implicit request => {
    Ok(views.html.landing())
  })

  val subscribeForm = Form {
    "email" -> email
  }

  def subscribe = StackAction(implicit request => {
    subscribeForm.bindFromRequest.fold(
      error => Ok("invalid_email"),
      email => {
        if (EmailDao.findByEmail(email).isEmpty) {
          EmailDao.save(Email(email = email))
        }
        Ok("success")
      }
    )
  })

}