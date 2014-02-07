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
                       bigArticle: Option[Article] = None,
                       blogs: List[Blog] = Nil,
                       articles: List[Article] = Nil
                       )

object NewsBoxDto {

  def apply(cat: Category, blogs: List[Blog], articles: List[Article]) = {
    if (!articles.isEmpty) {
      val bigArticle = articles.find(_.featureImage.isDefined).orElse(articles.headOption)
      val _articles = if (bigArticle.isDefined) articles.filterNot(_ == bigArticle.get) else articles.take(4)
      new NewsBoxDto(
        title = cat.name,
        shortName = cat.shortName,
        bigArticle = bigArticle,
        articles = _articles,
        blogs = blogs
      )
    } else {
      new NewsBoxDto(
        title = cat.name,
        shortName = cat.shortName,
        blogs = blogs
      )
    }

  }

}