package project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Task3 {
	
	static IndexGenerator ig = new IndexGenerator();
	static Map<String,String> docs = new LinkedHashMap<>();
	static Map<String, ArrayList<Pointer>> invertedIndex = new HashMap<>();
	static Integer total =0;
	static Map<String,Integer> dl = new LinkedHashMap<>();
	public static void main(String[] args) throws IOException 
	{

		String filename="input/cacm_stem.txt";
		FileReader fileReader = new FileReader(filename);
		// Always wrap FileReader in BufferedReader.
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		String line = "";
		String docId = "";
		
		StringBuffer docData = new StringBuffer();
		int k =1;
		bufferedReader.readLine();
		while((line = bufferedReader.readLine()) != null)
		{
			if(line.startsWith("#"))
			{
				docId = "CACM-"+k;
				docs.put(docId, docData.toString());
				k++;
				docData.delete(0,docData.length());
			}
			else
			{
				docData.append(line+" ");
			}

			
		}
		bufferedReader.close();
		
		
		
		
		// generate all the Inverted Index
		
		
		
		for(Entry<String, String> e : docs.entrySet())
		{
			System.out.println(e.getKey()+":;;;;;;"+ e.getValue());
			generateIndex(e.getValue().toString(),e.getKey().toString());
			
		}
		
		PrintWriter writer = new PrintWriter("F.txt","UTF-8");
		for(Entry<String, ArrayList<Pointer>> e : invertedIndex.entrySet())
		{
			System.out.println("nik");
			ArrayList<Pointer> p = e.getValue();
			writer.print(e.getKey()+":;;;;;;");
			for(Pointer p1 : p)
			{
				writer.print(p1.getDocId()+";;"+p1.getTermFrequency()+" ");
			}
			writer.println("");
		}
		writer.close();
		
		// actual calls
		readQueryMap();
		
		
	}
	
	public static void generateIndex(String str, String docID) {
		ArrayList<Pointer> listOfPointer;
		Map<String, Pointer> localIndex = new HashMap<>();
		Pointer pointer;
		String[] tokens = str.split(" ");

		for (String token : tokens) {
			token = token.trim();
			if(!token.equals(" ") && token.length() != 1)
			{
				if (localIndex.containsKey(token)) {
					pointer = localIndex.get(token);
					localIndex.put(token, new Pointer(pointer.getDocId(), pointer.getTermFrequency() + 1));
				} else {
					pointer = new Pointer(docID, 1);
					localIndex.put(token, pointer);
				}
			}

		}
		total += localIndex.size();
		dl.put(docID,localIndex.size());
		
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
	
	private static void readQueryMap() throws IOException 
	{
		
		String filename="input/cacm_stem.query.txt";
		FileReader qfileReader = new FileReader(filename);
		// Always wrap FileReader in BufferedReader.
		BufferedReader qbufferedReader = new BufferedReader(qfileReader);
		
		String line = "";
		while ((line = qbufferedReader.readLine()) != null) 
		{
			HashMap<String,Integer> qMap = new HashMap<>();
			String[] q = line.split(" ");
			
			for(int i=0;i<q.length;i++)
			{
				qMap.put(q[i], 1);
			}
			calcBM25(qMap,line.toString());
		}
		qbufferedReader.close();
	}



	public static void calcBM25(HashMap<String, Integer> qf, String filename) throws IOException {
		double k1 = 1.2, b = 0.75, k2 = 100, bm25 = 0, K = 0, n = 0, N = 3204;
		HashMap<String, Double> bm25Map = new HashMap<String, Double>();
		double avdl = total / 3204;
		 System.out.println(avdl + " avdl");
		for (String key : qf.keySet()) {

			// System.out.println(key);
			if (invertedIndex.get(key) != null) {
				n = invertedIndex.get(key).size();
				for (Pointer pointer : invertedIndex.get(key)) {
					K = k1 * ((1 - b) + b * dl.get(pointer.getDocId()) / avdl);
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
		IndexGenerator ig = new IndexGenerator();
		ig.sortPrint(bm25Map, filename, "StemBM25");

	}


//
//	private static Map<String,Integer> generateIndex(String str) 
//	{
//		Map<String,Integer> InvertedIndex = new HashMap<>();
//		String[] tokens = str.split(" ");
//		for (String token : tokens) 
//		{
//			if (InvertedIndex.containsKey(token)) 
//			{
//				Integer i  = InvertedIndex.get(token);
//				i ++;
//				InvertedIndex.put(token, i);
//			} 
//			else 
//			{
//				InvertedIndex.put(token, 1);
//			}
//		}
//		return InvertedIndex;
//	}
	
}
