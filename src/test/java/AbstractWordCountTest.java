import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class AbstractWordCountTest {

    public static class TestWordCount extends AbstractWordCount {
        @Override
        public BufferedReader getInput() {
            return null;
        }

        public TestWordCount(String dictionaryName) throws IOException {
            super(dictionaryName);
        }

        public TestWordCount() throws IOException {
            super(null);
        }

    }

    @Test
    public void testSingleWord() throws IOException {
        AbstractWordCount app = new TestWordCount();
        app.determineStatistics(Arrays.asList("word"));
        assertEquals(1, app.getTotalWords());
    }

    @Test
    public void testNotSingleWord() throws IOException {
        AbstractWordCount app = new TestWordCount();
        app.determineStatistics(Arrays.asList("word2"));
        assertEquals(0, app.getTotalWords());
    }

    @Test
    public void testSeveralWords() throws IOException {
        AbstractWordCount app = new TestWordCount();
        app.determineStatistics(Arrays.asList("Mary had a little lamb"));
        assertEquals(4, app.getTotalWords());
    }

    @Test
    public void testSeveralLines() throws IOException {
        AbstractWordCount app = new TestWordCount();
        app.determineStatistics(Arrays.asList("Mary had a little lamb", "another line"));
        assertEquals(6, app.getTotalWords());
    }

    @Test
    public void testUniqueWordsFromExcercise() throws IOException {
        AbstractWordCount app = new TestWordCount();
        app.determineStatistics(Arrays.asList("Humpty-Dumpty sat on a wall. Humpty-Dumpty had a great fall."));
        // the counts are different because we decided that strings with special characters are no words.
        assertEquals(5, app.getTotalWords());
        assertEquals(3, app.getUniqueWords());
    }

    @Test
    public void testUniqueWords() throws IOException {
        AbstractWordCount app = new TestWordCount();
        app.determineStatistics(Arrays.asList("An example with double double words"));
        assertEquals(6, app.getTotalWords());
        assertEquals(4, app.getUniqueWords());
    }

    @Test
    public void testAverageLengthOneWord() throws IOException {
        AbstractWordCount app = new TestWordCount();
        app.determineStatistics(Arrays.asList("simple"));
        assertEquals(6, app.getAverageLength(), 0.001);
    }

    @Test
    public void testAverageLengthMoreWords() throws IOException {
        AbstractWordCount app = new TestWordCount();
        app.determineStatistics(Arrays.asList("Mary had a little lamb"));
        assertEquals(4.25, app.getAverageLength(), 0.006);
    }

    @Test
    public void testIndex() throws IOException {
        AbstractWordCount app = new TestWordCount();
        app.determineStatistics(Arrays.asList("Mary had a little lamb"));
        List<String> words = app.getAllWords();
        assertTrue(words.containsAll(Arrays.asList("Mary", "had", "little", "lamb")));
        assertFalse(words.contains("a"));
    }

    @Test
    public void testIndexDupicateWords() throws IOException {
        AbstractWordCount app = new TestWordCount();
        app.determineStatistics(Arrays.asList("Mary had a little little lamb"));
        List<String> words = app.getAllWords();
        assertTrue(words.containsAll(Arrays.asList("Mary", "had", "little", "lamb")));
        assertFalse(words.contains("a"));
        assertEquals(4, words.size());
    }

    @Test
    public void testDictCheck() throws IOException {
        AbstractWordCount app = new TestWordCount("dict.txt");
        app.determineStatistics(Arrays.asList("Mary had a little little lamb"));
        List<String> words = app.getAllWords();
        assertTrue(words.containsAll(Arrays.asList("Mary*", "had", "little", "lamb*")));
        assertFalse(words.contains("a"));
        assertEquals(4, words.size());
    }

    @Test
    public void testReadStopwords() throws IOException {
        AbstractWordCount app = new TestWordCount();
        assertTrue(app.stopwords.contains("the"));
        assertTrue(app.stopwords.contains("a"));
        assertTrue(app.stopwords.contains("on"));
        assertTrue(app.stopwords.contains("off"));
    }
}
