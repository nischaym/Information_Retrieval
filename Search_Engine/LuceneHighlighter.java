package project;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneHighlighter {
	 
private static final String INDEX_DIRECTORY_PATH = "demo";
 
public void createIndex() throws Exception {
 
       Indexer indexer = new Indexer(INDEX_DIRECTORY_PATH);
       Integer maxDoc = indexer.createIndex(); // Returns total documents indexed
       System.out.println("Index Created, total documents indexed: " + maxDoc);
       indexer.close(); // Close index writer
 }

private Searcher searcher;

//public void searchIndex(String searchQuery) throws Exception {
//    searcher = new Searcher(INDEX_DIRECTORY_PATH);
//    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
//    QueryParser queryParser = new QueryParser(Version.LUCENE_47, "title", analyzer);
//    Query query = queryParser.parse(searchQuery);
//
//    TopDocs topDocs = searcher.search(query, maxDoc.SIZE);
//    ScoreDoc scoreDocs[] = topDocs.scoreDocs;
//
//    for (ScoreDoc scoreDoc : scoreDocs) {
//        Document document = searcher.getDocument(scoreDoc.doc);
//        String title = document.get("title");
//        System.out.println(title);
//    }
//}


public StringBuffer searchAndHighLightKeywords(String searchQuery) throws Exception {
	 
    // STEP A
    QueryParser queryParser = new QueryParser(Version.LUCENE_47, "title", new StandardAnalyzer(Version.LUCENE_47));
    Query query = queryParser.parse(searchQuery);
    QueryScorer queryScorer = new QueryScorer(query, "title");
    Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);

    Highlighter highlighter = new Highlighter(queryScorer); // Set the best scorer fragments
    highlighter.setTextFragmenter(fragmenter); // Set fragment to highlight

    // STEP B
    File indexFile = new File(INDEX_DIRECTORY_PATH);
    Directory directory = FSDirectory.open(indexFile);
    IndexReader indexReader = DirectoryReader.open(directory);

    // STEP C
    System.out.println("Searcher" + searcher);
    searcher = new Searcher(INDEX_DIRECTORY_PATH);
    ScoreDoc scoreDocs[] = searcher.search(query, 10).scoreDocs;
    StringBuffer buffer = new StringBuffer();
    for (ScoreDoc scoreDoc : scoreDocs) {
        Document document = searcher.getDocument(scoreDoc.doc);
        String title = document.get("title");
        TokenStream tokenStream = TokenSources.getAnyTokenStream(indexReader,
                scoreDoc.doc, "title", document, new StandardAnalyzer(Version.LUCENE_47));
        String fragment = highlighter.getBestFragment(tokenStream, title);
        System.out.println(fragment);
        buffer.append(fragment +"\n");
    }
    
    return buffer;
}






}