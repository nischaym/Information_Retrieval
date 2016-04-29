package project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StopWords {
	List<String> stopWordList = new ArrayList<>();

	public StopWords() throws IOException{

		// TODO Auto-generated constructor stub

		FileReader fileReader = new FileReader("input/common_words");
		String line = "";
		// Always wrap FileReader in BufferedReader.
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		while ((line = bufferedReader.readLine()) != null) {
			stopWordList.add(line.replace("\n", ""));
		}
	}

	public String[] removeStopWords(String[] tokens) {
		List<String> newTokens = new ArrayList<>();
		for (String s : tokens) {
			if (!stopWordList.contains(s)){
				newTokens.add(s);
			}
		}
		String[] arrayTokens = new String[newTokens.size()];
		arrayTokens = newTokens.toArray(arrayTokens);
		return arrayTokens;
	}
	
	public static void main(String[] args) throws IOException{
		String[] str = {"a","also","deriyl"};
		StopWords stop = new StopWords();
		str = stop.removeStopWords(str);
		for (String s : str)
			System.out.println(s);
	}
}
