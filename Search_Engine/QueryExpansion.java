package project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryExpansion {
	
	public static void main(String[] args) throws IOException {
		QueryExpansion queryExpansion = new QueryExpansion();
		queryExpansion.expansion();
		
	}
	
	public void expansion()throws IOException{
		RelevanceFeedback relevanceFeedback = new RelevanceFeedback();
		IndexGenerator indexGenerator = new IndexGenerator();
		TfIdf tfidf = new TfIdf();
		List<String> queryList = indexGenerator.processQuery();
		System.out.println();
		for (int i = 1 ; i < queryList.size() +1; i++){
			String q = queryList.get(i-1);
			List<String> terms = new ArrayList<>();
			terms = relevanceFeedback.getTopRelevantTerms(Arrays.asList(q.split(" ")), "tfidf/q"+ i + ".txt");
			for (String s : terms)
				q += s + " ";
			queryList.set(i-1, q);
		}
		tfidf.getQuery(queryList, "RelevanceExpansion");
	}
}
