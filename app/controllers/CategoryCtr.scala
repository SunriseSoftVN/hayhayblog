package controllers

import play.api.mvc.Controller
import jp.t2v.lab.play2.auth.OptionalAuthElement
import auth.AuthConfigImpl
import dao.{ArticleDao, CategoryDao}
import dto.TopMenuDto

/**
 * The Class CategoryCtr.
 *
 * @author Nguyen Duc Dung
 * @since 1/31/14 10:01 PM
 *
 */
object CategoryCtr extends Controller with OptionalAuthElement with AuthConfigImpl with MainTemplate {

  def index(shortName: String, page: Int) = StackAction(implicit request => {
    CategoryDao.findByShortName(shortName).mapRender(cat => {
      val (articles, totalPage) = ArticleDao.findByCatId(cat._id, page)
      val articlesNextPage = if(page < totalPage) ArticleDao.findByCatId(cat._id, page + 1)._1 else Nil
      implicit val topMenuDto = TopMenuDto(CategoryDao.all, cat.shortName)
      renderOk(views.html.category.view(articles, ArticleDao.mostRead(cat._id, 5), articlesNextPage, totalPage, page))
    })
  })

}
