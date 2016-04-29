package assignment_2;

import java.io.BufferedReader;
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



public class Assign2_Task3A {
	
	static Map<String, Integer> allLinks = new LinkedHashMap<String, Integer>();

	public static void main(String[] args) throws IOException, InterruptedException {
		
		/* Files for the URL and the raw content */
        PrintWriter writer = new PrintWriter("Assign2_Task3A_WTG1.txt", "UTF-8");
       	PrintWriter w = new PrintWriter("all_inlinks_wtg1.txt","UTF-8");
     	String line = null;
      	String fileName = "Urls_Assign2_Task1.txt";
      	//String fileName = "Urls_Assign2_wtg2_source.txt";

      	HashMap<Integer, Integer> hash = new HashMap<>();
        
      	FileReader fileReader = new FileReader(fileName);

        // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        
        Integer noInlinks = 0;
        while((line = bufferedReader.readLine()) != null) 
        {
        	String[] b = line.split(" ");
        	System.out.println(b[0]+ " "+(b.length-1));
        	allLinks.put(b[0], b.length-1);
        	//w.println(b[0]+" "+(b.length-1));
        	if(b.length -1 ==0)
        	{
        		noInlinks ++;
        	}
        	
        	
        }

        for(Entry<String, Integer> k : allLinks.entrySet())
        {
        	if(hash.get(k.getValue()) == null)
        	{
        		hash.put(k.getValue(), 1);
        	}
        	else
        	{
        		Integer t = hash.get(k.getValue());
  
        		hash.put(k.getValue(), t+1);
        	}

        }
        
        /*only for graph*/
        for(Entry<Integer, Integer> l : hash.entrySet())
        {
        	
        	System.out.println(l.getKey()+ " " +l.getValue());
        	w.println(Math.log10(l.getKey())+ " " +Math.log10(l.getValue()));
        }
        
        
        w.close();
        
        
        
        
        
        System.out.println("Links with No with links for them : "+noInlinks);
        
        List<Entry<String,Integer>> tmp = new ArrayList<Entry<String, Integer>>(allLinks.entrySet());

        Collections.sort(tmp,new Comparator<Entry<String,Integer>>() 
        {

 			@Override
 			public int compare(Entry<String, Integer> entry1, Entry<String, Integer> entry2) 
 			{
 				return entry2.getValue().compareTo(entry1.getValue());
 			}
        });

        
        for(int i=0;i<50;i++)
        {
        	Entry<String, Integer> l  = tmp.get(i);
        	System.out.println(l.getKey()+ " "+l.getValue());
        	writer.println(l.getKey()+ " "+l.getValue());
        }
        
        writer.close();

        
        
        
	}
}
