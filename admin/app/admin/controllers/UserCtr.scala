package admin.controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.AuthElement
import auth.AuthConfigImpl
import model.{User, Administrator}
import dto.PageDto
import dao.UserDao
import play.api.data.Form
import play.api.data.Forms._
import org.bson.types.ObjectId
import formater.ObjectIdFormat._
import org.apache.commons.codec.digest.DigestUtils
import org.joda.time.DateTime

/**
 * The Class Users.
 *
 * @author Nguyen Duc Dung
 * @since 1/7/14 2:04 PM
 *
 */
object UserCtr extends Controller with AuthElement with AuthConfigImpl with AdminTemplate {


  private def editForm = Form(
    mapping(
      "_id" -> of[ObjectId],
      "email" -> email,
      "password" -> text(minLength = 6),
      "fullname" -> optional(text),
      "role" -> text,
      "registerDate" -> default(jodaDate, DateTime.now())
    )(User.apply)(User.unapply)
  )


  def index = StackAction(AuthorityKey -> Administrator)(implicit request => {
    val pageDto: PageDto[User] = PageDto(request)
    renderOk(admin.views.html.user.list(UserDao.query(pageDto)))
  })

  def create = StackAction(AuthorityKey -> Administrator)(implicit request => {
    renderOk(admin.views.html.user.edit(editForm))
  })

  def edit(id: ObjectId) = StackAction(AuthorityKey -> Administrator)(implicit request => {
    UserDao.findOneById(id).map(user => {
      renderOk(admin.views.html.user.edit(editForm.fill(user)))
    }).getOrElse(NotFound)
  })

  def update = StackAction(AuthorityKey -> Administrator)(implicit request => {
    editForm.bindFromRequest.fold(
      error => renderOk(admin.views.html.user.edit(error)),
      user => {
        if (UserDao.findOneById(user._id).isDefined) {
          UserDao.save(user)
        } else {
          UserDao.insert(user.copy(password = DigestUtils.md5Hex(user.password)))
        }
        Redirect("/admin/user")
      }
    )
  })

}
