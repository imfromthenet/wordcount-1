import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import input.InputReader;
import input.impl.OneLineInputReaderImpl;
import input.impl.WholeInputReaderImpl;
import output.OutputWriter;
import output.impl.StdOutOutputWriter;
import text.obtain.TextObtainer;
import text.obtain.impl.TextObtainerImpl;
import text.obtain.impl.TextObtainerWithIntroTextImpl;
import text.split.TextSplitter;
import text.split.impl.WhiteSpaceTextSplitterImpl;
import word.count.WordCounter;
import word.count.impl.WordCounterImpl;
import word.filter.WordFilter;
import word.filter.impl.AzWordFilterImpl;
import word.filter.impl.ExcludedWordFilterImpl;

public class Main {

    private static final String STOPWORDS_FILENAME = "stopwords.txt";
    private static final String WORDS_COUNT_INTRO_TEXT = "Number of words: ";
    private static final String INPUT_TEXT_READING_INTRO_TEXT = "Enter text: ";

    private final OutputWriter resultOutputWriter;
    private final TextObtainer inputTextObtainer;
    private final WordCounter wordCounter;

    public Main(OutputWriter resultOutputWriter, TextObtainer inputTextObtainer, WordCounter wordCounter) {
        this.resultOutputWriter = resultOutputWriter;
        this.inputTextObtainer = inputTextObtainer;
        this.wordCounter = wordCounter;
    }

    public static void main(String[] args) throws FileNotFoundException {
        TextObtainer inputTextObtainer = initTextObtainerBasedOnArguments(args);
        OutputWriter resultOutputWriter = new StdOutOutputWriter();
        WordCounter wordCounter = initWordCounter(STOPWORDS_FILENAME);

        Main main = new Main(resultOutputWriter, inputTextObtainer, wordCounter);
        main.run();
    }

    public void run() {
        String text = inputTextObtainer.obtainText();
        long wordCount = wordCounter.count(text);
        resultOutputWriter.write(WORDS_COUNT_INTRO_TEXT);
        resultOutputWriter.write(String.valueOf(wordCount));
    }

    private static TextObtainer initTextObtainerBasedOnArguments(String[] args) throws FileNotFoundException {
        return args.length > 0 ? initTextObtainerForFile(args[0]) : initTextObtainerForStdIn();
    }

    private static TextObtainer initTextObtainerForFile(String fileName) throws FileNotFoundException {
        InputReader fileInputReader = initFileInputReader(fileName);
        return new TextObtainerImpl(fileInputReader);
    }

    private static TextObtainer initTextObtainerForStdIn() {
        OutputWriter stdOutOutputWriter = new StdOutOutputWriter();
        InputReader stdInInputReader = initStdInInputReader();
        return new TextObtainerWithIntroTextImpl(stdInInputReader, stdOutOutputWriter, INPUT_TEXT_READING_INTRO_TEXT);
    }

    private static InputReader initFileInputReader(String fileName) throws FileNotFoundException {
        return new WholeInputReaderImpl(new Scanner(new FileInputStream(fileName)));
    }

    private static InputReader initStdInInputReader() {
        return new OneLineInputReaderImpl(new Scanner(System.in));
    }

    private static WordCounter initWordCounter(String stopWordsFileName) throws FileNotFoundException {
        WordFilter excludeStopWordFilter = initExcludeStopWordMatcher(stopWordsFileName);
        WordFilter azWordFilter = new AzWordFilterImpl();
        TextSplitter textSplitter = new WhiteSpaceTextSplitterImpl();
        return new WordCounterImpl(Arrays.asList(azWordFilter, excludeStopWordFilter), textSplitter);
    }

    private static WordFilter initExcludeStopWordMatcher(String stopWordsFileName) throws FileNotFoundException {
        InputReader fileInputReader = initFileInputReader(stopWordsFileName);
        List<String> stopWords = fileInputReader.getInputByLines();
        return new ExcludedWordFilterImpl(stopWords);
    }

}
