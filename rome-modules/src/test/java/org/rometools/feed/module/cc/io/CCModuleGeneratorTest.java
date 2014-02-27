/*
 * CCModuleGeneratorTest.java
 * JUnit based test
 *
 * Created on November 20, 2005, 9:48 PM
 */

package org.rometools.feed.module.cc.io;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.SyndFeedOutput;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.rometools.feed.module.AbstractTestCase;
import org.rometools.feed.module.cc.CreativeCommons;

import java.io.File;

/**
 * 
 * @author <a href="mailto:cooper@screaming-penguin.com">Robert "kebernet" Cooper</a>
 */
public class CCModuleGeneratorTest extends AbstractTestCase {

    public CCModuleGeneratorTest(final String testName) {
        super(testName);
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite(CCModuleGeneratorTest.class);

        return suite;
    }

    public void testGenerate() throws Exception {
        System.out.println("testGenerate");
        final SyndFeedInput input = new SyndFeedInput();
        final SyndFeedOutput output = new SyndFeedOutput();
        final File testDir = new File(super.getTestFile("xml"));
        final File[] testFiles = testDir.listFiles();
        for (int h = 0; h < testFiles.length; h++) {
            if (!testFiles[h].getName().endsWith(".xml")) {
                continue;
            }
            System.out.println(testFiles[h].getName());
            final SyndFeed feed = input.build(testFiles[h]);
            // if( !feed.getFeedType().equals("rss_1.0"))
            {
                feed.setFeedType("rss_2.0");
                if (feed.getDescription() == null) {
                    feed.setDescription("test file");
                }
                output.output(feed, new File("target/" + testFiles[h].getName()));
                final SyndFeed feed2 = input.build(new File("target/" + testFiles[h].getName()));
                for (int i = 0; i < feed.getEntries().size(); i++) {
                    final SyndEntry entry = feed.getEntries().get(i);
                    final SyndEntry entry2 = feed2.getEntries().get(i);
                    final CreativeCommons base = (CreativeCommons) entry.getModule(CreativeCommons.URI);
                    final CreativeCommons base2 = (CreativeCommons) entry2.getModule(CreativeCommons.URI);
                    System.out.println(base2);
                    // if( base != null)
                    // this.assertEquals( testFiles[h].getName(), base.getLicenses(), base2.getLicenses() );
                }
            }
        }

    }

}
