package assignment1;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Scanner;


public class Crawler_DFS {

	private static int depth = 1;
	private static int MAX_DEPTH = 5;
	private static int MAX_COUNT = 1000;
	private static String key;
	private static long start =0;
	private static long stop =0;
	
	HashMap<String, Integer> visited = new HashMap<String, Integer>();
	ArrayList<String> listOfUniqueUrls = new ArrayList<String>();
	
	
	public void DFS(String inputurl,String key) throws IOException, InterruptedException
	{
		
		this.key = key;
		/**
		 * 
		 * return if size > 1000
		 */
		if(visited.size() >= 1000)
		{
			if(depth > 1)
			{
				depth--;
			}
				
			return;
		}
		
		
		 ArrayList<String> listOfAllUrls = new ArrayList<String>();
		 visited.put(inputurl.toLowerCase(), 1);
     	 listOfUniqueUrls.add(inputurl);
     	
     	 /**
     	  * return if depth > 5
     	  */
     	 if(depth >= MAX_DEPTH)
		 {
			 depth = depth-1;
			 System.out.println(depth);
			 return;
		 }

     	 /*getting all the links in the seed page
     	  * */
         Document doc = Jsoup.connect(inputurl.toString()).get(); // doc.bod
         Elements links = doc.select("a[href]");
         
         for(Element a : links)
         {
        	 System.out.println(a.attr("abs:href"));
        	 listOfAllUrls.add(a.attr("abs:href"));
         }

        while( !listOfAllUrls.isEmpty() && (visited.size() < MAX_COUNT))
        {
//        	
        	System.out.println(depth);
        	String next = get_next_valid_url(listOfAllUrls);
        	
        	if(next == "no idea")
        	{
        		depth--;
        		return;
        	}
        	depth++;
        	System.out.println("next is " +next);
        	DFS(next.toString(),key);
        	
        }
		
	}
	
	private String get_next_valid_url(ArrayList<String> links) throws IOException, InterruptedException {

		/*while the list of links in a page is empty keep iterating for the valid link*/
        while (!links.isEmpty()) 
        {
        	String link = links.remove(0);
        	
        	if(link.startsWith("https://en.wikipedia.org/wiki") )
        	{
    			String presentlink = link ;//.attr("abs:href");
    			String a1 = presentlink.split("#")[0];

   			stop = System.currentTimeMillis();
    			
    			//stop = System.currentTimeMillis();
    			System.out.println(stop-start);
    			if(stop-start < 1000)
    				Thread.sleep(1000 - (stop-stop));

        		if(!a1.matches(".*\\b:\\b.*")&&
        					!a1.toLowerCase().matches(".*\\bmain_page\\b.*") &&
        					(!a1.substring(6, (a1.length())).toLowerCase().contains(":")) &&
        						(Jsoup.connect(a1).get().body().text().toLowerCase().contains(key) || 
        								a1.toLowerCase().contains(key)))
        		{
        			
        			if(visited.get(a1.toLowerCase()) == null)
        			{
        				return a1;
        			}
        		}
        		start = System.currentTimeMillis();
        	}
        }
        return "no idea";
	}
	
}
