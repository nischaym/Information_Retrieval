package assignment1;

import org.jsoup.Jsoup;
//import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import java.io.File;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
//import java.lang.reflect.Array;
// java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Crawler_Task1 {
	
	/*To Maintain uniqueness of the Links */
    static HashMap<String, Integer> hash = new HashMap<String, Integer>();
    private static long start;
	private static long stop;
    
 	public static void main(String[] args) throws IOException, InterruptedException {
		
		/* Files for the URL and the raw content */
        PrintWriter writer = new PrintWriter("Urls_Part1.txt", "UTF-8");
        PrintWriter writer1 = new PrintWriter("Page_Content_Part1.txt", "UTF-8");
         	
		int count = 0;
        String url = "https://en.wikipedia.org/wiki/Sustainable_energy";
    	print("Fetching %s...", url);

    	String[] wholelist = new String[1000];
    	wholelist[0] = "https://en.wikipedia.org/wiki/Sustainable_energy";
    	hash.put(url.toLowerCase(), 1);
    	count++;
        
    	/**
         *	Writing the content of seed URL 
         */
        Document doc1 = Jsoup.connect("https://en.wikipedia.org/wiki/Sustainable_energy").get();
   		writer1.println(url);
		writer1.println(doc1);
		writer1.println("***********************************************************************************");
		writer1.println(" ");
		
		/**
		 * one sec delay
		 * */
		Thread.sleep(1000);
		
        int i =0;
        while(count < 1000)
        {
        	System.out.println(count);
        	url = wholelist[i];
        	System.out.println("UR is " +url);
            Document doc = Jsoup.connect(url).get(); 
            Elements links = doc.select("a[href]");
            ArrayList<String> b = generateUrl_BFS(links,writer1);
            System.out.println(" ");
            for(int j=0;j< b.size();j++)
            {
            	if(count == 1000)
            	{
            		break;
            	}
            	wholelist[count] = b.get(j);
            	count++;
            }
            i++;
         }	
        
        /* copying the URLS to the file*/
        for(String a :wholelist)
        {
        	writer.println(a);
        }
        
        /* Closing the files*/
        writer.close();
        writer1.close();
        System.out.println(count);
        System.out.println(hash.size());
        
}

 	/* A Method to find all the ursl a */
	private static ArrayList<String> generateUrl_BFS(Elements links,PrintWriter writer1) throws IOException, InterruptedException
    {
    	ArrayList<String> a = new ArrayList<String>();
    	
        for (Element link : links) 
        {
        	/**
        	 * Start the delay timer
        	 */
        	start = System.currentTimeMillis();
        	
        	
        	/* removing links having # , : , Non-English pages and Main_Page */
        	if(link.attr("abs:href").startsWith("https://en.wikipedia.org/wiki") )
        	{
        		if(!link.attr("abs:href").matches(".*\\b:\\b.*")&&
        				!link.attr("abs:href").substring(6, (link.attr("abs:href").length())).toLowerCase().contains(":")&&
        					!link.attr("abs:href").toLowerCase().matches(".*\\bmain_page\\b.*"))
        		{
        			/* to remove link's part which is after # */
        			String presentlink = link.attr("abs:href");
        			String a1 = presentlink.split("#")[0];
        			
        			
        			/* checking for the duplicacy */
               		if(hash.get(a1.toLowerCase()) != null )
            		{
            			/* Do Nothing*/
            		}
            		else
            		{
            			/* If size exceeds 1000 stop the crawling */
            			if( hash.size() == 1000)
            			{
            				break;
            			}
            			
            			/* Adding the url to the hash map and to the ArrayList*/
            			hash.put(a1.toLowerCase(), 1);
            			a.add(a1);
            			print("%s", a1, trim(link.text(), 35));
                		
            			/**
            			 * 
            			 * Delay only for the left time
            			 */
            			stop = System.currentTimeMillis();
            			System.out.println(stop-start);
            			if(stop-start < 1000)
                		Thread.sleep(1000 - (stop-stop));
            			
                		/*Taking the content of the URl and writing it to a file */
                		Document doc1 = Jsoup.connect(a1).get();
                		writer1.println(a1);
                		writer1.println(doc1);
                		writer1.println("***********************************************************************************");
                		writer1.println(" ");
            		}
        		}
        	}
        }
        return a;
    }
    
	/* Helper Functions */
    private static void print(String msg, Object... args) 
    {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) 
    {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}

