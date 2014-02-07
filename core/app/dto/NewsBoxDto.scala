package dto

import model.{Blog, Article, Category}

/**
 * The Class NewsBoxDto.
 *
 * @author Nguyen Duc Dung
 * @since 2/3/14 1:44 AM
 *
 */
case class NewsBoxDto(
                       title: String,
                       shortName: String,
                       bigArticle: Article,
                       blogs: List[Blog] = Nil,
                       articles: List[Article]
                       )

object NewsBoxDto {

  def apply(cat: Category, blogs: List[Blog], articles: List[Article]) = {
    val bigArticle = articles.head
    val _articles = articles.filterNot(_ == bigArticle)
    new NewsBoxDto(
      title = cat.name,
      shortName = cat.shortName,
      bigArticle = bigArticle,
      articles = _articles,
      blogs = blogs
    )
  }

}