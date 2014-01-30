package admin.controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.AuthElement
import auth.AuthConfigImpl
import model.{Category, Administrator}
import dao.CategoryDao
import play.api.data.Form
import play.api.data.Forms._
import com.mongodb.casbah.commons.Imports._
import formater.ObjectIdFormat._
import dto.PageDto

/**
 * The Class CategoryCtr.
 *
 * @author Nguyen Duc Dung
 * @since 1/7/14 4:57 PM
 *
 */
object CategoryCtr extends Controller with AuthElement with AuthConfigImpl with AdminTemplate {

  lazy val form = Form(
    mapping(
      "_id" -> default(of[ObjectId], new ObjectId()),
      "name" -> text(minLength = 3)
    )(Category.apply)(Category.unapply)
  )

  def index = StackAction(AuthorityKey -> Administrator)(implicit request => {
    val pageDto: PageDto[Category] = PageDto(request)
    renderOk(admin.views.html.categories.list(CategoryDao.query(pageDto)))
  })

  def create = StackAction(AuthorityKey -> Administrator)(implicit request => {
    renderOk(admin.views.html.categories.edit(form))
  })

  def edit(id: ObjectId) = StackAction(AuthorityKey -> Administrator)(implicit request => {
    CategoryDao.findOneById(id).map(category => {
      renderOk(admin.views.html.categories.edit(form.fill(category)))
    }).getOrElse(NotFound)
  })

  def update = StackAction(AuthorityKey -> Administrator)(implicit request => {
    form.bindFromRequest.fold(
      error => renderBadRequest(admin.views.html.categories.edit(error)),
      category => {
        CategoryDao.save(category)
        Redirect("/admin/category")
      }
    )
  })

  def delete(id: ObjectId) = StackAction(AuthorityKey -> Administrator)(implicit request => {
    CategoryDao.removeById(id)
    Redirect("/admin/category")
  })

}
