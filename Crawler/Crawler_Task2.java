package assignment1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class Crawler_Task2 {

    static HashMap<String, Integer> hash = new HashMap<String, Integer>();
    static HashMap<String, Integer> hash1 = new HashMap<String, Integer>();
    static String key;
    private static long start_bfs =0;
    private static long stop_bfs=0;
    
    
    //private static PrintWriter outFile;

	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		
        PrintWriter writer = new PrintWriter("Urls_Part2_BFS.txt", "UTF-8");
        PrintWriter writer_DFS = new PrintWriter("Urls_Part2_DFS.txt", "UTF-8");
	      Scanner in = new Scanner(System.in);
	   	 
	      System.out.println("Enter an seed URL");
	      String inputurl = in.nextLine();
	      System.out.println(inputurl);
	      System.out.println("Enter an Key Word");
	      key = in.nextLine();
	      
	      String url = inputurl;
	      if(inputurl == "" || inputurl == null)
	      {
	    	  inputurl = "https://en.wikipedia.org/wiki/Sustainable_energy";
	    	  url = inputurl.toString();
	      }
	      
	      if(key == "" || key == null)
	      {
	    	  key = "solar";
	      }
        
		int count = 0;
        //Validate.isTrue(args.length == 1, "usage: supply url to fetch");http://en.wikipedia.org/wiki/Hugh_of_Saint-Cher
        //String url = inputurl;//"https://en.wikipedia.org/wiki/Sustainable_energy";
        key = "solar";
    	print("Fetching %s...", url);
    	
    	
    	
    	String[] wholelist = new String[1000];
    	wholelist[0] = url;//https://en.wikipedia.org/wiki/Sustainable_energy";
    	hash.put(url.toLowerCase(), 1);
    	count++;
        
        /**
         * FOR BFS
         */
        int i =0;
        while(count < 1000)
        {
        	System.out.println(count);
        	url = wholelist[i];
        	System.out.println("UR is " +url);
            Document doc = Jsoup.connect(url).get(); 
            Elements links = doc.select("a[href]");
            ArrayList<String> b = generateUrl_BFS(links);
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
            //count++;
         }	
        
        /**
         * Copy the list of urls to a file
         */
        for(String a :wholelist)
        {
        	writer.println(a);
        }
        writer.close();
        System.out.println(count);
        System.out.println(hash.size()); 
        

    	// DFS LOGIC
    	
        Crawler_DFS crawler = new Crawler_DFS();
        
        crawler.DFS(inputurl.toString(),key);
        
        /**
         * Copy the list of urls to a file
         */
        for(String a: crawler.listOfUniqueUrls){
        	
        	System.out.println(a);
        	writer_DFS.println(a);
        	
        }
        writer_DFS.close();
        System.out.println(crawler.listOfUniqueUrls.size());
        
    }


	/* To get a List of Unique URLs using BFS */
	private static ArrayList<String> generateUrl_BFS(Elements links) throws IOException, InterruptedException
    {
    	ArrayList<String> a = new ArrayList<String>();
    	//
        for (Element link : links) 
        {
        	/*check if stating with standard english wiki page*/
        	if(link.attr("abs:href").startsWith("https://en.wikipedia.org/wiki") )
        	{
    			String presentlink = link.attr("abs:href");
    			String a1 = presentlink.split("#")[0];
 
    			/**
    			 * Politeness policy
    			 */
    			stop_bfs = System.currentTimeMillis();
     			System.out.println(stop_bfs-start_bfs);
    			if(stop_bfs-start_bfs < 1000)
    				Thread.sleep(1000 - (stop_bfs-stop_bfs));

    				
    			/*Check for the url pattern*/
    			if(!a1.matches(".*\\b:\\b.*")&&
        					!a1.toLowerCase().matches(".*\\bmain_page\\b.*") &&
        					(!a1.substring(6, (a1.length())).toLowerCase().contains(":")) &&
        						(Jsoup.connect(a1).get().body().text().toLowerCase().contains(key) || 
        								a1.toLowerCase().contains(key)))
        		{
        		
               		if(hash.get(a1.toLowerCase()) != null )
            		{
            			/* Do Nothing*/
            		}
            		else
            		{
            			if( hash.size() == 1000)
            			{
            				break;
            			}
            			
            			/*add to the hash map*/
            			hash.put(a1.toLowerCase(), 1);
                		print("%s", a1, trim(link.text(), 35));
                		a.add(a1);
                		
            		}
        		}
    			start_bfs = System.currentTimeMillis();
        	}
        }
        return a;
    }
    
	/*Helper Functions
	 * */
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

