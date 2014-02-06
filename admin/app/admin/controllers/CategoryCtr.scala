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
import org.apache.commons.lang3.StringUtils

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
      "_id" -> of[ObjectId],
      "name" -> text(minLength = 3),
      "shortName" -> text
    )(Category.apply)(Category.unapply)
  )

  def index = StackAction(AuthorityKey -> Administrator)(implicit request => {
    val pageDto: PageDto[Category] = PageDto(request)
    renderOk(admin.views.html.category.list(CategoryDao.query(pageDto)))
  })

  def create = StackAction(AuthorityKey -> Administrator)(implicit request => {
    renderOk(admin.views.html.category.edit(form))
  })

  def edit(id: ObjectId) = StackAction(AuthorityKey -> Administrator)(implicit request => {
    CategoryDao.findOneById(id).map(category => {
      renderOk(admin.views.html.category.edit(form.fill(category)))
    }).getOrElse(NotFound)
  })

  def update = StackAction(AuthorityKey -> Administrator)(implicit request => {
    form.bindFromRequest.fold(
      error => renderBadRequest(admin.views.html.category.edit(error)),
      category => {
        if (StringUtils.isBlank(category.shortName)) {
          val shortName = StringUtils.stripAccents(category.name).replaceAll(" ", "_").toLowerCase
          CategoryDao.save(category.copy(shortName = shortName))
        } else {
          CategoryDao.save(category)
        }
        Redirect("/admin/category")
      }
    )
  })

  def delete(id: ObjectId) = StackAction(AuthorityKey -> Administrator)(implicit request => {
    CategoryDao.removeById(id)
    Redirect("/admin/category")
  })

}
