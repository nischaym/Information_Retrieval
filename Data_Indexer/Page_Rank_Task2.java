package assignment_2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

public class Page_Rank_Task2 {

	

	static Integer N;
	static HashSet<String> sink;
	final static double damping = 0.95;
    static HashMap<String,Link>  links ;//=  new HashMap<String,ArrayList<ArrayList<String>>>();
    static PrintWriter writer_wtg1_perplex;
    static PrintWriter writer_wtg1_final_rank;
    
	public static void main(String[] args) throws IOException
	{
     	String line = null;
      	//String fileName = "page_rank_sample_one.txt";
      	//String fileName = "Urls_Assign2_Task1.txt";
     	String fileName = "Urls_Assign2_wtg2_source.txt";
      	
      	FileReader fileReader = new FileReader(fileName);
      	N = 0;
      	sink = new HashSet<String>();
      	links =  new HashMap<String,Link>();
        writer_wtg1_perplex = new PrintWriter("Urls_Assign2_Task1_wtg2_perplexity_d_095.txt", "UTF-8");
        writer_wtg1_final_rank  = new PrintWriter("Urls_Assign2_Task1_wtg2_final_ranking_d_095.txt", "UTF-8");
        // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        
        
        ArrayList<String> s = new ArrayList<String>(); 
        
        while((line = bufferedReader.readLine()) != null) 
        {
        	s.add(line);
           	String[] b = line.split(" ");
           	
           	//ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
           	Link temp = new Link();
           	HashSet<String> inhash = new HashSet<String>();
           	HashSet<String> outhash = new HashSet<String>();
           	ArrayList<String> inlinks = new ArrayList<String>();
           	ArrayList<String> outlinks = new ArrayList<String>();
           	//ArrayList<String> pr = new ArrayList<String>();
           	double pr =0;
           	
//           	if(b.length-1 == 0)
//        	{
//        		sink.add(b[0]);
//        		
//        		temp.inlink = inlinks;
//        		temp.outlink = outlinks;
//        		temp.pageRank = pr;
//        		links.put(b[0],temp);
//        	}
           	
           	
           	for(int i=1;i<b.length;i++)
           	{
           		inhash.add(b[i]);
           	}
           	
           	for(String s2 : inhash)
           	{
           		inlinks.add(s2);
           	}
           	
    		temp.inlink = inlinks;
    		temp.outlink = outhash;
    		temp.pageRank = pr;
    		links.put(b[0],temp);
        	N++;
        }
        
        for(String a: s)
        {
        	String[] b = a.split(" ");
        	if(b.length -1 == 0)
        	{
        		continue;
        	}
        	else
        	{
        		for(int i=1;i<b.length;i++)
        		{
        			Link temp  = links.get(b[i]);
        			temp.outlink.add(b[0]);
        		}
        	}
        }
        
       
        
      for(Entry<String, Link> l : links.entrySet())
      {
    	  if(l.getValue().outlink.size() ==0)
    	  {
    		  sink.add(l.getKey());
    	  }
      }
      System.out.println(sink.size());
        PageRank();
        
//        for(Entry<String, Link> l : links.entrySet())
//        {
//        	System.out.println(l.getKey()+" inlinks are "+ l.getValue().inlink+ " outlinks are "+ l.getValue().outlink +" rank is "+l.getValue().pageRank );
//        }

        //PageRank();
        
	}
	
	
	private static void PageRank() 
	{
	
		System.out.println(N);
		double pr = (double) 1/(double) N;
		double sinkPr = 0;
		double newPr = 0;
		int count = 0;
		double[] tempPr = new double[links.size()];
		
		double hpr = 0;
        double prevPerplexity = 0 ;
        double currPerplexity = 0 ;
        ArrayList<Double> preplexity = new ArrayList<Double>();
        
        for(Entry<String, Link> l : links.entrySet())
        {
        	l.getValue().pageRank = pr;
        }
        
        ArrayList<String> sink1 = new ArrayList<>();
        
        for(String si : sink)
        {
        	sink1.add(si);
        }
        
        
        while (count<4)
        {
        	sinkPr = 0;
            if(sink.size() != 0)
            {
            	for(int i=0;i<sink.size();i++)
            	{
            		sinkPr += links.get(sink1.get(i)).pageRank; 
            	}
            }
            System.out.println(sink.size());
            System.out.println(sinkPr);
            
            int i =0;
            for(Entry<String, Link> l : links.entrySet())
            {
            	newPr = (double)(1-damping)/N;
            	newPr += (double) (((double)damping * (double)sinkPr)/(double)N);
            	
            	ArrayList<String> in = l.getValue().inlink;
            	
            	for(String inl : in)
            	{
            		newPr += (double)((double)damping *(double) links.get(inl).pageRank)/(double) (links.get(inl).outlink.size());  
            	}
            	
            	//storing in temp array
            	tempPr[i] = newPr;
            	i++;
            	
            }
            
            /*copying the new PRs to the actual place*/
            i = 0;
            for(Entry<String, Link> l : links.entrySet())
            {
            	l.getValue().pageRank = tempPr[i];
            	i++;
            }
            
            /*Calculate the perplexity*/
            
            for(Entry<String, Link> l : links.entrySet())
            {
            	double value = l.getValue().pageRank;
            	hpr +=  value * (Math.log(value)/Math.log(2));
            }
            
            hpr = hpr * -1;
            System.out.println("hpr is "+hpr);
            currPerplexity = Math.pow(2.0, hpr);
            
            
            System.out.println(currPerplexity - prevPerplexity);
            //if(((currPerplexity - prevPerplexity) < 1) || ((prevPerplexity - currPerplexity) < 1))
            if(Math.abs(currPerplexity - prevPerplexity) < 1)
            {
            	count++;
            }
            else
            {
            	count = 0;
            }
            
            preplexity.add(currPerplexity);
            writer_wtg1_perplex.println(currPerplexity);
            
            prevPerplexity = currPerplexity;
            hpr = 0;
        } /*end of while loop */
        
        writer_wtg1_perplex.close();
        
        ///links.values().
       
       List<Entry<String,Link>> tmp = new ArrayList<Entry<String, Link>>(links.entrySet());
       
       
       
       Collections.sort(tmp,new Comparator<Entry<String,Link>>() 
       {

			@Override
			public int compare(Entry<String, Link> entry1, Entry<String, Link> entry2) 
			{
				return entry2.getValue().pageRank.compareTo(entry1.getValue().pageRank);
			}
       });
       
       
       for(int i = 0;i<50;i++)
       {
    	   Entry<String, Link> l  = tmp.get(i);
    	   System.out.println(l.getKey()+" "+l.getValue().pageRank);
    	   writer_wtg1_final_rank.println(l.getKey()+" "+l.getValue().pageRank);
       }
       writer_wtg1_final_rank.close();
	}


	
}
