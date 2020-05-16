package ie.gmit.sw;

import ie.gmit.sw.runner.UserSettings;
import ie.gmit.sw.utils.Constants;

/**
 * Vectorise converts a language into its vector equivalent
 * @author Ethan Horrigan
 *
 */
public class Vectorise {
	
	static UserSettings settings = UserSettings.getInstance();

	/**
	 * Converts a given language to its vector equivalent.
	 * 
	 * Example: The language vector for Swedish 1[0.0], 2[0.0]....193[1.0]
	 * 
	 * @param lang : the language subject for vector conversion.
	 * @return language as a vector
	 */
	public static double[] toLanguageVector(Language lang) {	
		double[] lanuages = new double[Language.values().length];
		lanuages[settings.getLangMap().get(lang)] = Constants.IDEAL_LANGAUGE;
		return lanuages;
	}
	
	/*
	public static double[] toLanguageVector(Language lang) {
		settings.getLangMap().get(lang);
		double[] lanuages = new double[Language.values().length];
		
		Language[] langs = Language.values();
		for (int i = 0; i < langs.length; i++){
			if(lang == langs[i]) lanuages[i] = 1.0;
		}
		return lanuages;
	}
	*/
}
