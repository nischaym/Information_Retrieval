package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.io.FilenameFilter;

public class IndexGenerator {

	Map<String, ArrayList<Pointer>> invertedIndex = new HashMap<>();
	Map<String, Integer> dl = new HashMap<String, Integer>();
	int total = 0;

	// start reading the files
	public IndexGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	public IndexGenerator(String filename) throws IOException{
		readFiles("cacm");
	}

	
	public File[] finder(String dirName) {
		File dir = new File(dirName);

		return dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".html");
			}
		});

	}

	public void readFiles(String path) {
		try {
			//File folder = new File(path);
			File[] listOfFiles = finder(path);
			for (File file : listOfFiles) {
				if (file.isFile()) {
					String filename = file.getName();
					filename = path + "/" + filename;
					readFile(filename);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("folder not proper " + e.printStackTrace());
		}
	}
	
	public String readFile(String filename) throws IOException{
		FileReader fileReader = new FileReader(filename);
		// Always wrap FileReader in BufferedReader.
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		StringBuffer data = new StringBuffer();
//		System.out.println();
		String line = "";
		int k = 0;
//		System.out.println(filename + "   "+ filename.substring(filename.indexOf("-") + 1, filename.indexOf(".")));
		String fileID = " " + Integer.parseInt(filename.substring(filename.indexOf("-") + 1, filename.indexOf("."))) +"";
		//System.out.println("1 2 3");
//		System.out.println(fileID);
		while ((line = bufferedReader.readLine()) != null) {
			//System.out.println(">>>   " +line);
			if (line.contains("\\<[^>]*>") || cleanString(line)
					.contains(fileID)) {
				continue;
			} else {
//				System.out.println(cleanString(line));
				data.append(" " + cleanString(line) + " ");
				
			}
		}
		generateIndex(data.toString(), getDocID(filename).replace(".html", ""));
		bufferedReader.close();
		return data.toString();
	}
	
	public String getDocID(String url) {
		return url.substring(url.lastIndexOf('/') + 1, url.length());
	}

	public String cleanString(String data) {
		data = data.replaceAll("\\<script[^>]*>[^>]*\\</script>", " ").replaceAll("\\<sup[^>]*>[^>]*\\</sup>", " ").
		// replaceAll("\n","").
		// replaceAll("\t","").
				replaceAll("\\<[^>]*>", " ").replaceAll("\\^", " ").replaceAll("\\[edit\\]", " edit ")
				// .replaceAll("((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+"
				// + "[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)", " ")
				.replaceAll("\\[[\\d]*\\]", " ").replaceAll("&nbsp;", " ")
				// .replaceAll("&amp;", "&")
				// .replaceAll("&lt;", "<").replaceAll("&gt;",
				// ">").replaceAll("([a-zA-z]*),", "$1")
				.replaceAll("([a-zA-Z]*)\\.", "$1").replaceAll("\\(", " ")
				.replaceAll("(\\s+[-|�|-|-|�|/]\\s*)|(\\s*[-|�|�]\\s+)", " ").replaceAll("(\\w);\\s", "$1 ")
				.replaceAll("(\\w):\\s", "$1 ").replaceAll("\\s*:\\s*", " ").replaceAll("\\)", " ")
				.replaceAll("[:;,\\(\\)\\[\\]\'\"\\\\!@#$%^*+=\\|`~{}?><\\/]", " ").replaceAll("\\\"|'", " ")
				.replaceAll("\\s+", " ").toLowerCase();

		return data;
	}

	public void generateIndex(String str, String docID) {
		ArrayList<Pointer> listOfPointer;
		Map<String, Pointer> localIndex = new HashMap<>();
		Pointer pointer;
		String[] tokens = str.split(" ");
		dl.put(docID, tokens.length);
		total += tokens.length;
		for (String token : tokens) {
			if (localIndex.containsKey(token)) {
				pointer = localIndex.get(token);
				localIndex.put(token, new Pointer(pointer.getDocId(), pointer.getTermFrequency() + 1));
			} else {
				pointer = new Pointer(docID, 1);
				localIndex.put(token, pointer);
			}
		}
		for (String key : localIndex.keySet()) {
			if (invertedIndex.containsKey(key)) {
				listOfPointer = invertedIndex.get(key);
				listOfPointer.add(localIndex.get(key));
				invertedIndex.put(key, listOfPointer);
			} else {
				listOfPointer = new ArrayList<>();
				listOfPointer.add(localIndex.get(key));
				invertedIndex.put(key, listOfPointer);
			}
		}
	}

	public void sortPrint(Map<String, Double> Map, String filename, String dir) throws IOException {
		List<Entry<String, Double>> sortedList = new ArrayList<Entry<String, Double>>(Map.entrySet());

		Collections.sort(sortedList, new Comparator<Entry<String, Double>>() {

			@Override
			public int compare(Entry<String, Double> entry1, Entry<String, Double> entry2) {
				return entry2.getValue().compareTo(entry1.getValue());
			}
		});

		printSortedListR(sortedList, filename, dir);
	}

	public void printSortedListR(List<Entry<String, Double>> sortedList, String filename, String dir)
			throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter("./" + dir + "/" + filename + ".txt"));
		int rank = 1;
		int sum = 0;
		for (int i = 0; i < sortedList.size(); i++) {
			Entry<String, Double> entry = sortedList.get(i);
			// System.out.println(entry.getValue());
			out.write(filename.replace("q", "") + " Q0 " + entry.getKey() + " " + rank + " " + entry.getValue() + " "
					+ dir + "\n");

			rank++;
			if (rank == 101)
				break;
			sum += entry.getValue();
		}
		out.flush();
		out.close();
	}

	public void sortMapValue(Map<String, ArrayList<Pointer>> map, String filename) throws IOException {
		List<Entry<String, Integer>> sortedList = new ArrayList<Entry<String, Integer>>(convertMap(map).entrySet());

		Collections.sort(sortedList, new Comparator<Entry<String, Integer>>() {

			@Override
			public int compare(Entry<String, Integer> entry1, Entry<String, Integer> entry2) {
				return entry2.getValue().compareTo(entry1.getValue());
			}
		});

		printSortedList(sortedList, filename);
	}

	public void printSortedList(List<Entry<String, Integer>> sortedList, String filename) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(filename + ".txt"));
		int rank = 1;
		int sum = 0;
		for (int i = 0; i < sortedList.size(); i++) {
			Entry<String, Integer> entry = sortedList.get(i);
			if (entry.getKey().startsWith(" ") || entry.getKey().equals(""))
				continue;
			out.write(rank + " " + entry.getKey() + " " + entry.getValue() + "\n");

			rank++;
			sum += entry.getValue();
		}
		System.out.println(filename + " total " + sum);
		// if (filename.equals("oneGramValueSorted")) {
		// generateStopList(sortedList);
		// }
		out.flush();
		out.close();
	}

	public HashMap<String, Integer> convertMap(Map<String, ArrayList<Pointer>> map) {
		HashMap<String, Integer> newMap = new LinkedHashMap<String, Integer>();
		for (String key : map.keySet()) {
			newMap.put(key, Integer.valueOf(calcTf(map.get(key))));
		}
		return newMap;
	}

	public int calcTf(ArrayList<Pointer> documentFrequencyList) {
		int sum = 0;
		for (Pointer documentFrequency : documentFrequencyList) {
			sum += documentFrequency.getTermFrequency();
		}
		return sum;
	}

	// RETURNS ARRAYLIST OF QUERIES
	public List<String> processQuery() throws IOException {
		List<String> queries = new ArrayList<String>();

		// try {
		File file = new File("input/cacm.query");
		FileReader fileReader = new FileReader(file);
		// Always wrap FileReader in BufferedReader.
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		StringBuffer data = new StringBuffer();
		System.out.println();
		String line = "";
		while ((line = bufferedReader.readLine()) != null) {
			if (line.startsWith("<DOC>")) {
				data = new StringBuffer();
				continue;
			} else if (line.startsWith("<DOCNO>") || line.length() == 0) {
				continue;
			} else if (line.startsWith("</DOC")) {
				queries.add(data.toString());
			} else {
				data.append(cleanString(line));
				data.append(" ");
			}

		}
		bufferedReader.close();
		// } catch (Exception e) {
		// System.out.println("folder not proper");
		// }

		return queries;
	}

	public static void main(String[] args) throws IOException {
		IndexGenerator ig = new IndexGenerator();

		// DEMO FOR QUERIES
		List<String> queries = ig.processQuery();

		for (String q : queries) {
			System.out.println(q);
			System.out.println();
		}

		// ig.readFiles("./cacm");
		// ig.sortMapValue(ig.invertedIndex, "Map");
	}
}