package vn.myfeed.parser.processor

import vn.myfeed.parser.model.Article
import org.apache.commons.lang3.StringUtils
import collection.mutable.ListBuffer
import collection.JavaConversions._
import vn.myfeed.parser.util.{ArticleUtil, StopWordCounter}
import breeze.text.tokenize.JavaWordTokenizer

/**
 * The Class TitleExtractor.
 *
 * @author Nguyen Duc Dung
 * @since 12/26/12 2:51 PM
 *
 */
class TitleExtractor(minTitleLength: Int = 5) extends Processor {

  /**
   * This method to get the title base on element content. In most of case the title will appear in two places,
   * the first one is in document title and the second one on top of the article content. But the title in the title tag is
   * not only title of the article, it usually include another text such as domain name, article category.
   * So we try to get title in document title and search inside the document to find the correct title.
   * @return
   */
  def process(implicit article: Article) {

    if (StringUtils.isNotBlank(article.title)) return

    val counter = new StopWordCounter(article.languageCode)
    val tokenizer = JavaWordTokenizer


    val potentialTitles = new ListBuffer[String]
    val elements = article.containerElement.getAllElements
    val title = article.doc.title()

    //Step1: Try to find potential by checking page content.
    elements.foreach(element => {
      val text = StringUtils.strip(element.text)
      if (StringUtils.isNotBlank(text)
        && ArticleUtil.titleContain(title, text) && text.length > minTitleLength) {
        //In case element is h element, we will pick it as the title and stop searching.
        if ((element.tagName.startsWith("h") || element.attr("class").toLowerCase.contains("title"))
          && element.children.isEmpty) {
          if (tokenizer(text).size > 2) {
            article.title = text
            return
          }
        } else if (!potentialTitles.contains(text)) {
          potentialTitles += text
        }
      }

    })

    //Step2: Find potential title by split the title. Just in case step 1 can't found any potential title.
    if (potentialTitles.isEmpty) {
      var candidateTitle = title
      if (StringUtils.isNotBlank(candidateTitle)) {
        candidateTitle = candidateTitle.replaceAll("\\|", "-")
        candidateTitle = candidateTitle.replaceAll("\\/", "-")
        candidateTitle = candidateTitle.replaceAll("\\\\", "-")
        candidateTitle = candidateTitle.replaceAll("\\//", "-")
        candidateTitle = candidateTitle.replaceAll("»", "-")
        candidateTitle = candidateTitle.replaceAll(":", "-")

        if (candidateTitle.contains("-")) {
          potentialTitles ++= candidateTitle.split("-")
        }
      }
    }

    //Step3: Find the best title base on stop word.
    val bestTitle = getTitleByStopWord(potentialTitles.toList, title, counter)

    if (StringUtils.isNotBlank(bestTitle)) {
      article.title = bestTitle
    }
  }

  private def getTitleByStopWord(potentialTitles: List[String], title: String, counter: StopWordCounter) = {
    var bestTitle = title
    var maxScore = 0

    potentialTitles.foreach(potentialTitle => {
      val score = (counter.count(potentialTitle) + 1) * potentialTitle.length
      if (score > maxScore) {
        maxScore = score
        bestTitle = potentialTitle
      }
    })
    StringUtils.strip(bestTitle)
  }
}
