package assignment_2;

import java.util.ArrayList;
import java.util.HashSet;

class Link
{
	ArrayList<String> inlink ;//= new ArrayList<String>();
	HashSet<String> outlink ;//= new ArrayList<String>();
	Double pageRank ;//= 0;
	Link()
	{
		inlink = new ArrayList<String>();
		outlink = new HashSet<String>();
		pageRank = 0.0d;
	}
	

	public ArrayList<String> getInlink() {
		return inlink;
	}

	public void setInlink(ArrayList<String> inlink) {
		this.inlink = inlink;
	}

	public HashSet<String> getOutlink() {
		return outlink;
	}

	public void setOutlink(HashSet<String> outlink) {
		this.outlink = outlink;
	}

	public Double getPageRank() {
		return pageRank;
	}

	public void setPageRank(Double pageRank) {
		this.pageRank = pageRank;
	}
	
}
