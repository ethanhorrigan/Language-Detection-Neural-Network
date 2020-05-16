package ie.gmit.sw;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ie.gmit.sw.utils.Constants;

public class CSVHandler {
	private File csvFile = new File(Constants.CSV_DATA);

	public void processCsv() {
		
		/**
		 * If the CSV does not exist, create a new csv file.
		 */
		if(!csvFile.exists()) {
			try {
				BufferedWriter fileWriter = new BufferedWriter(new FileWriter(Constants.CSV_DATA));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
	}
}
