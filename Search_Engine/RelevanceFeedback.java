package project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RelevanceFeedback {

	Map<String, Double> relevantTerms = new HashMap<>();
	Map<String, Integer> partition = new HashMap<>();

	public List<String> get10Documents(String fileName) throws IOException {

		FileReader fileReader = new FileReader(fileName);

		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line;
		List<String> top10 = new ArrayList<String>();
		String documentName = null;
		int cnt = 1;
		while ((line = bufferedReader.readLine()) != null) {
			documentName = line.split(" ")[2];
			readFile("cacm/" + documentName + ".html");
			if (cnt == 10)
				break;
			top10.add(documentName);
			cnt++;
		}

		return top10;
	}

	// READING FROM FILE

	public void readFile(String filename) throws IOException {
		FileReader fileReader = new FileReader(filename);
		IndexGenerator indexGenerator = new IndexGenerator("Nothing");
		// Always wrap FileReader in BufferedReader.
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		StringBuffer data = new StringBuffer();
		// System.out.println();
		String line = "";
		int k = 0;
		// System.out.println(filename + " "+
		// filename.substring(filename.indexOf("-") + 1,
		// filename.indexOf(".")));
		String fileID = " " + Integer.parseInt(filename.substring(filename.indexOf("-") + 1, filename.indexOf(".")))
				+ "";
		// System.out.println("1 2 3");
		// System.out.println(fileID);
		while ((line = bufferedReader.readLine()) != null) {
			// System.out.println(">>> " +line);
			if (line.contains("\\<[^>]*>") || indexGenerator.cleanString(line).contains(fileID)) {
				continue;
			} else {
				// System.out.println(cleanString(line));
				data.append(" " + indexGenerator.cleanString(line) + " ");

			}
		}
		widthPartition(data.toString(), indexGenerator.getDocID(filename).replace(".html", ""));
		bufferedReader.close();
	}

	// Make the width partition

	public void widthPartition(String str, String docID) throws IOException {
		Map<String, ArrayList<Pointer>> invertedIndex = new HashMap<>();
		StopWords stop = new StopWords();
		String[] tokens = str.split(" ");
		tokens = stop.removeStopWords(tokens);
		int width = tokens.length / 10;
		int id = 0;
		for (int i = 0; i < tokens.length;) {
			int start = i;
			int end = i + width;
			invertedIndex = generatePartition(tokens, start, end, id, invertedIndex);
			i = end + 1;
		}

		frequencyPartition(tokens, getFScoreMax(invertedIndex));

	}

	// make the frequency partition

	public void frequencyPartition(String[] tokens, int fScoreMax) {
		int k = tokens.length / fScoreMax;
		int id = 0;
		Map<String, ArrayList<Pointer>> invertedIndex = new HashMap<>();
		for (String key : partition.keySet()) {
			partition.put(key, 0);
		}
		for (int i = 0; i < tokens.length;) {
			int start = i;
			int end = i + k;
			invertedIndex = generatePartition(tokens, start, end, id, invertedIndex);
			i = end + 1;
		}
		calculateScore(invertedIndex, tokens.length / k);
	}

	public void calculateScore(Map<String, ArrayList<Pointer>> invertedIndex, int P) {
		for (String key : invertedIndex.keySet()) {
			double max = Double.MIN_VALUE;
			for (Pointer part : invertedIndex.get(key)) {
				double score = 0;
				score = (part.getTermFrequency() / partition.get(part.getDocId()))
						* (Math.log(P / invertedIndex.get(key).size()));
				if (score > max) {
					max = score;
				}
			}
			if (relevantTerms.containsKey(key)) {
				if (max > relevantTerms.get(key))
					relevantTerms.put(key, max);
			} else
				relevantTerms.put(key, max);
		}
	}

	// calculate the fScoreMax

	public int getFScoreMax(Map<String, ArrayList<Pointer>> invertedIndex) {
		int fScoremax = 0;
		ArrayList<Pointer> listOfPartitions;
		for (String key : invertedIndex.keySet()) {
			listOfPartitions = invertedIndex.get(key);
			int frequency = 0;
			for (Pointer p : listOfPartitions) {
				frequency += p.getTermFrequency();
			}
			if (frequency > fScoremax)
				fScoremax = frequency;
		}
		return fScoremax;
	}

	// Map the given partition to the invertedIndex

	public Map<String, ArrayList<Pointer>> generatePartition(String[] tokens, int start, int end, int id,
			Map<String, ArrayList<Pointer>> invertedIndex) {
		ArrayList<Pointer> listOfPartitions;

		Map<String, Pointer> localIndex = new HashMap<>();
		Pointer pointer;
		// String[] tokens = str.split(" ");
		// dl.put(docID, tokens.length);
		// total += tokens.length;
		if (end >= tokens.length)
			end = tokens.length - 1;
		int max = 0;
		for (int i = start; i < end + 1; i++) {
			if (localIndex.containsKey(tokens[i])) {
				pointer = localIndex.get(tokens[i]);
				localIndex.put(tokens[i], new Pointer(pointer.getDocId(), pointer.getTermFrequency() + 1));
			} else {
				pointer = new Pointer("p" + id, 1);
				localIndex.put(tokens[i], pointer);
			}
		}
		for (String key : localIndex.keySet()) {
			int freq = localIndex.get(key).getTermFrequency();
			if (freq > max) {
				max = freq;
			}
		}
		partition.put("p" + id, max);

		for (String key : localIndex.keySet()) {
			if (invertedIndex.containsKey(key)) {
				listOfPartitions = invertedIndex.get(key);
				listOfPartitions.add(localIndex.get(key));
				invertedIndex.put(key, listOfPartitions);
			} else {
				listOfPartitions = new ArrayList<>();
				listOfPartitions.add(localIndex.get(key));
				invertedIndex.put(key, listOfPartitions);
			}
		}
		return invertedIndex;
	}

	public List<String> getTopRelevantTerms(List<String> query, String fileName) throws IOException{
		get10Documents(fileName);
		System.out.println(fileName);
		List<Entry<String, Double>> sortedList = new ArrayList<Entry<String, Double>>(relevantTerms.entrySet());

		Collections.sort(sortedList, new Comparator<Entry<String, Double>>() {

			@Override
			public int compare(Entry<String, Double> entry1, Entry<String, Double> entry2) {
				return entry2.getValue().compareTo(entry1.getValue());
			}
		});
		List<String> terms = new ArrayList<String>();
		int cnt = 1;
		for (int i = 0; i < sortedList.size(); i++) {
			Entry<String, Double> entry = sortedList.get(i);
			if (!query.contains(entry.getKey())) {
				terms.add(entry.getKey());
				cnt++;
			}
			if (cnt == 6)
				break;
		}
		return terms;
	}

	public static void main(String[] args) throws IOException {
		RelevanceFeedback queryExpansion = new RelevanceFeedback();
		String fileName = "BM25/q1.txt";
		List<String> terms = new ArrayList<>();
		List<String> query = new ArrayList<>();
		query.add("system");
		query.add("3");
		
		terms = queryExpansion.getTopRelevantTerms(query, fileName);
		for (String s : terms) {
			System.out.println(s);
		}
	}
}
