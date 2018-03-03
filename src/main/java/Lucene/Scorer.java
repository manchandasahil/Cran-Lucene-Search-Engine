package Lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Scorer {
	private float map = -1;
	private float recall = -1;
	private File reference;
	private File results;
	HashMap<Integer,ArrayList<Integer>> referenceMap;
	HashMap<Integer,ArrayList<Integer>> resultsMap;
	
	
	public void init(){
		reference = new File("cranqrel_mod");
		results = new File("cran.results");
	}
	
	public void score() {
		try {
			init();
			BufferedReader referencebr = new BufferedReader(new FileReader(reference));
			BufferedReader resultsbr = new BufferedReader(new FileReader(results));
			
			//prepare reference Hashmap
			referenceMap = new HashMap<Integer,ArrayList<Integer>>();
			String str =null;
			while((str = referencebr.readLine())!=null) {
				String[] row = str.split(" +");
				int query = Integer.parseInt(row[0]);
				if(!referenceMap.containsKey(query)) {
					referenceMap.put(query,new ArrayList<Integer>());
				}
				referenceMap.get(query).add(Integer.parseInt(row[2]));
			}
			
			//prepare results hashmap
			resultsMap = new HashMap<Integer,ArrayList<Integer>>();
			str =null;
			while((str = resultsbr.readLine())!=null) {
				String[] row = str.split(" ");
				int query = Integer.parseInt(row[0]);
				if(!resultsMap.containsKey(query)) {
					resultsMap.put(query,new ArrayList<Integer>());
				}
				resultsMap.get(query).add(Integer.parseInt(row[2]));
			}
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public float calculatePrecision(){
		HashMap<Integer,Float> precision = new HashMap<Integer,Float>();
		float relevantdocs = 0;
		float totaldocs = Utils.DOMAIN_SIZE;
		for(int i : resultsMap.keySet()) {
			if(!referenceMap.containsKey(i)) {
				//System.out.println("results map does not contain " + i);
				continue;
			}
			relevantdocs = 0;
			for(int j:resultsMap.get(i)) {
				
				if(referenceMap.get(i).contains(j)) {
					relevantdocs++;
				}
			}
			precision.put(i,(float) (relevantdocs/totaldocs));
		}
		float x = 0;
		for(int i: precision.keySet()) {
			x += precision.get(i);
		}
		map = x/precision.size();
		return map;
	}
	
	public float calculateRecall(){
		HashMap<Integer,Float> recallmap = new HashMap<Integer,Float>();
		float relevantdocs = 0;
		for(int i : resultsMap.keySet()) {
			if(!referenceMap.containsKey(i)) {
				continue;
			}
			relevantdocs = 0;
			for(int j:resultsMap.get(i)) {
				
				if(referenceMap.get(i).contains(j)) {
					relevantdocs++;
				}
			}
			recallmap.put(i,(float) (relevantdocs/referenceMap.size()));
		}
		float x = 0;
		for(int i: recallmap.keySet()) {
			x += recallmap.get(i);
		}
		recall = (float) x/recallmap.size();
		return recall;
	}
	
}

