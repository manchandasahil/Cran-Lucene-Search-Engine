package Lucene;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.MultiSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexFiles {

  private IndexFiles() {
  }

  @SuppressWarnings("resource")
public static void Index() throws Exception {
	  
      boolean create = true;
      try {
          System.out.println("Indexing to directory '" + Utils.INDEX_PATH + "'...");
          Directory dir = FSDirectory.open(Paths.get(Utils.INDEX_PATH));
          
          Splitter s = Splitter.getInstance();
          CharArraySet myStopSet = CharArraySet.copy(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
          Analyzer analyzer = new WhitespaceAnalyzer();
          Similarity sim = new BM25Similarity();
          Similarity similarity[] = {
                  new BM25Similarity(2, (float) 0.89),
                  new LMDirichletSimilarity(1500)
          };
          IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
          iwc.setSimilarity(new MultiSimilarity(similarity));

          if (create) {
              iwc.setOpenMode(OpenMode.CREATE);
          } else {
              iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
          }

          IndexWriter writer = new IndexWriter(dir, iwc);
          indexDocsfromArrayList(writer,s);
          writer.close();
      } catch (IOException e) {
          System.out.println(" caught a " + e.getClass() +
                  "\n with message: " + e.getMessage());
      }
  }
  static void indexDocsfromArrayList(IndexWriter iw,Splitter s) throws Exception{
	  ArrayList<Document> documents = s.returnDocuments();
	  for(Document d:documents) {
		  try {
			iw.addDocument(d);
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
  }
}

//{}