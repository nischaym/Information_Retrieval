package project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class Thesaurus
{

//	System.setProperty("wordnet.database.dir", "C:\WordNet-3.0\dict\");
	WordNetDatabase database = WordNetDatabase.getFileInstance();
	
	public static void main(String[] args) throws Exception
	{
		Thesaurus the = new Thesaurus();
		the.createThearsus();
	}
	
	public void createThearsus() throws Exception
	{
		IndexGenerator generator = new IndexGenerator();
		List<String> queries =  generator.processQuery();
		
		List<String> result = new ArrayList<String>();
		for(String query : queries)
		{
			StopWords word = new StopWords();
			String[] words = word.removeStopWords(query.split(" "));
			String expanded = "";
			for(int i=0; i< words.length; i++)
			{
				Thesaurus thesaurus = new Thesaurus();
				
				expanded  = thesaurus.getMeaning(words[i]);
				
				System.out.println("WORD    " + words[i] + "SAME WRODS" + thesaurus.getMeaning(words[i]));
				
			}
			
			String actualquery = "";
			for(int i = 0; i < words.length; i++)
			{
				actualquery += words[i] + " ";
				
			}
			
			result.add(actualquery + " " + expanded);
			
		}
		
		generator.readFiles("cacm");
		BM25 bm25 = new BM25();
		bm25.getQuery(result, "theasursBM25");
	}
	
	public String getMeaning(String word)
	{
		String path = "/Users/derylrodrigues/MAD/IR/WordNet-3.0/dict/";
		System.setProperty("wordnet.database.dir", path);
		
		//  Get the synsets containing the wrod form
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		Synset[] synsets = database.getSynsets(word);
		HashSet<String> keyword = new HashSet<String>();
		if (synsets.length > 0)
		{
			//System.out.println("The following synsets contain '" + word + "' or a possible base form " + "of that text:");
			for (int i = 0; i < synsets.length && keyword.size() < 3; i++)
			{
				//System.out.println("");
				String[] wordForms = synsets[i].getWordForms();
				for (int j = 0; j < wordForms.length && keyword.size() < 3; j++)
				{
					//System.out.println(j + "  " + wordForms[j]);
					keyword.add(wordForms[j]);
				}
			}
		}
		else
		{
			//System.err.println("No synsets exist that contain " + "the word form '" + word + "'");
			
		}
		
		//System.out.println("Size of hashSet" + keyword.size());
		
		
		String expandedWord = "";
		Iterator<String> hashIterator = keyword.iterator();
		
		while(hashIterator.hasNext())
		{
			expandedWord += hashIterator.next() +" ";
		}
		
		
		return expandedWord;
	
	}

}

	
	