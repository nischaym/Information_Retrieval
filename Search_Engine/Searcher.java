package project;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Searcher { 
	 
	 private IndexSearcher indexSearcher;
	  
	 public Searcher(String indexerDirectoryPath) throws Exception {
	   
	        File indexFile = new File(indexerDirectoryPath);
	        Directory directory = FSDirectory.open(indexFile);
	        IndexReader indexReader = DirectoryReader.open(directory);
	        indexSearcher = new IndexSearcher(indexReader);
	 } 
	 
	 public TopDocs search(Query query, int n) throws Exception {
	        return indexSearcher.search(query, n);
	 }
	 
	 public Document getDocument(int docID) throws Exception {
	        return indexSearcher.doc(docID); // Returns a document at the nth ID
	 }
}
