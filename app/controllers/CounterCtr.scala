package controllers

import play.api.mvc.{Action, Controller}
import dao.ArticleDao

/**
 * The Class CounterCtr.
 *
 * @author Nguyen Duc Dung
 * @since 2/3/14 2:48 PM
 *
 */
object CounterCtr extends Controller {

  def count(url: String) = Action {
    ArticleDao.updateClick(url)
    Redirect(url)
  }

}
