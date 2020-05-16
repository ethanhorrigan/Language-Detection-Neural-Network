package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import ie.gmit.sw.ngram.Ngram;
import ie.gmit.sw.runner.UserSettings;
import ie.gmit.sw.utils.Constants;
import ie.gmit.sw.utils.Utilities;

/**
 * VectorProcessor handles preprocessing of data to be used in training the
 * neural network.
 * 
 * @author Ethan Horrigan
 *
 */
public class VectorProcessor {
	static UserSettings settings = UserSettings.getInstance();
	private Logger logger = Logger.getLogger(VectorProcessor.class.getName());
	private static double[] vector = new double[100];
	private static double[] langVector = new double[Language.values().length];
	private static double[] queryVector = new double[settings.getVectorSize()];
	private static DecimalFormat df = new DecimalFormat("###.###");
	private static DecimalFormat dfl = new DecimalFormat("#.#");
	private HashMap<String, Integer> processedLangs = new HashMap<String, Integer>();
	private static int n = 3;
	private static File dataCsv = new File(Constants.CSV_DATA);
	private FileWriter csvWriter;
	private FileWriter dataPro;

	public VectorProcessor() throws IOException {
	}

	public void go(boolean s) throws Exception {

		if (s)
			if (dataCsv.exists())
				dataCsv.delete();

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(Constants.DATA))))) {
			String line = null;

			while ((line = br.readLine()) != null) {
				if (s)
					process(line);
				if (!s)
					processQuery(line);
			}
			if (!s) {
				settings.setNormalisedQuery(queryVector);
				// new NeuralNetwork().predict(settings.getNormalisedQuery());
			}
			

			System.out.println(processedLangs);
		} catch (Exception e) {
			System.out.println("error reading lines");
		}

	}

	public void process(String line) throws IOException {
		String[] record = line.split("@");
		if (record.length > 2) return;
		
		if(processedLangs.containsKey(record[1])) {
			processedLangs.put(record[1], processedLangs.get(record[1]) + 1);
		}
		else {
			processedLangs.put(record[1], 1);
		}
		
		String text = record[0].toLowerCase();
		String lang = record[1];

		
		
		
		/*
		dataPro = new FileWriter("testdata.txt", true);
		dataPro.append(text);
		dataPro.flush();
		dataPro.close();
		*/
		/**
		 * If the language is not in the enum, return.
		 */
		if(!Language.isInEnum(lang, Language.class)) return;
		
		/**
		 * Initialize the vector
		 */
		for (int i = 0; i < vector.length; i++) vector[i] = 0;

		
		Collection<String> ngrams = Ngram.ngrams(n, text);
		for (String ngram : ngrams) {
			vector[ngram.hashCode() % vector.length]++;
		}

		// normalize the data.
		vector = Utilities.normalize(vector, -1.0, 1.0);
		langVector = Vectorise.toLanguageVector(Language.valueOf(lang));
		

		try {
			csvWriter = new FileWriter(Constants.CSV_DATA, true);


			for (int i = 0; i < vector.length; i++) {
				String input = df.format(vector[i]) + Constants.COMMA_DELIMITER;
				csvWriter.append(input);
			}
			
			for (int j = 0; j < langVector.length; j++) {
				csvWriter.append(dfl.format(langVector[j]) + Constants.COMMA_DELIMITER);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error: Writing to data.csv");
		} finally {
			csvWriter.append(Constants.NEW_LINE_SEPARATOR);
			csvWriter.flush();
			csvWriter.close();
		}
	}

	public double[] processQuery(String line) throws IOException {
		for (int i = 0; i < queryVector.length; i++)
			queryVector[i] = 0;
		String text = line.toLowerCase();

		Collection<String> ngrams = Ngram.ngrams(n, text);
		for (String ngram : ngrams) {
			queryVector[ngram.hashCode() % queryVector.length]++;
		}

		queryVector = Utilities.normalize(queryVector, -1.0, 1.0);

		return queryVector;
	}
	
	public static void main(String[] args) throws IOException, Exception {
		new VectorProcessor().go(true);
	}
	

}
