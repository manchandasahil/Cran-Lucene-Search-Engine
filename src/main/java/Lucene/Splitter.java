package Lucene;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;


public class Splitter {
	static String path = "cran.all.1400";
	private static Splitter s;
	public static Splitter getInstance() {
		if(s==null) {
			s = new Splitter();
		}
		return s;
	}
public static void main(String[] args) throws Exception {
	Splitter s = Splitter.getInstance();
	
}
public ArrayList<Document> returnDocuments(){
	ArrayList<Document> results = new ArrayList<Document>();
	
	try {
		Splitter s = Splitter.getInstance();
		BufferedReader reader = new BufferedReader(new FileReader(new File(Utils.CRAN_PATH)));
		Document doc1 = new Document();
		char field = 'T';
		String str = reader.readLine();
		String index = str.substring(3);
		while((str = (reader.readLine()))!=null) {
			if(str.charAt(0)=='.') {
				
				switch(str.charAt(1)) {
				case 'I':
					doc1.add((new TextField("ID", index, Field.Store.YES)));
					results.add(doc1);
					doc1 = new Document();
					index = str.substring(3);
					break;
				default:
					field = str.charAt(1);
			}
		}
			else{
					if(field == 'T') {
						String n = (doc1.get("Title")==null)?"":doc1.get("Title");
						if(n=="") {
							doc1.add((new TextField("Title", str, Field.Store.YES)));
						}
						else {
							doc1.removeField("Title");
							doc1.add((new TextField("Title", n + " " +  str, Field.Store.YES)));
						}
					}
					if(field == 'A') {
						String n = (doc1.get("Author")==null)?"":doc1.get("Author");
						if(n=="") {
							doc1.add((new TextField("Author", str, Field.Store.NO)));
						}
						else {
							doc1.removeField("Author");
							doc1.add((new TextField("Author", n + " " +  str,  Field.Store.NO)));
						}
					}
					if(field == 'B') {
						String n = (doc1.get("Source")==null)?"":doc1.get("Source");
						if(n=="") {
							doc1.add((new TextField("Source", str, Field.Store.NO)));
						}
						else {
							doc1.removeField("Source");
							doc1.add((new TextField("Source", n + " " +  str, Field.Store.NO)));
						}
					}
					if(field == 'W') {
						String n = (doc1.get("Content")==null)?"":doc1.get("Content");
						if(n=="") {
							doc1.add((new TextField("Content", str, Field.Store.YES)));
						}
						else {
							doc1.removeField("Content");
							doc1.add((new TextField("Content", n + " " +  str, Field.Store.YES)));
						}
					}
				}
			}
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	return results;
}
}

