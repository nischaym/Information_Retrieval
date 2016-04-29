package assignment4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import assignment3.DocumentFrequency;
import assignment3.Index;

public class BM25 {

	static Map<String, ArrayList<Index>> unigramInvertedIndex = new HashMap<>();
	static Map<String, Integer> unigramtf = new HashMap<>();
	static Map<String, DocumentFrequency> unigramdf = new HashMap<>();

	static Map<String, Integer> dl_map = new HashMap<>();
	static Double sumOfAllTokens = 0.0;

	final static Double B = 0.75;
	final static Double K1 = 1.2;
	final static Double K2 = 100.0;

	public static void main(String[] args) throws IOException {

		File folder = new File("./Links");
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile()) {

				String filename = file.getName();
				filename = "./Links/" + filename;
				FileReader fileReader = new FileReader(filename);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				String AllWordsInFile = "";
				Map<String, Index> uniLocalMap = new HashMap<>();

				System.out.println(file.getName());

				while ((AllWordsInFile = bufferedReader.readLine()) != null) {
					String[] AllWordsInFileArray = AllWordsInFile.split(" ");

					// to Calculate AvDL
					sumOfAllTokens = sumOfAllTokens + AllWordsInFileArray.length;
					dl_map.put(file.getName(), AllWordsInFileArray.length);

					for (int i = 0; i < AllWordsInFileArray.length; i++) {
						String currString = AllWordsInFileArray[i].trim();
						if (!" ".equals(currString) && !"-".equals(currString) && !"_".equals(currString)
								&& !"$".equals(currString) && !"".equals(currString) && !"&".equals(currString)) {

							if (uniLocalMap.get(currString) != null) {
								Index in = uniLocalMap.get(currString);
								in.frequency += 1;

								uniLocalMap.put(currString, in);
							} else {
								// int j = 1;
								String id = file.getName();
								id = id.replace("_", "");
								Index in = new Index(id, 1);
								uniLocalMap.put(currString, in);
							}
						}

					}

				}

				// to put into global invertedIndex for each file
				for (Entry<String, Index> b : uniLocalMap.entrySet()) {
					String lkey = b.getKey();

					if (unigramInvertedIndex.get(lkey) != null) {
						ArrayList<Index> lindex = unigramInvertedIndex.get(lkey);
						lindex.add(b.getValue());
						unigramInvertedIndex.put(lkey, lindex);
					} else {
						ArrayList<Index> lindex = new ArrayList<Index>();
						lindex.add(b.getValue());
						unigramInvertedIndex.put(lkey, lindex);
					}
				}
				bufferedReader.close();
			}
		} // end of all files

		Double AvDL = sumOfAllTokens / 1000.0;

		FileReader fileReader = new FileReader("queries.txt");
		// Always wrap FileReader in BufferedReader.
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line = "";
		int index =1;
		while ((line = bufferedReader.readLine()) != null) {
			String[] qw = line.split(" ");
			System.out.println(line);
			Map<String, ArrayList<Double>> bm25 = new LinkedHashMap<>();
			for (int i = 0; i < qw.length; i++) {
				System.out.println(qw[i]);
				// calculate BM25 score for all the files in which the query
				// terms are present
				ArrayList<Index> index_list = unigramInvertedIndex.get(qw[i]);

				for (int j = 0; j < index_list.size(); j++) {

					int dl = dl_map.get(index_list.get(j).docId);
					System.err.println(index_list.get(j).docId);
					int fi = index_list.get(j).frequency;

					Double K = K1 * ((1 - B) + (B * (dl / AvDL)));

					int Ni = index_list.size();
					Double BM = Math.log(1 / ((Ni + 0.5) / (1000 - Ni + 0.5))) * (((K1 + 1) * fi) / (K + fi))
							* ((K2 + 1) / (K2 + 1));

					System.out.println(BM);
					if (bm25.get(index_list.get(j).docId) != null) {
						ArrayList<Double> b = bm25.get(index_list.get(j).docId);
						b.add(BM);

						bm25.put(index_list.get(j).docId, b);
					} else {
						ArrayList<Double> b = new ArrayList<>();
						b.add(BM);
						bm25.put(index_list.get(j).docId, b);
					}

				}
			}

			String filename = "BM_" + line + ".txt";
			PrintWriter writerBM = new PrintWriter(filename, "UTF-8");
			Map<String, Double> bm25_print = new LinkedHashMap<>();

			for (Entry<String, ArrayList<Double>> c : bm25.entrySet()) {
				ArrayList<Double> d = c.getValue();
				Double bm = 0.0;
				for (int i = 0; i < d.size(); i++) {
					bm = bm + d.get(i);
				}

				bm25_print.put(c.getKey(), bm);
			}
			// SORT the MAP based on BM Score

			List<Entry<String, Double>> tmp = new ArrayList<Entry<String, Double>>(bm25_print.entrySet());

			Collections.sort(tmp, new Comparator<Entry<String, Double>>() {

				@Override
				public int compare(Entry<String, Double> entry1, Entry<String, Double> entry2) {
					return entry2.getValue().compareTo(entry1.getValue());
				}
			});
			for (int n=0;n<100;n++) {
				Entry<String, Double> c = tmp.get(n);
				writerBM.println("Q"+index+" "+ c.getKey() + " "+ (n+1) +" "+ c.getValue()+ " BM25" );
			}

			writerBM.close();
			index++;

		}
		bufferedReader.close();

	}

}
