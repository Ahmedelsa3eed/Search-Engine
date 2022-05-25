package searchEngine;

import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchEngineTest {

    SearchEngine searchEngine;
    String filePath = "C:\\CSED\\2nd year\\2nd-semester\\Data Structure 2\\Labs\\Wikipedia Data Sample\\Wikipedia Data Sample\\wiki_00";
    String dirPath = "C:\\CSED\\2nd year\\2nd-semester\\Data Structure 2\\Labs\\Wikipedia Data Sample\\Wikipedia Data Sample";

    @BeforeEach
    void init(){
        searchEngine = new SearchEngine();
    }

    @org.junit.jupiter.api.Test
    void indexWebPage() {
        searchEngine.indexWebPage(filePath);
        assertEquals(475, searchEngine.webPageIndices.size());
    }

    @org.junit.jupiter.api.Test
    void indexDirectory() {
        int expected = 475 + 373 + 410 + 429 + 474 + 351 + 464 + 502 + 343 + 419 + 303;
        searchEngine.indexDirectory(dirPath);
        assertEquals(expected, searchEngine.webPageIndices.size());
    }

    @org.junit.jupiter.api.Test
    void searchByWordWithRanking() {
        searchEngine.indexWebPage(filePath);
        List<ISearchResult> result = searchEngine.searchByWordWithRanking("Leonardo");
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals(2, result.get(0).getRank())
        );
    }

    @org.junit.jupiter.api.Test
    void testTwoForSearchingOnOneWord() {
        searchEngine.indexWebPage(filePath);
        List<ISearchResult> result = searchEngine.searchByWordWithRanking("unique");
        int totalRank = 0;
        for(ISearchResult res: result){
            totalRank += res.getRank();
        }
        assertEquals(13, totalRank);
    }

    @org.junit.jupiter.api.Test
    void searchByMultipleWordWithRanking() {
        searchEngine.indexWebPage(filePath);
        List<ISearchResult> result = searchEngine.searchByMultipleWordWithRanking("Peya Davis Cup Britain");
        assert result != null;
        assertEquals("7697641", result.get(0).getId());
    }

    @org.junit.jupiter.api.Test
    void searchByMultipleWordWithRankingSecondTest() {
        searchEngine.indexWebPage(filePath);
        List<ISearchResult> result = searchEngine.searchByMultipleWordWithRanking("Mexico Olympics Summer");
        List<String> ids = new ArrayList<>();
        for (ISearchResult res: result) {
            ids.add(res.getId());
        }
        assertAll(
                () -> assertTrue(ids.contains("7697794")),
                () -> assertTrue(ids.contains("7697800"))
        );
    }

    @org.junit.jupiter.api.Test
    void deleteWebPage() {
        searchEngine.indexWebPage(filePath);
        String deletedDocsPath = "C:\\CSED\\2nd year\\2nd-semester\\Data Structure 2\\Labs\\hasDocToDelete\\wiki_00";
        searchEngine.deleteWebPage(deletedDocsPath);
        List<ISearchResult> res1 = searchEngine.searchByWordWithRanking("Minolta");
        List<ISearchResult> res2 = searchEngine.searchByWordWithRanking("twentiethcentury");
        List<ISearchResult> res3 = searchEngine.searchByWordWithRanking("Stockton");
        assertAll(
                () -> assertEquals(0, res1.size()),
                () -> assertEquals(0, res2.size()),
                () -> assertEquals(0, res3.size())
        );
    }
}