package assignment_2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
//import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class Crawler_Task1 {
	
	static Map<String, HashSet<String>> allLinks = new LinkedHashMap<String, HashSet<String>>();
	static Map<String, HashSet<String>> allLinks_to_lower = new LinkedHashMap<String, HashSet<String>>();
	static HashSet<String> hash = new HashSet<String>();
	static ArrayList<String> al = new ArrayList<String>();
 	public static void main(String[] args) throws IOException, InterruptedException {
		
		/* Files for the URL and the raw content */
        PrintWriter writer = new PrintWriter("Urls_Assign2_Task1.txt", "UTF-8");
         	
     	String line = null;
      	String fileName = "Urls_Part1.txt";
      	FileReader fileReader = new FileReader(fileName);

        // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        
        
        
        
        while((line = bufferedReader.readLine()) != null) 
        {
        	HashSet<String> s = new HashSet<String>();
        	allLinks.put(line,s);
        	allLinks_to_lower.put(line.toLowerCase(), s);
        	hash.add(line);
            System.out.println(line);
         }
        System.out.println(allLinks.size());
        
        for(String s : hash)
        {
        	al.add(s.toLowerCase());
        }
        
        
        for(Entry<String, HashSet<String>> s : allLinks.entrySet())
        {
        	System.out.println("sfknsaofn");
        	generateUrl_BFS(s.getKey());
        }

        
        
        
        for(Entry<String, HashSet<String>> s : allLinks_to_lower.entrySet())
        {
        	writer.print(s.getKey().substring(30,s.getKey().length()));
        	HashSet<String> h =  s.getValue();
        	//writer.print(" "+s.getValue().size());
        	for(String s1 : h)
        	{
        		writer.print(" "+s1.substring(30, s1.length()));
        	}
        	writer.println("");
        }
        
        writer.close();
        System.out.println(allLinks.get("https://en.wikipedia.org/wiki/Sustainable_energy").size() );
}

 	/* A Method to find all the ursl a */
	private static void generateUrl_BFS(String url) throws IOException, InterruptedException
    {
		
        Document doc = Jsoup.connect(url).get(); 
        Elements links = doc.select("a[href]");
        
        for(Element link : links)
        {
        	if(link.attr("abs:href").startsWith("https://en.wikipedia.org/wiki") )
        	{
        		
    			String presentlink = link.attr("abs:href");
    			String a1 = presentlink.split("#")[0];
    			
    			if(!a1.matches(".*\\b:\\b.*")&&
    					!a1.toLowerCase().matches(".*\\bmain_page\\b.*") &&
    					(!a1.substring(6, (a1.length())).toLowerCase().contains(":")))
    			{
    				
    				if(al.contains(a1.toLowerCase()) && !a1.toLowerCase().equalsIgnoreCase(url))
    				{
    					allLinks_to_lower.get(a1.toLowerCase()).add(url.toLowerCase());
    					
    				}
     			}
        	}
        }
    
//        for(String s : hash)
//        {
//        	if(s.toLowerCase().equalsIgnoreCase(url.toLowerCase()))
//        	{
//        		continue;
//        	}
//        	else
//        	{
//                Document doc = Jsoup.connect(s).get(); 
//                Elements links = doc.select("a[href]");
//                
//                for(Element link:links)
//                {
//                	if(link.attr("abs:href").startsWith("https://en.wikipedia.org/wiki") )
//                	{
//                		
//            			String presentlink = link.attr("abs:href");
//            			String a1 = presentlink.split("#")[0];
//            			
//            			if(!a1.matches(".*\\b:\\b.*")&&
//            					!a1.toLowerCase().matches(".*\\bmain_page\\b.*") &&
//            					(!a1.substring(6, (a1.length())).toLowerCase().contains(":")))
//            			{
//            				
//            				if(a1.toLowerCase().equals(url.toLowerCase()))
//            				{
//            					System.out.println(s);
//            					allLinks.get(url).add(s);
//            					break;
//            				}
//            			}
//                	}
//
//                }
//
//        	}
//        }
    
    
    }
    
	
	
	
}
