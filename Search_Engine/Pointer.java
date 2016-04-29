package project;

public class Pointer {
	
	private String docId;
	
	private Integer termFrequency;
	
	Pointer(String d, Integer tf)
	{
		this.setDocId(d);
		this.setTermFrequency(tf);
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public Integer getTermFrequency() {
		return termFrequency;
	}

	public void setTermFrequency(Integer termFrequency) {
		this.termFrequency = termFrequency;
	}

	
}
