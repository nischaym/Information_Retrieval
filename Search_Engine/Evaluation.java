package project;

import java.util.*;
import java.io.*;

public class Evaluation {

	public static void main(String[] args) throws Exception {
		Evaluation e = new Evaluation();

		HashMap<String, List<String>> revelanceMap = e.buildRevelance();
		e.readOutputFile(revelanceMap, "BM25StopExpansion", "BM25StopExpansionEval");

	}

	public HashMap<String, List<String>> buildRevelance() throws Exception {
		FileReader reader = new FileReader("input/cacm.rel");
		BufferedReader br = new BufferedReader(reader);

		String line = null;
		HashMap<String, List<String>> revelanceMap = new HashMap<String, List<String>>();
		while ((line = br.readLine()) != null) {
			String key = line.split(" ")[0];
			String filename = line.split(" ")[2];

			if (revelanceMap.containsKey(key)) {
				revelanceMap.get(key).add(filename);
			} else {
				List<String> files = new ArrayList<String>();
				files.add(filename);
				revelanceMap.put(key, files);
			}

		}

		return revelanceMap;

	}

	public File[] finder(String dirName) {
		File dir = new File(dirName);

		return dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".txt");
			}
		});

	}

	public void readOutputFile(HashMap<String, List<String>> revelanceMap, String source, String dest)
			throws Exception {
		// Iterator it = revelanceMap.entrySet().iterator();
		// while (it.hasNext()) {
		// Map.Entry pair = (Map.Entry) it.next();
		// System.out.println(pair.getKey() + " = " + ((ArrayList)
		// pair.getValue()).size());
		// }
		double mrr = 0d;
		double map = 0d;
		for (int i = 1; i < 65; i++) {

			FileReader reader = new FileReader(source + "/q" + i + ".txt");
			BufferedReader br = new BufferedReader(reader);
			if (!((i == 34) || (i == 35) || (i == 46) || (i == 47) || (i == 41) || (i < 57 && i > 49))) {
				String line = null;
				double rr = Double.MAX_VALUE;
				double ap = 0d;
				// System.out.println("I " + i);
				List<String> revelantFiles = revelanceMap.get(i + "");
				StringBuffer newLine = new StringBuffer();
				double revelanceUntilNow = 0d;
				double count = 1d;
				while ((line = br.readLine()) != null) {

					String docId = line.split(" ")[2];
					line = line.replace("\n", "");
					int revelantScore = 0;
					// System.out.println("DOC" + docId);

					if (revelantFiles.contains(docId)) {
						if (count < rr)
							rr = count;
						revelantScore = 1;
						revelanceUntilNow++;
						ap += (revelanceUntilNow / count);
						
						
					}
					if (count == 5)
						System.out.println(((revelanceUntilNow / count)));
					newLine.append(line + " " + revelantScore + " " + (revelanceUntilNow / count) + " "
							+ (revelanceUntilNow / revelantFiles.size() + "\n"));
					count++;
				}
				if (rr == Double.MAX_VALUE)
					rr = 0;
				else
					rr = 1/rr;
				mrr += rr;
				if (revelanceUntilNow != 0d)
				map += (ap/revelanceUntilNow);
				// print a new file
				br.close();
				// @SuppressWarnings("resource")
				BufferedWriter out = new BufferedWriter(new FileWriter(dest + "/q" + i + ".txt"));
				out.write(newLine.toString());
				out.close();
			}
		}
		System.out.println("MRR = " + (mrr / 53d));
		System.out.println("MAP =  " + (map / 53d)+"  map" + map);
	}

}
