package project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StopExpansion {
	public static void main(String[] args) throws IOException{
		StopExpansion stopExpansion = new StopExpansion();
		stopExpansion.expansion();
	}

	public void expansion() throws IOException {
		RelevanceFeedback relevanceFeedback = new RelevanceFeedback();
		StopRun stopRun = new StopRun();
		stopRun.readFiles("cacm");
		List<String> queryList = stopRun.processQuery();
		System.out.println();
		for (int i = 1; i < queryList.size() + 1; i++) {
			String q = queryList.get(i - 1);
			List<String> terms = new ArrayList<>();
			terms = relevanceFeedback.getTopRelevantTerms(Arrays.asList(q.split(" ")), "BM25Stop/q" + i + ".txt");
			for (String s : terms)
				q += s + " ";
			queryList.set(i - 1, q);
		}
		stopRun.getQuery(queryList, "BM25StopExpansion");
	}
}
