/*
 * ITunesGeneratorTest.java
 * JUnit based test
 *
 * Created on August 2, 2005, 2:31 PM
 */
package org.rometools.feed.module;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.SyndFeedOutput;
import com.sun.syndication.io.XmlReader;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.rometools.feed.module.itunes.AbstractITunesObject;

import java.io.File;
import java.util.List;

/**
 * 
 * @author cooper
 */
public class ITunesGeneratorTest extends AbstractTestCase {

    public ITunesGeneratorTest(final String testName) {
        super(testName);
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite(ITunesGeneratorTest.class);

        return suite;
    }

    /**
     * Test of generate method, of class com.totsp.xml.syndication.itunes.ITunesGenerator.
     */
    public void testEndToEnd() throws Exception {
        System.out.println("testEndToEnd");
        testFile("xml/leshow.xml");

        // testFile( "/test/xml/apple.xml" );
        // testFile( "/test/xml/lr.xml" );
    }

    private void testFile(final String filename) throws Exception {
        final File feed = new File(getTestFile(filename));
        final SyndFeedInput input = new SyndFeedInput();
        final SyndFeed syndfeed = input.build(new XmlReader(feed.toURL()));

        final SyndFeedOutput output = new SyndFeedOutput();
        final File outfeed = new File("target/" + feed.getName());
        output.output(syndfeed, outfeed);

        final SyndFeed syndCheck = input.build(new XmlReader(outfeed.toURL()));
        System.out.println(syndCheck.getModule(AbstractITunesObject.URI).toString());
        assertEquals("Feed Level: ", syndfeed.getModule(AbstractITunesObject.URI).toString(), syndCheck.getModule(AbstractITunesObject.URI).toString());

        final List syndEntries = syndfeed.getEntries();
        final List syndChecks = syndCheck.getEntries();

        for (int i = 0; i < syndEntries.size(); i++) {
            final SyndEntry entry = (SyndEntry) syndEntries.get(i);
            final SyndEntry check = (SyndEntry) syndChecks.get(i);
            System.out.println("Original: " + entry.getModule(AbstractITunesObject.URI));
            System.out.println("Check:    " + check.getModule(AbstractITunesObject.URI));
            assertEquals("Entry Level: ", entry.getModule(AbstractITunesObject.URI).toString(), check.getModule(AbstractITunesObject.URI).toString());
        }
    }
}
