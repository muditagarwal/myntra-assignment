package com.assignment.myntra.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by muditagarwal on 30/01/17.
 */

public class PreferencesManager {

	private static final String PREF_BEST_SCORE = "BEST_SCORE";

	private SharedPreferences sharedPreferences;

	private static PreferencesManager preferencesManager;

	private PreferencesManager(Context context) {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
	}

	public static PreferencesManager getInstance(Context context) {
		if (preferencesManager == null) {
			preferencesManager = new PreferencesManager(context);
		}
		return preferencesManager;
	}

	public int getBestScore() {
		return sharedPreferences.getInt(PREF_BEST_SCORE, -1);
	}

	public void setBestScore(int newScore) {
		int oldBestScore = getBestScore();
		//aditional check for default value -1
		if (oldBestScore == -1 || oldBestScore > newScore) {
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putInt(PREF_BEST_SCORE, newScore);
			editor.commit();
		}
	}
}
