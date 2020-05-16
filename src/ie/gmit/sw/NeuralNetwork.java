package ie.gmit.sw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationReLU;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationSoftMax;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.buffer.MemoryDataLoader;
import org.encog.ml.data.buffer.codec.CSVDataCODEC;
import org.encog.ml.data.buffer.codec.DataSetCODEC;
import org.encog.ml.data.folded.FoldedDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.cross.CrossValidationKFold;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.EncogUtility;

import ie.gmit.sw.runner.UserSettings;
import ie.gmit.sw.utils.Constants;
import ie.gmit.sw.utils.Utilities;

public class NeuralNetwork {

	/*
	 * *****************************************************************************
	 * ******** NB: READ THE FOLLOWING CAREFULLY AFTER COMPLETING THE TWO LABS ON
	 * ENCOG AND REVIEWING THE LECTURES ON BACKPROPAGATION AND MULTI-LAYER NEURAL
	 * NETWORKS! YOUR SHOULD ALSO RESTRUCTURE THIS CLASS AS IT IS ONLY INTENDED TO
	 * DEMO THE ESSENTIALS TO YOU.
	 * *****************************************************************************
	 * ********
	 * 
	 * The following demonstrates how to configure an Encog Neural Network and train
	 * it using backpropagation from data read from a CSV file. The CSV file should
	 * be structured like a 2D array of doubles with input + output number of
	 * columns. Assuming that the NN has two input neurons and two output neurons,
	 * then the CSV file should be structured like the following:
	 *
	 * -0.385,-0.231,0.0,1.0 -0.538,-0.538,1.0,0.0 -0.63,-0.259,1.0,0.0
	 * -0.091,-0.636,0.0,1.0
	 * 
	 * The each row consists of four columns. The first two columns will map to the
	 * input neurons and the last two columns to the output neurons. In the above
	 * example, rows 1 an 4 train the network with features to identify a category
	 * 2. Rows 2 and 3 contain features relating to category 1.
	 * 
	 * You can normalize the data using the Utils class either before or after
	 * writing to or reading from the CSV file.
	 */
	private Logger logger = Logger.getLogger(NeuralNetwork.class.getName());
	private static DecimalFormat df = new DecimalFormat("###.###");
	UserSettings settings = UserSettings.getInstance();
	int inputs = settings.getVectorSize(); // Change this to the number of input neurons
	int outputs = Constants.LANGUAGE_AMOUNT; // Change this to the number of output neurons
	int hiddenLayer = (int) calculateHiddenNeurons(inputs, outputs);

	int tpos = 0, fpos = 0, fneg = 0, tneg = 0;
	float correct = 0;
	float total = 0;
	float sensitivity = 0;
	float specificity = 0;

	public NeuralNetwork() {
	}

	public void createNetwork() {

		// Configure the neural network topology.
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(new ActivationTANH(), true, inputs)); // You need to figure out the activation
																				// function
		network.addLayer(new BasicLayer(new ActivationTANH(), true, hiddenLayer));

		network.addLayer(new BasicLayer(new ActivationSoftMax(), false, outputs));
		network.getStructure().finalizeStructure();
		network.reset();

		// Read the CSV file "data.csv" into memory. Encog expects your CSV file to have
		// input + output number of columns.
		DataSetCODEC dsc = new CSVDataCODEC(new File("data.csv"), CSVFormat.DECIMAL_POINT, false, inputs, outputs,
				false);
		MemoryDataLoader mdl = new MemoryDataLoader(dsc);
		MLDataSet trainingSet = mdl.external2Memory();

		FoldedDataSet folded = new FoldedDataSet(trainingSet);
		MLTrain train = new ResilientPropagation(network, folded);
		CrossValidationKFold cv = new CrossValidationKFold(train, 5);

		// Use backpropagation training with alpha=0.1 and momentum=0.2
		//Backpropagation trainer = new Backpropagation(network, trainingSet, 0.1,0.2);

		long startTime = System.nanoTime();
		// Train the neural network

		int epoch = 1; // Use this to track the number of epochs
		do {
			cv.iteration();
			System.out.println(cv.getError());

			epoch++;

		} while (cv.getError() > 0.01);
		cv.finishTraining();
		
		long stopTime = System.nanoTime();
		long elapsed = stopTime - startTime;
		long convert = TimeUnit.SECONDS.convert(elapsed, TimeUnit.NANOSECONDS);
		System.out.println("[INFO] Training finish in" + convert + ": Seconds with error "+ cv.getError());

		
		float correct = 0, total = 0;
		ConfusionMatrix cMatrix = new ConfusionMatrix();
		for (MLDataPair pair : trainingSet) {
			total++;
			MLData output = network.compute(pair.getInput());
			int Y = (int) Math.round(output.getData(0));
			int Yd= (int) pair.getIdeal().getData(0);
			
			if(Y==1) {
				if(Y==Yd) {
					cMatrix.setTruePositive();
				}
				else cMatrix.setFalseNegative();
			}
			if(Y==0) {
				if(Y==Yd) {
					cMatrix.setTrueNegative();
				}
				else {
					cMatrix.setFalsePositive();
				}
			}
	
		}
		
		
		System.out.println(cMatrix.toString());
		
		try {
			FileWriter csvWriter = new FileWriter("training.csv", true);
			csvWriter.append(Constants.NEW_LINE_SEPARATOR);
			csvWriter.append(String.valueOf(epoch) + Constants.COMMA_DELIMITER);
			csvWriter.append(String.valueOf(inputs) + Constants.COMMA_DELIMITER);
			csvWriter.append(String.valueOf(hiddenLayer) + Constants.COMMA_DELIMITER);
			csvWriter.append(null + Constants.COMMA_DELIMITER);
			csvWriter.append(null + Constants.COMMA_DELIMITER);
			csvWriter.append(null + Constants.COMMA_DELIMITER);
			csvWriter.append(String.valueOf(cv.getError()) + Constants.COMMA_DELIMITER);
			csvWriter.append(String.valueOf(convert) + Constants.COMMA_DELIMITER);
			csvWriter.append(String.valueOf(cMatrix.getAccuracy()) + Constants.COMMA_DELIMITER);
			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Error Reading to File.");
		}
		Utilities.saveNeuralNetwork(network, "./testnetwork.nn");

		Encog.getInstance().shutdown();
	}

	public Language predict(double[] userNormalised) throws IOException {

		BasicNetwork n = Utilities.loadNeuralNetwork("testnetwork.nn");
		MLData input = new BasicMLData(userNormalised);
		MLData out = n.compute(input);

		double best = 0;
		double next = 0;
		int bestIndex = 0;
		System.out.println();
		System.out.print(out.getData());
		
		FileWriter outwriter = new FileWriter("outputs.csv", true);
		outwriter.append(Constants.NEW_LINE_SEPARATOR);
		for (int i = 0; i < out.getData().length; i++) {
			outwriter.append(out.getData(i) + Constants.COMMA_DELIMITER);
		}
		outwriter.flush();
		outwriter.close();
		for (int i = 0; i < Constants.LANGUAGE_AMOUNT; i++) {
				next = out.getData(i);
			if (next > best) {
				best = next;
				System.out.println("i : ["+i+"] best: "+ best);
				bestIndex = i;
			}
		}

		Language prediction = null;
		Language[] langs = Language.values(); // Only call this once...
		for (int i = 0; i < langs.length; i++) {
			if (bestIndex == i)
				prediction = langs[i];
		}
		System.out.println(bestIndex);
		System.out.println(prediction);
		return prediction;
	}

	/**
	 * A guideline for choosing the number of hidden neurons, is the geometric
	 * pyramid rule. It means that the number of neurons follows a pyramid type
	 * structure, decreasing from input to output.
	 * 
	 * To calculate the number of hidden neurons for the hidden-layer, we can
	 * calculate sqrt(mn) where n is inputs and m is outputs.
	 * 
	 * @param in : the number of input neurons
	 * @param on : the number of output neurons
	 * @return number of hidden-layer neurons.
	 */
	public double calculateHiddenNeurons(int in, int on) {
		return Math.sqrt(in * on);
	}
}