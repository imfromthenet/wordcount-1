package wordcount;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Nándor Előd Fekete
 */
public class WordCountApp {

    public static void main(String[] args) throws IOException {
        final WordCounter wordCounter = new WordCounter();
        final long wordCount = wordCounter.wordCount(new InputStreamReader(System.in));
        System.out.printf("Number of words: %d", wordCount);
    }

}
