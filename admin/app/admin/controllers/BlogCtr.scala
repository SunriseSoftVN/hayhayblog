package admin.controllers

import play.api.mvc.Controller
import jp.t2v.lab.play2.auth.AuthElement
import auth.AuthConfigImpl
import dto.PageDto
import model.Administrator
import dao.BlogDao
import play.api.data.Form
import play.api.data.Forms._
import com.mongodb.casbah.commons.Imports._
import model.Blog
import formater.ObjectIdFormat._

/**
 * The Class BlogCtr.
 *
 * @author Nguyen Duc Dung
 * @since 1/31/14 3:21 AM
 *
 */
object BlogCtr extends Controller with AuthElement with AuthConfigImpl with AdminTemplate {

  private def editForm = Form(
    mapping(
      "_id" -> of[ObjectId],
      "name" -> text(minLength = 3),
      "url" -> text(minLength = 6),
      "rssUrl" -> text(minLength = 6),
      "isEnable" -> boolean,
      "categoryId" -> of[ObjectId]
    )(Blog.apply)(Blog.unapply)
  )

  def index = StackAction(AuthorityKey -> Administrator)(implicit request => {
    val pageDto: PageDto[Blog] = PageDto(request)
    renderOk(admin.views.html.blog.list(BlogDao.query(pageDto)))
  })

  def create = StackAction(AuthorityKey -> Administrator)(implicit request => {
    renderOk(admin.views.html.blog.edit(editForm))
  })

}
