package project;



import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import project.IndexGenerator;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Indexer {
	private IndexWriter indexWriter;
	 
	 public Indexer(String indexerDirectoryPath) throws Exception { 
	        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
	        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_47, analyzer);
	        indexWriterConfig.setOpenMode(OpenMode.CREATE);
	 
	        File indexFile = new File(indexerDirectoryPath);
	        Directory directory = FSDirectory.open(indexFile);
	        indexWriter = new IndexWriter(directory, indexWriterConfig);
	 }
	 
	 
	 
	 public File[] finder(String dirName) {
			File dir = new File(dirName);

			return dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String filename) {
					return filename.endsWith(".html");
				}
			});

		}
	 
	 public List<String> getContents() throws IOException{
		 
		 File[] files = finder("cacm");
		 List<String> contents = new ArrayList<String>();
		 for (File file : files) 
		 {
			if (file.isFile()) {
					String filename = file.getName();
					filename = "cacm" + "/" + filename;
					IndexGenerator generator = new IndexGenerator();
					String content = generator.readFile(filename);
					contents.add(content);
					System.out.println("CONTENT >>" + content);
			}
		}
		 
		return contents; 
	 }
	 
	 public int createIndex() throws Exception {  
	 
	   List<String> titles =  getContents();
	 
	     for(String title : titles) {
	         Document document = new Document();
	         document.add(new TextField("title", title, Store.YES));
	         indexWriter.addDocument(document);
	      }  
	      indexWriter.commit();
	      return indexWriter.numDocs();
	 }
	 
	 public void close() throws Exception {
	               indexWriter.close();
	        }

}
