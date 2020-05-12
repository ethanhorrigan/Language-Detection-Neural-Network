package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
public class VectorProcessor {
	private double[] vector = new double[100];
	private DecimalFormat df = new DecimalFormat("###.###");
	private int n = 4;
	private Language[] languages;

	
	public void go() throws Exception {
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("filename"))))) {
			String line = null;
			
			while(br.readLine() != null) {
				process(line);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void process(String line) throws Exception {
		String[] record = line.split("@");
		
		if (record.length > 2) return;
		
		String text = record[0].toLowerCase();
		String lang = record[1];
		
		for (int i = 0; i < vector.length; i++) vector[i] = 0;
		
		// Loop over text
		// for each n-grame
			// compute index = n-gram.hashcode() % vector.length
		//vector[index]++;
		
		// normalize the data.
		Utilities.normalize(vector, -1, 1);
		
		//write out the vector to a CSV file using df.format(number) for each vector index..
	}
}
