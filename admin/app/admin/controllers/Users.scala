package admin.controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.AuthElement
import auth.AuthConfigImpl
import model.Administrator
import dao.UserDao
import utils.String2Int

/**
 * The Class Users.
 *
 * @author Nguyen Duc Dung
 * @since 1/7/14 2:04 PM
 *
 */
object Users extends Controller with AuthElement with AuthConfigImpl with AdminTemplate {

  lazy val query_field = "email"
  lazy val sort_field = "registerDate"

  def index = StackAction(AuthorityKey -> Administrator)(implicit request => {
    val page = request.getQueryString("page").collect {
      case String2Int(_page) => _page
    }.getOrElse(1)

    val query = request.getQueryString("query")

    val users = UserDao.query(
      fieldName = query_field,
      fieldValue = query,
      sortFiled = sort_field,
      page = page
    )

    val totalPage = UserDao.totalPage(
      fieldName = query_field,
      fieldValue = query
    )

    renderOk(admin.views.html.users.list(users, totalPage, page, query))
  })

}
