package project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BM25 {
	Map<String, ArrayList<Pointer>> invertedIndex = new HashMap<>();
//	List<String> queryList = new ArrayList<>();
	IndexGenerator indexGenerator;

	public BM25() throws IOException {
		indexGenerator = new IndexGenerator("Str");
		this.invertedIndex = indexGenerator.invertedIndex;
	}

	public void getQuery(List<String> queryList, String dir) throws IOException {
		int cnt = 0;
		for (String data : queryList) {
			String query = indexGenerator.cleanString(data);
			cnt++;
			String filename = "q" + cnt;
			LinkedHashMap<String, Integer> qf = new LinkedHashMap<String, Integer>();
			for (String key : query.split(" ")) {
				if (qf.containsKey(key))
					qf.put(key, qf.get(key) + 1);
				else
					qf.put(key, 1);
			}
			calcBM25(qf, filename, dir);
		}
	}
	
//	public void processQuery(String query ,int id) throws Exception
//	{
//		query = indexGenerator.cleanString(query);
//		
//		String filename = "q" + id;
//		LinkedHashMap<String, Integer> qf = new LinkedHashMap<String, Integer>();
//		for (String key : query.split(" ")) {
//			if (qf.containsKey(key))
//				qf.put(key, qf.get(key) + 1);
//			else
//				qf.put(key, 1);
//		}
//		calcBM25(qf, filename);
//		
//		
//		
//		
//	}

	public void calcBM25(HashMap<String, Integer> qf, String filename, String dir) throws IOException {
		double k1 = 1.2, b = 0.75, k2 = 100, bm25 = 0, K = 0, n = 0, N = 3204;
		HashMap<String, Double> bm25Map = new HashMap<String, Double>();
		double avdl = indexGenerator.total / 3204;
		 System.out.println(avdl + " avdl");
		for (String key : qf.keySet()) {

			// System.out.println(key);
			if (invertedIndex.get(key) != null) {
				n = invertedIndex.get(key).size();
				for (Pointer pointer : invertedIndex.get(key)) {
					K = k1 * ((1 - b) + b * indexGenerator.dl.get(pointer.getDocId()) / avdl);
//					 System.out.println(avdl + " avdl");
					// System.out.println(pointer.getTermFrequency() + "
					// pointer");
					// System.out.println(qf.get(key)+ " qf");
					bm25 = Math.log(1 / ((n + 0.5) / (N - n + 0.5)))
							* ((k1 + 1) * pointer.getTermFrequency() / (K + pointer.getTermFrequency()))
							* ((k2 + 1) * qf.get(key) / (k2 + qf.get(key)));
//					 System.out.println(bm25 + " bm25");
//					 System.out.println(bm25Map.get(pointer.getDocId()) + bm25);
					if (bm25Map.containsKey(pointer.getDocId()))
						bm25Map.put(pointer.getDocId(), bm25Map.get(pointer.getDocId()) + bm25);
					else
						bm25Map.put(pointer.getDocId(), bm25);
//					System.out.println(bm25Map.get(pointer.getDocId()));
				}
			} else {
//				System.out.println("key " + key);
			}
		}
		indexGenerator.sortPrint(bm25Map, filename, dir);

	}

	public static void main(String[] args) throws IOException {

		BM25 bm25 = new BM25();

		List<String> queryList = bm25.indexGenerator.processQuery();
		bm25.getQuery(queryList, "BM25");
	}
}
