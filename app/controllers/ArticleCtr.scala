package controllers

import play.api.mvc.Controller
import jp.t2v.lab.play2.auth.OptionalAuthElement
import auth.AuthConfigImpl
import dao.{BlogDao, ArticleDao, CategoryDao}
import dto.TopMenuDto

/**
 * The Class ArticleCtr.
 *
 * @author Nguyen Duc Dung
 * @since 1/31/14 10:01 PM
 *
 */
object ArticleCtr extends Controller with OptionalAuthElement with AuthConfigImpl with MainTemplate {

  def index(shortName: String, page: Int) = StackAction(implicit request => {
    CategoryDao.findByShortName(shortName).mapRender(cat => {
      val (articles, totalPage) = ArticleDao.findByCatId(cat._id, page)
      implicit val topMenuDto = TopMenuDto(CategoryDao.all, cat.shortName)
      renderOk(views.html.article.view(articles, totalPage, page))
    })
  })

  def blog(blogName: String, page: Int) = StackAction(implicit request => {
    BlogDao.findByBlogName(blogName).mapRender(blog => {
      val (articles, totalPage) = ArticleDao.findByBlogId(blog._id, page)
      val catName = blog.category.map(_.shortName).getOrElse("")
      implicit val topMenuDto = TopMenuDto(CategoryDao.all, catName)
      renderOk(views.html.article.view(articles, totalPage, page))
    })
  })

  def tag(tagName: String, page: Int) = StackAction(implicit request => {
    val (articles, totalPage) = ArticleDao.findByTag(tagName.replaceAll("_", ".*"), page)
    implicit val topMenuDto = TopMenuDto(CategoryDao.all, "none")
    renderOk(views.html.article.view(articles, totalPage, page))
  })

}
