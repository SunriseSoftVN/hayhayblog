package controllers

import play.api.mvc.Controller
import jp.t2v.lab.play2.auth.OptionalAuthElement
import auth.AuthConfigImpl
import dao.{ArticleDao, CategoryDao}

/**
 * The Class CategoryCtr.
 *
 * @author Nguyen Duc Dung
 * @since 1/31/14 10:01 PM
 *
 */
object CategoryCtr extends Controller with OptionalAuthElement with AuthConfigImpl with MainTemplate {

  def index = StackAction(implicit request => {
    renderOk(views.html.category.index())
  })

  def view(shortName: String) = StackAction(implicit request => {
    CategoryDao.findByShortName(shortName).mapRender(cat => {
      val article = ArticleDao.findByCatId(cat._id)
      renderOk(views.html.category.view(cat, article))
    })
  })

}
