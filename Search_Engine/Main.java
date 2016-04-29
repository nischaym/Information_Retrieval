package project;

import project.IndexGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class Main {
	
	public static void main(String[] args) throws Exception {
        String searchQuery = "java action";
        
        IndexGenerator generator = new IndexGenerator();
        List<String> queries = generator.processQuery();
        int count = 1;
        LuceneHighlighter luceneHighlighter = new LuceneHighlighter();
        luceneHighlighter.createIndex();
        for(String query: queries)
        {
              //luceneHighlighter.searchIndex(searchQuery); // without highlight functionality
              StringBuffer newLine = luceneHighlighter.searchAndHighLightKeywords(query);
              BufferedWriter out = new BufferedWriter(new FileWriter("snippetoutput"+"/q"+count+++".txt"));
              out.write(newLine.toString());
			  out.close();
        	
        }
        
        
        
      
    }

}
