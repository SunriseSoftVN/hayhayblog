package controllers

import play.api.mvc.Controller
import jp.t2v.lab.play2.auth.OptionalAuthElement
import auth.AuthConfigImpl
import dao.{BlogDao, ArticleDao, CategoryDao}
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
      val articlesNextPage = if (page < totalPage) ArticleDao.findByCatId(cat._id, page + 1)._1 else Nil
      implicit val topMenuDto = TopMenuDto(CategoryDao.all, cat.shortName)
      val blogs = BlogDao.findByCatId(cat._id)
      renderOk(views.html.category.view(articles, ArticleDao.mostReadByCatId(cat._id, 5), articlesNextPage, blogs, totalPage, page))
    })
  })

  def blog(domain: String, page: Int) = StackAction(implicit request => {
    BlogDao.findByDomain(domain).mapRender(blog => {
      val (articles, totalPage) = ArticleDao.findByBlogId(blog._id, page)
      val articlesNextPage = if (page < totalPage) ArticleDao.findByBlogId(blog._id, page + 1)._1 else Nil
      val catName = blog.category.map(_.shortName).getOrElse("")
      implicit val topMenuDto = TopMenuDto(CategoryDao.all, catName)
      val blogs = BlogDao.findByCatId(blog.categoryId).filterNot(_._id == blog._id)
      renderOk(views.html.category.view(articles, ArticleDao.mostReadByBlogId(blog._id, 5), articlesNextPage, blogs, totalPage, page))
    })
  })

  def tag(tagName: String, page: Int) = StackAction(implicit request => {
    val (articles, totalPage) = ArticleDao.findByTag(tagName, page)
    val articlesNextPage = if (page < totalPage) ArticleDao.findByTag(tagName, page + 1)._1 else Nil
    implicit val topMenuDto = TopMenuDto(CategoryDao.all, "none")
    renderOk(views.html.category.view(articles, Nil, articlesNextPage, Nil, totalPage, page))
  })

}
