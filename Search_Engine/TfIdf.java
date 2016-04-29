package project;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TfIdf {
	Map<String, ArrayList<Pointer>> invertedIndex = new HashMap<>();
	// List<String> queryList = new ArrayList<String>();
	IndexGenerator indexGenerator;

	public TfIdf() throws IOException{
		// TODO Auto-generated constructor stub
		indexGenerator = new IndexGenerator("Str");
		this.invertedIndex = indexGenerator.invertedIndex;
	}

	public void getQuery(List<String> queryList, String dir) throws IOException {
		int cnt = 0;
		for (String data : queryList) {
			String query = indexGenerator.cleanString(data);
			cnt++;
			String filename = "q" + cnt;
			calcTfiDF(query, filename, dir);
		}
	}

	public void calcTfiDF(String query, String filename, String dir) throws IOException {
		Map<String, Double> tfIDFMap = new HashMap<String, Double>();
		ArrayList<Pointer> listOfPointer;
		double n = 3204, idf;

		for (String term : query.split(" ")) {
			//System.out.println("--------"+term);
			if (invertedIndex.get(term) != null) {
				listOfPointer = invertedIndex.get(term);
				idf = n / listOfPointer.size();
				for (Pointer pointer : listOfPointer) {
					double tfidf = 0;
					tfidf = Math.log(pointer.getTermFrequency() * idf);
					if (tfIDFMap.containsKey(pointer.getDocId())) {
//						System.out.println(tfIDFMap.get(pointer.getDocId()));
						tfIDFMap.put(pointer.getDocId(), tfIDFMap.get(pointer.getDocId()) + tfidf);
					} else
						tfIDFMap.put(pointer.getDocId(), tfidf);
				}
			} else {
				System.out.println("key " + term);
			}

		}
		indexGenerator.sortPrint(tfIDFMap, filename, dir);
	}

	public static void main(String[] args) throws IOException {

		TfIdf tfIdf = new TfIdf();
		String dir = "tfidf";
		List<String> queryList = tfIdf.indexGenerator.processQuery();
		tfIdf.getQuery(queryList, dir);
	}
}
