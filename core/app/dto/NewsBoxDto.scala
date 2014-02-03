package dto

import model.{Article, Category}

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
                       articles: List[Article]
                       )

object NewsBoxDto {

  def apply(cat: Category, articles: List[Article]) = {
    val bigNews = articles.find(_.featureImage.isDefined)
    val _articles = if (bigNews.isDefined) articles.filterNot(_ == bigNews.get) else articles
    new NewsBoxDto(
      title = cat.name,
      shortName = cat.shortName,
      bigArticle = bigNews,
      articles = _articles
    )
  }

}