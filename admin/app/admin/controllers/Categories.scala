package admin.controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.AuthElement
import auth.AuthConfigImpl
import model.{Category, Administrator}
import dao.CategoryDao
import utils.String2Int
import play.api.data.Form
import play.api.data.Forms._
import com.mongodb.casbah.commons.Imports._
import formater.ObjectIdFormat._

/**
 * The Class Categories.
 *
 * @author Nguyen Duc Dung
 * @since 1/7/14 4:57 PM
 *
 */
object Categories extends Controller with AuthElement with AuthConfigImpl with AdminTemplate {

  lazy val form = Form(
    mapping(
      "_id" -> default(of[ObjectId], new ObjectId()),
      "name" -> nonEmptyText(minLength = 3)
    )(Category.apply)(Category.unapply)
  )

  def index = StackAction(AuthorityKey -> Administrator)(implicit request => {
    Ok
  })

  def create = StackAction(AuthorityKey -> Administrator)(implicit request => {
    renderOk(admin.views.html.categories.edit(form))
  })

}
