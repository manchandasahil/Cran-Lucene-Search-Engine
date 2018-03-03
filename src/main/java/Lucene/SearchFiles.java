package Lucene;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/** Simple command-line based search demo. */
public class SearchFiles {
    private SearchFiles() {}
    /** Simple command-line based search demo. */
    public static void Search() throws Exception {

        String[] field = {"Title","Author","Source","Content"};
        String queryString = null;

        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(Utils.INDEX_PATH)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Splitter s = Splitter.getInstance();
        
        CharArraySet myStopSet = CharArraySet.copy(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
        Analyzer analyzer = new StandardAnalyzer();
        
        
        QueryParser parser = new MultiFieldQueryParser(field, analyzer);
        parser.setAllowLeadingWildcard(true);
        
        
        QueryFire queryfire = new QueryFire();
       HashMap<Integer,String> allQueries = queryfire.readQueries();
       File resultsfile = new File("cran.results");
       resultsfile.createNewFile();
       PrintWriter pw = new PrintWriter(resultsfile);
       
        for(int j:allQueries.keySet()) {
        	String line = allQueries.get(j);

            if (line == null || line.length() <= 0) {
            	continue;
            }

            line = line.trim();

            Query query = parser.parse(line);
            //System.out.println("Searching for: " + line);

            doPagingSearch(j,searcher, query, Utils.DOMAIN_SIZE, pw);
            if (queryString != null) {
                break;
            }
        }
        pw.close();
        reader.close();
        
        Scorer scorer = new Scorer();
        scorer.init();
        scorer.score();
        System.out.println("Precision " + scorer.calculatePrecision());
        System.out.println("Recall " + scorer.calculateRecall());
    }

    public static void doPagingSearch(int querynumber,IndexSearcher searcher, Query query, int hitsPerPage,PrintWriter pw) throws IOException {
        TopDocs results = searcher.search(query, 10);
        ScoreDoc[] hits = results.scoreDocs;
        //System.out.println(hits[0]);
        for(int i=0;i<hits.length;i++) {
        	int s = hits[i].doc;
        	Document d = searcher.doc(s);
        	int j = i+1;
        	pw.println(querynumber + " Q0 " +  d.get("ID") + " " + j + " " + hits[i].score + " STANDARD");
        	pw.flush();
        }
    }
}