package auth

import play.api.mvc._
import play.api.mvc.Results._
import scala.reflect._
import jp.t2v.lab.play2.auth.{CookieIdContainer, IdContainer, AuthConfig}
import play.api.mvc.AcceptExtractors
import model.{Administrator, Permission}
import dao.UserDao

/**
 * The Class AuthConfigImpl.
 *
 * @author Nguyen Duc Dung
 * @since 11/6/12 10:10 AM
 *
 */
trait AuthConfigImpl extends AuthConfig with Rendering with AcceptExtractors {

  type Id = String

  type User = model.User

  type Authority = Permission

  def sessionTimeoutInSeconds = 3600

  def resolveUser(email: Id) = UserDao.findByEmail(email)

  implicit val idTag: ClassTag[Id] = classTag[Id]

  def loginSucceeded(request: RequestHeader) = Redirect("/admin")

  def logoutSucceeded(request: RequestHeader) = Redirect("/user/login")

  def authenticationFailed(request: RequestHeader) = render {
    case Accepts.Json() => Forbidden("Please login")
    case _ => Redirect("/user/login")
  }(request)

  def authorizationFailed(request: RequestHeader) = Forbidden("Please login")

  def authorize(user: User, authority: Authority) = user.role == authority.value || user.role == Administrator.value

  override lazy val idContainer: IdContainer[Id] = new CookieIdContainer[Id]
}