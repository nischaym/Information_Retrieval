package assignment3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
//import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class parser {
	

	static Map<String,ArrayList<Index>> unigramInvertedIndex = new HashMap<>();
	static Map<String,ArrayList<Index>> bigramInvertedIndex = new HashMap<>();
	static Map<String,ArrayList<Index>> trigramInvertedIndex = new HashMap<>();
	static Map<String,Integer> unigramtf = new HashMap<>();
	static Map<String,Integer> bigramtf = new HashMap<>();
	static Map<String,Integer> trigramtf = new HashMap<>();
	static Map<String,DocumentFrequency> unigramdf = new HashMap<>();
	static Map<String,DocumentFrequency> bigramdf = new HashMap<>();
	static Map<String,DocumentFrequency> trigramdf = new HashMap<>();
	
	public static void main(String[] args) throws IOException
	{
		//getCorpus();
		generateInvertedIndex();
                

	}
	


	public static void getCorpus() throws UnsupportedEncodingException, IOException
	{
        FileReader fileReader = new FileReader("Base_URls_Assignment3.txt");

        // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = "";
        while((line = bufferedReader.readLine()) != null) 
        {
        	String[] temp = line.substring(30, line.length()).split("/");
        	
        	
        	String filename  = "./Links/"+temp[temp.length-1]+".txt";
        	filename = filename.replace("_", "");
        	PrintWriter writer = new PrintWriter(filename, "UTF-8");
        	//PrintWriter writer = new PrintWriter(filename, "UTF-8");
        	
        	
        	//PrintWriter writer1 = new PrintWriter("sample.txt", "UTF-8");
            Document doc = Jsoup.connect(line).get();
            
            if(doc.getElementById("References") != null)
            {
            	doc.getElementById("References").remove();
            }
            if(doc.getElementById("See_also") != null )
            {
            	doc.getElementById("See_also").remove();
            }
            
            if(doc.getElementById("Related_journals") != null )
            {
            	doc.getElementById("Related_journals").remove();
            }
            
            if(doc.getElementsByClass("reference") != null)
            {
            	doc.getElementsByClass("reference").remove();
            }
            
            if(doc.getElementById("Further_reading") != null)
            {
            	doc.getElementById("Further_reading").remove();
            }
           
            if(doc.getElementById("External_links") != null)
            {
            	doc.getElementById("External_links").remove();
            }
            if(doc.getElementById("toc") != null)
            {
            	doc.getElementById("toc").remove();
            }
            
            
            Elements heading =  doc.select("h1");
            String heading1Text= Jsoup.clean(heading.toString(), new Whitelist());
            heading1Text = textClean(heading1Text);
            
            Elements heading2 = doc.select("span[class=mw-headline]");
            String heading2Text= Jsoup.clean(heading2.toString(), new Whitelist());
            heading2Text = textClean(heading2Text);
            
            /// para and Sublines
            Elements note =  doc.select("div[class=hatnote]");
            String noteText= Jsoup.clean(note.toString(), new Whitelist());
            noteText = textClean(noteText);
            
            Elements para = doc.select("p");
            String stringWithoutHtml = textClean(para.text());
            
            //System.out.println("safsdfsf");
            System.out.println(line);
            writer.print(heading1Text+ " ");
            writer.print(heading2Text+ " ");
            writer.print(noteText+" ");
            writer.println(stringWithoutHtml);
            writer.close();
        }
        bufferedReader.close();

	}

	
	public static void generateInvertedIndex() throws IOException
	{
		
		File folder = new File("./Links");
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        
		    	String filename = file.getName();
		    	filename = "./Links/"+filename;
		        FileReader fileReader = new FileReader(filename);
		        BufferedReader bufferedReader = new BufferedReader(fileReader);
		        String AllWordsInFile = "";
		        Map<String,Index> uniLocalMap = new HashMap<>();
		        Map<String,Index> biLocalMap = new HashMap<>();
		        Map<String,Index> triLocalMap = new HashMap<>();
		        
		        System.out.println(file.getName());
		        
		        while((AllWordsInFile = bufferedReader.readLine()) != null) 
		        {
		        	String[] AllWordsInFileArray = AllWordsInFile.split(" ");
		        	
		            for(int i=0;i<AllWordsInFileArray.length;i++)
		            {
		            	String currString = AllWordsInFileArray[i].trim();
		            	if( !" ".equals(currString) 
		            			&& !"-".equals(currString)
		            			&& !"_".equals(currString)
		            			&& !"$".equals(currString)
		            			&& !"".equals(currString)
		            			&& !"&".equals(currString))
		            	{
		            		
		            	
		                	if(uniLocalMap.get(currString) != null)
		                	{
		                		Index in = uniLocalMap.get(currString);
		                		in.frequency += 1;
		                		
		                		uniLocalMap.put(currString, in);
		                	}
		                	else
		                	{
		                		//int j = 1;
		                		String id = file.getName();
		                		id = id.replace("_", "");
		                		Index in = new Index(id,1);
		                		uniLocalMap.put(currString, in);
		                	}
		                }
		            	
		            }
		            
		            // bigrams
		            generateBigrams(AllWordsInFileArray,file,biLocalMap);
		            generateTrigrams(AllWordsInFileArray,file,triLocalMap);
		        }
		        
		        // to put into global invertedIndex
		        for(Entry<String,Index> b : uniLocalMap.entrySet())
		        {
		        	String lkey = b.getKey();
		    	  
		        	if(unigramInvertedIndex.get(lkey) != null)
		        	{
		        		ArrayList<Index> lindex = unigramInvertedIndex.get(lkey);
		        		lindex.add(b.getValue());
		        		unigramInvertedIndex.put(lkey, lindex);
		        	}
		        	else
		        	{	
		        		ArrayList<Index> lindex = new ArrayList<Index>();
		        		lindex.add(b.getValue());
		        		unigramInvertedIndex.put(lkey, lindex);
		        	}
		        }
		        for(Entry<String,Index> b : biLocalMap.entrySet())
		        {
		        	String lkey = b.getKey();
		    	  
		        	if(bigramInvertedIndex.get(lkey) != null)
		        	{
		        		ArrayList<Index> lindex = bigramInvertedIndex.get(lkey);
		        		lindex.add(b.getValue());
		        		bigramInvertedIndex.put(lkey, lindex);
		        	}
		        	else
		        	{	
		        		ArrayList<Index> lindex = new ArrayList<Index>();
		        		lindex.add(b.getValue());
		        		bigramInvertedIndex.put(lkey, lindex);
		        	}
		        }
		        for(Entry<String,Index> b : triLocalMap.entrySet())
		        {
		        	String lkey = b.getKey();
		    	  
		        	if(trigramInvertedIndex.get(lkey) != null)
		        	{
		        		ArrayList<Index> lindex = trigramInvertedIndex.get(lkey);
		        		lindex.add(b.getValue());
		        		trigramInvertedIndex.put(lkey, lindex);
		        	}
		        	else
		        	{	
		        		ArrayList<Index> lindex = new ArrayList<Index>();
		        		lindex.add(b.getValue());
		        		trigramInvertedIndex.put(lkey, lindex);
		        	}
		        }
		        bufferedReader.close();
		    }
		}

	    
        /* Sortin based on TF*/
		//sortOnTf();
		
		/*Sorting based on DF*/
		generateSortByKey();
		
		
	    for(Entry<String,ArrayList<Index>> a : trigramInvertedIndex.entrySet())
	    {
	    	ArrayList<Index> local = a.getValue();
	    	int df =  local.size();
	    	DocumentFrequency d = new DocumentFrequency();
	    	ArrayList<String> s = new ArrayList<String>();
	    	
	    	for(int j=0;j<local.size();j++)
	  	  	{
	  		  	Index c = local.get(j);
	  		  	s.add(c.docId);
	  	  	}
	    	d.docIds = s;
	    	d.dfrequency = df;
	    	trigramdf.put(a.getKey(),d);
	    }
        List<Entry<String,DocumentFrequency>> triTmp = new ArrayList<Entry<String, DocumentFrequency>>(trigramdf.entrySet());

        Collections.sort(triTmp,new Comparator<Entry<String,DocumentFrequency>>() 
        {

 			@Override
 			public int compare(Entry<String, DocumentFrequency> entry1, Entry<String, DocumentFrequency> entry2) 
 			{
 				return entry1.getKey().compareTo(entry2.getKey());
 			}
        });
        PrintWriter writerTriDf = new PrintWriter("Tri_Sorted_DocumentF.txt","UTF-8");
        for(int i=0;i<triTmp.size();i++)
        {
        	
        	Entry<String, DocumentFrequency> l  = triTmp.get(i);
        	if(l.getKey().startsWith(" ") || l.getKey().startsWith("."))
        	{
        		
        	}
        	else
        	{
        		writerTriDf.print(l.getKey()+ ",");
            	ArrayList<String> docs = l.getValue().docIds;
            	for(int j =0;j<docs.size();j++)
            	{
            		writerTriDf.print(docs.get(j)+ " ");
            	}
            	
            	writerTriDf.println(","+l.getValue().dfrequency);
        	}
        	
        	

        }
	    
        writerTriDf.close();
		
        

        
	    System.out.println(unigramInvertedIndex.size());
	    System.out.println(bigramInvertedIndex.size());
	    System.out.println(trigramInvertedIndex.size());
	}
	
	public static void generateBigrams(String[] AllWordsInFileArray,File file,Map<String,Index> biLocalMap)
	{
		
        for(int i=0;i<AllWordsInFileArray.length-1;i++)
        {
        	String currString = AllWordsInFileArray[i]+" ";
        	String nextString = AllWordsInFileArray[i+1];
        	String now = currString+nextString;
        	if( !" ".equals(now) 
        			&& !"-".equals(now)
        			&& !"_".equals(now)
        			&& !"$".equals(now)
        			&& !"".equals(now)
        			&& !"&".equals(now))
        	{
        		
        	
            	if(biLocalMap.get(now) != null)
            	{
            		Index in = biLocalMap.get(now);
            		in.frequency += 1;
            		
            		biLocalMap.put(now, in);
            	}
            	else
            	{
            		//int j = 1;
            		String id = file.getName();
            		id = id.replace("_", "");
            		Index in = new Index(id,1);
            		biLocalMap.put(now, in);
            	}
            }
        	
        }
        
	}
	
	public static void generateTrigrams(String[] AllWordsInFileArray,File file,Map<String,Index> triLocalMap)
	{
		
        for(int i=0;i<AllWordsInFileArray.length-2;i++)
        {
        	String currString = AllWordsInFileArray[i]+" ";
        	String nextString = AllWordsInFileArray[i+1]+" ";
        	String nextnextString = AllWordsInFileArray[i+2];
        	String now = currString+nextString+nextnextString;
        	if( !" ".equals(now) 
        			&& !"-".equals(now)
        			&& !"_".equals(now)
        			&& !"$".equals(now)
        			&& !"".equals(now)
        			&& !"&".equals(now))
        	{
        		
        	
            	if(triLocalMap.get(now) != null)
            	{
            		Index in = triLocalMap.get(now);
            		in.frequency += 1;
            		triLocalMap.put(now, in);
            	}
            	else
            	{
            		//int j = 1;
            		String id = file.getName();
            		id = id.replace("_", "");
            		Index in = new Index(id,1);
            		triLocalMap.put(now, in);
            	}
            }
        	
        }

	}
	
	public static void sortOnTf() throws FileNotFoundException, UnsupportedEncodingException
	{
	    
	    //PrintWriter writer2 = new PrintWriter("Uaii.txt", "UTF-8");
	    for(Entry<String,ArrayList<Index>> a : unigramInvertedIndex.entrySet())
	    {
	    	ArrayList<Index> local = a.getValue();
	    	int tf =  0;
	    	for(int j=0;j<local.size();j++)
	  	  	{
	  		  	Index c = local.get(j);
	  		  	tf += c.frequency;
	  	  	}
	    	unigramtf.put(a.getKey(), tf);
	    }
	    for(Entry<String,ArrayList<Index>> a : bigramInvertedIndex.entrySet())
	    {
	    	ArrayList<Index> local = a.getValue();
	    	int tf =  0;
	    	for(int j=0;j<local.size();j++)
	  	  	{
	  		  	Index c = local.get(j);
	  		  	tf += c.frequency;
	  	  	}
	    	bigramtf.put(a.getKey(), tf);
	    }
	    for(Entry<String,ArrayList<Index>> a : trigramInvertedIndex.entrySet())
	    {
	    	ArrayList<Index> local = a.getValue();
	    	int tf =  0;
	    	for(int j=0;j<local.size();j++)
	  	  	{
	  		  	Index c = local.get(j);
	  		  	tf += c.frequency;
	  	  	}
	    	trigramtf.put(a.getKey(), tf);
	    }
	    
	    /*Unigrams Sorting*/
	    
        List<Entry<String,Integer>> uniTmp = new ArrayList<Entry<String, Integer>>(unigramtf.entrySet());

        Collections.sort(uniTmp,new Comparator<Entry<String,Integer>>() 
        {

 			@Override
 			public int compare(Entry<String, Integer> entry1, Entry<String, Integer> entry2) 
 			{
 				return entry2.getValue().compareTo(entry1.getValue());
 			}
        });
        PrintWriter writerUniTf = new PrintWriter("Uni_Sorted_TF.txt","UTF-8");
        for(int i=0;i<uniTmp.size();i++)
        {
        	Entry<String, Integer> l  = uniTmp.get(i);
        	System.out.println(l.getKey()+ " "+l.getValue());
        	writerUniTf.println(l.getKey()+ " "+l.getValue());
        }
	    
        writerUniTf.close();
        
        /**
         * To generate the StopList
         * 
         * */
        PrintWriter writerStop = new PrintWriter("Stopwords_List.txt","UTF-8");
        System.err.println(unigramtf.size()+" sfsdfsdgsdg");
        
        for(int i=0;i<uniTmp.size();i++)
        {
        	
        	if(uniTmp.get(i).getValue() >= 3000)
        	{
        		writerStop.println(uniTmp.get(i).getKey()+ " " + uniTmp.get(i).getValue());
        	}
        	else
        	{
        		break;
        	}
        }
        writerStop.close();
        /*Bigrams */

        List<Entry<String,Integer>> biTmp = new ArrayList<Entry<String, Integer>>(bigramtf.entrySet());

        Collections.sort(biTmp,new Comparator<Entry<String,Integer>>() 
        {

 			@Override
 			public int compare(Entry<String, Integer> entry1, Entry<String, Integer> entry2) 
 			{
 				return entry2.getValue().compareTo(entry1.getValue());
 			}
        });
        PrintWriter writerBiTf = new PrintWriter("Bi_Sorted_TF.txt","UTF-8");
        for(int i=0;i<biTmp.size();i++)
        {
        	Entry<String, Integer> l  = biTmp.get(i);
        	System.out.println(l.getKey()+ " "+l.getValue());
        	writerBiTf.println(l.getKey()+ " "+l.getValue());
        }
	    
        writerBiTf.close();
	    
        
        /* Tri Grams Sorted*/
        
        List<Entry<String,Integer>> triTmp = new ArrayList<Entry<String, Integer>>(trigramtf.entrySet());

        Collections.sort(triTmp,new Comparator<Entry<String,Integer>>() 
        {

 			@Override
 			public int compare(Entry<String, Integer> entry1, Entry<String, Integer> entry2) 
 			{
 				return entry2.getValue().compareTo(entry1.getValue());
 			}
        });
        PrintWriter writerTriTf = new PrintWriter("Tri_Sorted_TF.txt","UTF-8");
        for(int i=0;i<triTmp.size();i++)
        {
        	Entry<String, Integer> l  = triTmp.get(i);
        	System.out.println(l.getKey()+ " "+l.getValue());
        	writerTriTf.println(l.getKey()+ " "+l.getValue());
        }
	    
        writerTriTf.close();
        
        

	}
	
	
	public static void generateSortByKey() throws FileNotFoundException, UnsupportedEncodingException
	{
	    for(Entry<String,ArrayList<Index>> a : unigramInvertedIndex.entrySet())
	    {
	    	ArrayList<Index> local = a.getValue();
	    	int df =  local.size();
	    	DocumentFrequency d = new DocumentFrequency();
	    	ArrayList<String> s = new ArrayList<String>();
	    	
	    	for(int j=0;j<local.size();j++)
	  	  	{
	  		  	Index c = local.get(j);
	  		  	s.add(c.docId);
	  	  	}
	    	d.docIds = s;
	    	d.dfrequency = df;
	    	unigramdf.put(a.getKey(),d);
	    }
        List<Entry<String,DocumentFrequency>> uniTmp = new ArrayList<Entry<String, DocumentFrequency>>(unigramdf.entrySet());

        Collections.sort(uniTmp,new Comparator<Entry<String,DocumentFrequency>>() 
        {

 			@Override
 			public int compare(Entry<String, DocumentFrequency> entry1, Entry<String, DocumentFrequency> entry2) 
 			{
 				return entry1.getKey().compareTo(entry2.getKey());
 			}
        });
        PrintWriter writerUniDf = new PrintWriter("Uni_Sorted_DocumentF.txt","UTF-8");
        for(int i=0;i<uniTmp.size();i++)
        {
        	
        	Entry<String, DocumentFrequency> l  = uniTmp.get(i);
        	writerUniDf.print(l.getKey()+ ",");
        	
        	ArrayList<String> docs = l.getValue().docIds;
        	for(int j =0;j<docs.size();j++)
        	{
        		writerUniDf.print(docs.get(j)+ " ");
        	}
        	
        	writerUniDf.println(","+l.getValue().dfrequency);
        }
	    
        writerUniDf.close();
        
        
        /**
         * 
         * BiGrams
         */
	    for(Entry<String,ArrayList<Index>> a : bigramInvertedIndex.entrySet())
	    {
	    	ArrayList<Index> local = a.getValue();
	    	int df =  local.size();
	    	DocumentFrequency d = new DocumentFrequency();
	    	ArrayList<String> s = new ArrayList<String>();
	    	
	    	for(int j=0;j<local.size();j++)
	  	  	{
	  		  	Index c = local.get(j);
	  		  	s.add(c.docId);
	  	  	}
	    	d.docIds = s;
	    	d.dfrequency = df;
	    	bigramdf.put(a.getKey(),d);
	    }
        List<Entry<String,DocumentFrequency>> biTmp = new ArrayList<Entry<String, DocumentFrequency>>(bigramdf.entrySet());

        Collections.sort(biTmp,new Comparator<Entry<String,DocumentFrequency>>() 
        {

 			@Override
 			public int compare(Entry<String, DocumentFrequency> entry1, Entry<String, DocumentFrequency> entry2) 
 			{
 				return entry1.getKey().compareTo(entry2.getKey());
 			}
        });
        PrintWriter writerBiDf = new PrintWriter("Bi_Sorted_DocumentF.txt","UTF-8");
        for(int i=0;i<biTmp.size();i++)
        {
        	
        	Entry<String, DocumentFrequency> l  = biTmp.get(i);
        	if(l.getKey().startsWith(" ") || l.getKey().startsWith("."))
        	{
        		
        	}
        	else
        	{
        		writerBiDf.print(l.getKey()+ ",");
            	ArrayList<String> docs = l.getValue().docIds;
            	for(int j =0;j<docs.size();j++)
            	{
            		writerBiDf.print(docs.get(j)+ " ");
            	}
            	
            	writerBiDf.println(","+l.getValue().dfrequency);
        	}
        	
        	

        }
	    
        writerBiDf.close();
        
        
	}
	
	public static String textClean(String input)
	{
//		String output = input.replaceAll( "</?a[^>]*>", "" ).replaceAll("<b>", "")
//				  .replaceAll("</b>", "").replaceAll("<i>", "")
//				  .replaceAll("</i>", "").replaceAll( "<sub>[^>]*</sub>", "" );
		String output = input.replace("&nbsp", " ");
			output = output.replaceAll("\\<[^>]*>"," ");
		output = output.replaceAll("((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+"
				+ "[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)", " ");
		
		output = output.replaceAll("(\\s+[-|–|—|/]\\s*)|(\\s*[-|–|—]\\s+)"," ");
		output = output.replace("&amp", "&");
		output = output.replace("&lt", "");
		output = output.replace("&gt", "");
		
		
		output = output.replaceAll("[:;,()\\[\\]\'\"\\\\!@#$%^*+=\\|`~{}?><\\/]", "");               
		//output = output.replace(". ", " ");
		//output = output.replaceAll("\\.\\n", "\n");
		
		output = output.toLowerCase();
		output = output.replaceAll("([a-zA-Z]*)\\.","$1");
		output = output.replaceAll("\\s+", " ");
		return output;
	}
}
