package assignment_2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Assign2_Task3A_Calculate_Tau {

	static Integer C;
	static Integer D;
	static Integer N;
	static Double tau;
	
	
	
	public static void main(String[] args) throws IOException
	{
        PrintWriter writer = new PrintWriter("Assign2_Task3A_WTG2_TAU_D0.95_vs_0.85.txt", "UTF-8");

		
      	String fileName_wtg_pr = "Urls_Assign2_Task1_wtg2_final_ranking_d_095.txt";
      	//String fileName_wtg_inlink = "Assign2_Task3A_WTG1.txt";
      	String fileName_wtg_inlink = "Urls_Assign2_Task2_wtg2_final_ranking.txt";

		String line = "";
      	FileReader fileReader_wtg_pr = new FileReader(fileName_wtg_pr);
      	FileReader fileReader_wtg_inlink = new FileReader(fileName_wtg_inlink);

      	C =0;D=0;N=0;
      	tau = 0.0;
      	
      	Map<String,Integer> hash_wtg_pr = new LinkedHashMap<String,Integer>();
      	String[] array_wtg_pr = new String[50];
      	Map<String,Integer> hash_wtg_inlink = new LinkedHashMap<String,Integer>();
      	//String[] array_wtg_inlink = new String[50];
      	
        BufferedReader bufferedReader_wtg_pr = new BufferedReader(fileReader_wtg_pr);
        BufferedReader bufferedReader_wtg_inlink = new BufferedReader(fileReader_wtg_inlink);
        
        int i=0;
        double prev = 0;
        double curr = 0;
        int k =0;
        while((line = bufferedReader_wtg_pr.readLine()) != null) 
        {
        	String[] b = line.split(" ");
        	curr = Double.valueOf(b[1]);
        	if(curr < prev)
        	{
        		i++;
        		hash_wtg_pr.put(b[0],i);
        		
        	}
        	else if(curr == prev)
        	{
        		hash_wtg_pr.put(b[0],i);
        	}
        	else
        	{
        		i++;
        		hash_wtg_pr.put(b[0],i);
        	}
        	
        	array_wtg_pr[k] = b[0];
        	prev = curr;
        	k++;
        }

        for(Entry<String,Integer> e : hash_wtg_pr.entrySet())
        {
        	System.out.println(e.getKey()+ " " +e.getValue());
        }
        
        
        i=0;
        prev = 0;
        curr = 0;
        k =0;
        while((line = bufferedReader_wtg_inlink.readLine()) != null) 
        {
        	String[] b = line.split(" ");
        	
        	curr = Double.valueOf(b[1]);
        	if(curr < prev)
        	{
        		i++;
        		hash_wtg_inlink.put(b[0],i);
        		
        	}
        	else if(curr == prev)
        	{
        		hash_wtg_inlink.put(b[0],i);
        	}
        	else
        	{
        		i++;
        		hash_wtg_inlink.put(b[0],i);
        	}
        	
        	prev = curr;
        	
        	array_wtg_pr[k] = b[0];
        	k++;
        }

        for(Entry<String,Integer> e : hash_wtg_inlink.entrySet())
        {
        	System.out.println(e.getKey()+ " " +e.getValue());
        }

        
        
        ArrayList<String> intersect = new ArrayList<String>(hash_wtg_inlink.keySet());
        
        intersect.retainAll(hash_wtg_pr.keySet());
        
       
        
       System.out.println(intersect.size());
       
       N = (intersect.size() * (intersect.size()-1))/2;
       int mn = 0;
       for(i=0;i<intersect.size();i++)
       {
    	   String first = intersect.get(i);
    	   
    	   for(int j =i+1;j<intersect.size();j++)
    	   {
    		   String second = intersect.get(j);
    		   
    		   if((hash_wtg_pr.get(first).equals(hash_wtg_pr.get(second))) ||
    				   (hash_wtg_inlink.get(first).equals(hash_wtg_inlink.get(second))))
    		   {
    			   // do nothing
    			   mn++;
    			   continue;
    		   }
    		   else if((hash_wtg_inlink.get(first) < hash_wtg_inlink.get(second)) &&
    				   (hash_wtg_pr.get(first) < hash_wtg_pr.get(second)) ||
    			  (hash_wtg_inlink.get(first) > hash_wtg_inlink.get(second)) &&
    			  	   (hash_wtg_pr.get(first) > hash_wtg_pr.get(second)))
    		   {
    			   C++;
    		   }
    		   else
    		   {
    			   D++;
    		   }
    	   }
    	   
       }
       
        
        tau = (double)(C-D)/N;
        
        System.out.println("N = "+N);
        System.out.println("C = "+C);
        System.out.println("D = "+D);
        System.out.println("tau = "+tau);
        System.out.println(mn);
        writer.println("N = "+N);
        writer.println("C = "+C);
        writer.println("D = "+D);
        writer.println("tau = "+tau);
        
        writer.close();
        bufferedReader_wtg_pr.close();
        bufferedReader_wtg_inlink.close();
        
	}
	
}
