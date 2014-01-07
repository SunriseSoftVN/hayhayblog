package admin.controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.AuthElement
import auth.AuthConfigImpl
import model.Administrator
import dao.CategoryDao
import utils.String2Int

/**
 * The Class Categories.
 *
 * @author Nguyen Duc Dung
 * @since 1/7/14 4:57 PM
 *
 */
object Categories extends Controller with AuthElement with AuthConfigImpl with AdminTemplate {

  lazy val query_field = "name"
  lazy val sort_field = "name"

  def index = StackAction(AuthorityKey -> Administrator)(implicit request => {
    val page = request.getQueryString("page").collect {
      case String2Int(_page) => _page
    }.getOrElse(1)

    val query = request.getQueryString("query")

    val users = CategoryDao.query(
      fieldName = query_field,
      fieldValue = query,
      sortFiled = sort_field,
      page = page
    )

    val totalPage = CategoryDao.totalPage(
      fieldName = query_field,
      fieldValue = query
    )

    renderOk(admin.views.html.categories.list(users, totalPage, page, query))
  })

}
