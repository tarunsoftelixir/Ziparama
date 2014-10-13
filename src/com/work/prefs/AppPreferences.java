package com.work.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {

	private static final String SHARED_PREFERENCE_NAME = "SestoreSharedPrefernce";
	public static AppPreferences sAppPreference;
	private SharedPreferences mPreferences;
	private Editor mEditor;

	/**
	 * Enum for shared preferences keys to store various values
	 * 
	 * VARSHA NAMA <varsha@softelixir.com>
	 */
	enum SharedPreferncesKeys {
		isRegistered, isLastName, isGender, isEmail, isPicture, isFirstName, isDOB, isUId,isPId;
	}

	/**
	 * private constructor for singleton class
	 * 
	 * @param context
	 */
	private AppPreferences(Context context) {
		mPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);
		mEditor = mPreferences.edit();
	}

	/**
	 * 
	 * @param context
	 *            Context to pass for creating preferences
	 * @return AppPreferences returns reference of static AppPreferences
	 */
	public static AppPreferences getInstance(Context context) {
		if (sAppPreference == null)
			sAppPreference = new AppPreferences(context);
		return sAppPreference;
	}

	public boolean isRegistered() {
		return mPreferences.getBoolean(
				SharedPreferncesKeys.isRegistered.toString(), false);
	}

	public void setRegistered(boolean isRegistered) {
		mEditor.putBoolean(SharedPreferncesKeys.isRegistered.toString(),
				isRegistered);
		mEditor.commit();
	}

	public String isUId() {
		return mPreferences
				.getString(SharedPreferncesKeys.isUId.toString(), "");
	}

	
	public void setUid(String isUId) {
		mEditor.putString(SharedPreferncesKeys.isUId.toString(), isUId);
		mEditor.commit();
	}

	public String isFirstName() {
		return mPreferences.getString(
				SharedPreferncesKeys.isFirstName.toString(), "firstname");
	}

	public void setFirstName(String isFirstName) {
		mEditor.putString(SharedPreferncesKeys.isFirstName.toString(),
				isFirstName);
		mEditor.commit();
	}

	public String isLastName() {
		return mPreferences.getString(
				SharedPreferncesKeys.isLastName.toString(), "lastname");
	}

	public void setLastName(String isLastName) {
		mEditor.putString(SharedPreferncesKeys.isLastName.toString(),
				isLastName);
		mEditor.commit();
	}

	public String isDOB() {
		return mPreferences.getString(SharedPreferncesKeys.isDOB.toString(),
				"dob");
	}

	public void setDOB(String isDOB) {
		mEditor.putString(SharedPreferncesKeys.isDOB.toString(), isDOB);
		mEditor.commit();
	}

	public String isEmail() {
		return mPreferences.getString(SharedPreferncesKeys.isEmail.toString(),
				"email");
	}

	public void setEmail(String isEmail) {
		mEditor.putString(SharedPreferncesKeys.isEmail.toString(), isEmail);
		mEditor.commit();
	}

	public String isGender() {
		return mPreferences.getString(SharedPreferncesKeys.isGender.toString(),
				"gender");
	}

	public void setGender(String isGender) {
		mEditor.putString(SharedPreferncesKeys.isGender.toString(), isGender);
		mEditor.commit();
	}

	public String isPicture() {
		return mPreferences.getString(
				SharedPreferncesKeys.isPicture.toString(), "picture");
	}

	public void setPicture(String isPicture) {
		mEditor.putString(SharedPreferncesKeys.isPicture.toString(), isPicture);
		mEditor.commit();
	}
}
