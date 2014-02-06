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

  def count(domain: String, uniqueTitle: String) = Action {
    ArticleDao.findByUniqueTitleAndDomain(domain, uniqueTitle).map(article => {
      ArticleDao.save(article.copy(clicked = article.clicked + 1))
      Redirect(article.url)
    }).getOrElse(NotFound)
  }

}
