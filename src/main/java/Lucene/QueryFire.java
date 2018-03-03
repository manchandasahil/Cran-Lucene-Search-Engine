package Lucene;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class QueryFire {
	ArrayList<String> queries = new ArrayList<String>();
	public static void main(String[] args) {
		QueryFire qf = new QueryFire();
		qf.readQueries();
		System.out.println(qf.queries.get(1));
	}
	
	public HashMap<Integer,String> readQueries() {
		HashMap<Integer,String> w = new HashMap<Integer,String>();
		File file = new File("cran.qry");
		try {
			String text = String.join(" ", Files.readAllLines(Paths.get("cran.qry")));
            text = text.replace("?", "");
            String lines[] = text.split("\\.I.*?.W");
            //System.out.println(lines[4]);
            for(int i =0;i<lines.length;i++) {
            	w.put(i,lines[i]);
            }
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return w;
	}
	
	
}
