package ie.gmit.sw.runner;

import java.util.*;

import ie.gmit.sw.Language;
import ie.gmit.sw.utils.Constants;

/**
 * UserSettings Singleton: Stores the users input and language EnumMap.
 * 
 * Instead of using a loop each time we need to map the ideal language to a vector,
 * we can use an {@code EnumMap<Language, Integer>} to store the language and its value.
 * Therefore, we can just reference the EnumMap with a given language to find its
 * index to be mapped to the data vector.
 * 
 * @author Ethan Horrigan
 *
 */
public class UserSettings {

	private static UserSettings instance = null;
	private Map<Language, Integer> langMap = new EnumMap<>(Language.class);
	private int ngram;
	private int vectorSize;
	private String query;
	private Language[] langs = Language.values();
	private double[] normalisedQuery = new double[this.vectorSize];
	
	/**
	 * Singleton Constructor
	 * 
	 * Initialise the Lanuage EnumMap.
	 */
	private UserSettings() {
		for (int i = 0; i < Constants.LANGUAGE_AMOUNT; i++) langMap.put(langs[i], i);
	}
	
	/**
	 * @return instance of UserSettings
	 */
	public static UserSettings getInstance() {
		if(instance == null) instance = new UserSettings();
		return instance;
	}

	public Map<Language, Integer> getLangMap() {
		return langMap;
	}

	public void setLangMap(Map<Language, Integer> langMap) {
		this.langMap = langMap;
	}

	public int getNgram() {
		return ngram;
	}

	public void setNgram(int ngram) {
		this.ngram = ngram;
	}

	public int getVectorSize() {
		return vectorSize;
	}

	public void setVectorSize(int vectorSize) {
		this.vectorSize = vectorSize;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public double[] getNormalisedQuery() {
		for (int i = 0; i < normalisedQuery.length; i++) {
			System.out.println(normalisedQuery[i]);
		}
		return normalisedQuery;
	}

	public void setNormalisedQuery(double[] normalisedQuery) {
		this.normalisedQuery = normalisedQuery;
	}
}
