

————>>>>>>>>Folders to be created 
Lucene_Rankings
Lucene_RankingsEval
BM25Stop
BM25StopEval
RelevanceExpansion
RelevanceExpansionEval
BM25
BM25Eval
tfidf
tfidfEval
BM25StopExpansion
BM25StopExpansionEval
theasursBM25
theasursBM25Eval
StemBM25
Input — Contains all the input files
cacm — Contains all the documents in the corpora.

— Keep all the java files under the package project under src folder in general project in eclipse.

Libraries to be installed
Lucene
Jaws

Software Needs to be Installed
WordNet


>>>>TASK 1

1. Run the BM25.java, which will generate the qi.txt in the BM25 folder, where i is the query id.

2.Run the tfidf.java, which will generate the qi.txt in the tfidf folder, where i is the query id.

3. Create folder Run the Lucene.java file and the output will be in Lucene_Rankings folder



>>>>TASK 2

1. Pseudo relevance feedback — to run this code, run the QueryExpansion.java, which will generate the qi.txt, where i is the query id, in the RelevanceExpansion folder.

2. Thesauri — to run this code, run the Theasurus.java, which will generate BM25 retrieval model for all the queries in the theasurusbm25eval 


>>>>TASK 3
A. Stop Words — run the StopRun.java, which will generate the qi.txt, where i is the query id, in the BM25Stop folder.

B.
Created a folder called 
run the java file Task3.java and the output will the 
above mention folder

>>>>EVALUATION

1. For the seventh run, run the StopExpansion.java,which will generate the qi.txt, where i is the query id, in the BM25StopExpansion folder.

2. For the calculation of the retrieval effectiveness measures like MAP, MRR, p @ k=5, p @ k=20, recall and precision run the Evaluation.java with the following parameters
	i) BM25Stop and BM25StopEval
	ii) RelevanceExpansion and RelevanceExpansionEval
	iii) BM25 and BM25Eval
	iv) tfidf and tfidfEval
	v) BM25StopExpansion and BM25StopExpansionEval
	vi) theasursBM25 and theasursBM25Eval
	vii) Lucene_Rankings and Lucene_RankingsEval



>>>>>>>>>> Snippet Generation

1. To run the snippet generation, run the code Main.java this will generate the snippet for all the queries in the snippetoutput folder

