package searchEngine;

import BTree.BTree;
import BTree.BTreeNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchEngine implements ISearchEngine {

    /* maintain an index of these documents content using the B-Tree */
    HashMap<String ,BTree> webPageIndices;
    XMLParser parser;

    public SearchEngine() {
        parser = new XMLParser();
    }

    /**
     * index documents content inside filePath to be able to
     * search through them later
     * */
    @Override
    public void indexWebPage(String filePath) {
        webPageIndices = parser.readXML(filePath);
    }

    @Override
    public void indexDirectory(String directoryPath) {
        try {
            webPageIndices = new HashMap<>();
            Path dir = Paths.get(directoryPath);
            Files.walk(dir).forEach(path -> makeDirIndices(path.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeDirIndices(File file){
        if(!file.isDirectory()){
            webPageIndices.putAll(parser.readXML(file.getAbsolutePath()));
        }
    }

    @Override
    public void deleteWebPage(String filePath) {
        XMLParser newParser = new XMLParser();
        HashMap<String ,BTree> docs = newParser.readXML(filePath);
        for(String id: docs.keySet()) {
            webPageIndices.remove(id);
        }
    }

    @Override
    public List<ISearchResult> searchByWordWithRanking(String word) {
        List<ISearchResult> searchResultList = new ArrayList<>();

        for (String id: webPageIndices.keySet()) {
            ISearchResult searchResult = new SearchResult();
            BTreeNode node = webPageIndices.get(id).search(word);
            if(node != null){
                int rankIndex = node.find(word);
                searchResult.setRank(node.getValues()[rankIndex]);
                searchResult.setId(id);
                searchResultList.add(searchResult);
            }
        }
        return searchResultList;
    }

    @Override
    public List<ISearchResult> searchByMultipleWordWithRanking(String sentence) {
        List<ISearchResult> searchResultList = new ArrayList<>();

        for (String id: webPageIndices.keySet()) {
            int minFreq = 0;
            boolean isAllFound = true;
            ISearchResult searchResult = new SearchResult();
            String[] words = sentence.split("\\s");
            for (String word: words) {
                BTreeNode node = webPageIndices.get(id).search(word);
                if (node != null) {
                    int rankIndex = node.find(word);
                    minFreq = Math.min(minFreq, rankIndex);
                }
                else {
                    isAllFound = false;
                    break;
                }
            }
            if (isAllFound) {
                searchResult.setRank(minFreq);
                searchResult.setId(id);
                searchResultList.add(searchResult);
            }
        }
        return searchResultList;
    }

    public static void main(String[] args) {
        SearchEngine searchEngine = new SearchEngine();
        String filePath = "C:\\CSED\\2nd year\\2nd-semester\\Data Structure 2\\Labs\\Wikipedia Data Sample\\Wikipedia Data Sample\\wiki_00";
        searchEngine.indexWebPage(filePath);
        searchEngine.searchByWordWithRanking("Leonardo");
    }
}
