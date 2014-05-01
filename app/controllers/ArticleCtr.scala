package controllers

import play.api.mvc.Controller
import jp.t2v.lab.play2.auth.OptionalAuthElement
import auth.AuthConfigImpl
import dao.{BlogDao, ArticleDao, CategoryDao}
import dto.TopMenuDto
import model.SortMode

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
      val sortMode = request.getQueryString("sort").getOrElse(SortMode.newest)
      val (articles, totalPage) = ArticleDao.findByCatId(cat._id, sortMode, page, 20)
      implicit val topMenuDto = TopMenuDto(CategoryDao.all, cat.shortName)
      renderOk(views.html.article.view(articles, totalPage, sortMode), title = "Các blog hay về " + cat.name.toLowerCase)
    })
  })

  def blog(blogName: String, page: Int) = StackAction(implicit request => {
    BlogDao.findByBlogName(blogName).mapRender(blog => {
      val sortMode = request.getQueryString("sort").getOrElse(SortMode.newest)
      val (articles, totalPage) = ArticleDao.findByBlogId(blog._id, sortMode, page, 20)
      val catShortName = blog.category.map(_.shortName).getOrElse("")
      val catName = blog.category.map(_.name).getOrElse("")
      implicit val topMenuDto = TopMenuDto(CategoryDao.all, catShortName)
      renderOk(views.html.article.view(articles, totalPage, sortMode), title = "Blog hay về " + catName  + " - " + blog.name)
    })
  })

  def tag(tagName: String, page: Int) = StackAction(implicit request => {
    val sortMode = request.getQueryString("sort").getOrElse(SortMode.newest)
    val (articles, totalPage) = ArticleDao.findByTag(tagName.replaceAll("_", ".*"), sortMode, page, 20)
    implicit val topMenuDto = TopMenuDto(CategoryDao.all, "none")
    renderOk(views.html.article.view(articles, totalPage, sortMode), title = "Các bài viết hay về " + tagName.replaceAll("_", " "))
  })

}
