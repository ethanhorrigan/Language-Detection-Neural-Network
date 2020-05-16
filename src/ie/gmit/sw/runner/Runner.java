package ie.gmit.sw.runner;

import java.io.FileWriter;
import java.util.Scanner;

import ie.gmit.sw.Language;
import ie.gmit.sw.NeuralNetwork;
import ie.gmit.sw.VectorProcessor;
import ie.gmit.sw.utils.Constants;

public class Runner {
	

	public static void main(String[] args) throws Exception {
		UserSettings settings = UserSettings.getInstance();
		
		//int n = 
		/*			
			Each of the languages in the enum Language can be represented as a number between 0 and 234. You can 
			map the output of the neural network and the training data label to / from the language using the
			following. Eg. index 0 maps to Achinese, i.e. langs[0].  
		*/
		Scanner scanner = new Scanner(System.in);
		System.out.println("Suggested ngram size: 2 or 3");
		System.out.println("Suggested Vector Size between: 100 to 1000");
		System.out.println("Enter NGram Size: ");
	    settings.setNgram(scanner.nextInt());
		System.out.println("Enter Vector Size: ");
		settings.setVectorSize(scanner.nextInt());
		int option = showMenu();
		do {

		    switch (option) {

		        case 1:
		    		new VectorProcessor().go(true);
		    		new NeuralNetwork().createNetwork();
		            break;
		        case 2:
		        	Scanner scanner2 = new Scanner(System.in);
		        	System.out.println("Enter Text for prediction: ");
		        	String q = scanner2.nextLine();
		        	settings.setQuery(q);
		        	settings.setNormalisedQuery(new VectorProcessor().processQuery(settings.getQuery()));
		        	new NeuralNetwork().predict(settings.getNormalisedQuery());
		        
		            break;
		        case 3:
		            System.exit(0);
		            scanner.close();
		        default:
		            System.out.println("Sorry, please enter valid Option");
		            showMenu();
		    }// End of switch statement
		    option = showMenu();//SHOWS THE MENU AGAIN
		} while (option != 3);
		System.out.println("Thank you. Good Bye.");



		/*
		FileWriter csvWriter = new FileWriter("training.csv");
		csvWriter.append("Epoch");
		csvWriter.append(Constants.COMMA_DELIMITER);
		csvWriter.append("Inputs");
		csvWriter.append(Constants.COMMA_DELIMITER);
		csvWriter.append("Hidden Layer 1 Neurons");
		csvWriter.append(Constants.COMMA_DELIMITER);
		csvWriter.append("Hidden Layer 2 Neurons");
		csvWriter.append(Constants.COMMA_DELIMITER);
		csvWriter.append("Hidden Layer 3 Neurons");
		csvWriter.append(Constants.COMMA_DELIMITER);
		csvWriter.append("Hidden Layer 4 Neurons");
		csvWriter.append(Constants.COMMA_DELIMITER);
		csvWriter.append("Average Error");
		csvWriter.append(Constants.COMMA_DELIMITER);
		csvWriter.append("Time");
		csvWriter.append(Constants.COMMA_DELIMITER);
		csvWriter.append("Accuracy");
		csvWriter.append(Constants.COMMA_DELIMITER);
		
		csvWriter.flush();
		csvWriter.close();
		*/
		showMenu();
	}
	
	private static int showMenu() {
		int option = 0;
		Scanner scanner = new Scanner(System.in);
		System.out.println(Constants.HORIZONTAL_LINE);
		System.out.println(Constants.TITLE);
		System.out.println(Constants.HORIZONTAL_LINE);
		System.out.println("Please Enter an Option:");
		System.out.println("[1] Train Model [2] Predict [3] Exit");

		option = scanner.nextInt();
		return option;
	}
}