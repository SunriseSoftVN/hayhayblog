package vn.myfeed.parser.strategy

import vn.myfeed.parser.processor._
import vn.myfeed.parser.model.Article

/**
 * The Class DefaultStrategy.
 *
 * @author Nguyen Duc Dung
 * @since 6/19/13 10:42 PM
 *
 */
class DefaultStrategy extends Strategy {

  val processors = List(
    //Step1: Remove hidden element , clean document.
    new DocumentCleaner,
    new LanguageDetector,
    new RemoveHiddenElement,
    new RemoveDirtyElementFilter,
    new WhiteListProcessor,
    //Step2: Extract article elements.
    new ArticleExtractor,
    new TitleExtractor,
    //Step3: Try to find potential element.
    new TitleBaseFilter,
    new EndOfContentDetector,
    new NumbOfWordFilter,
    new TagBaseFilter,
    new MediaBaseFilter,
    new DistanceBaseFilter,
    //Step4: Remove bad quality element.
    new DirtyImageFilter,
    new HighLinkDensityFilter,
    //Step5: Only keep high score elements.
    new HighestScoreElementFilter,
    new ContainerElementDetector,
    new ExpandTitleToContentFilter,
    //Step 6: Fixed broken link
    new BrokenLinkFixed,
    new DuplicateCleaner,
    new ContentHtmlProcessor,
    new HtmlFormater
  )

  //This is default processor list using in most of case
  def isRight(implicit article: Article) = true
}
