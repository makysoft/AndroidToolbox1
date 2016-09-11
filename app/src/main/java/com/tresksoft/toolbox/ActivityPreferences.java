package com.tresksoft.toolbox;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ActivityPreferences extends PreferenceActivity{
	
	final String SHARED_PREFERENCES = "myCustomSharedPrefs";
	// Google Play
	//final String URI_MARKET = "market://details?id=com.tresksoft.toolbox";
	// Market Amazon
	final String URI_MARKET = "http://www.amazon.com/gp/mas/dl/android?p=com.tresksoft.toolbox";

	String listProcess;
	String listApplication;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		Preference customPref = null;
		
		/*
		Preference customPref = (Preference) findPreference("listProcess");
		customPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener () {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				SharedPreferences customSharedPreference = getSharedPreferences(SHARED_PREFERENCES, Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = customSharedPreference.edit();
				editor.putString("listProcess", (String) newValue);
				editor.commit();
				return true;
			}
		});
		
		customPref = (Preference) findPreference("listApplication");
		customPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				SharedPreferences customSharedPreference = getSharedPreferences(SHARED_PREFERENCES, Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = customSharedPreference.edit();
				editor.putString("listApplication", (String) newValue);
				editor.commit();				
				return false;
			}
		});
		*/
		
		customPref = (Preference) findPreference("about");
		customPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			
			public boolean onPreferenceClick(Preference preference) {
				mostrarDialogAcerca();
				return true;
			}
		});
		
		customPref = (Preference) findPreference("market");
		customPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			
			public boolean onPreferenceClick(Preference preference) {
				 handlePuntuar();
				 return true;
			}
		});
		
		customPref = (Preference) findPreference("send_email");
		customPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			
			public boolean onPreferenceClick(Preference preference) {
				handleEmail();
				return true;
			}
		});
		
	}
	
	private void handleEmail() {
		Email email = new Email();
		
		email.sendEmail(this, "salvalc@gmail.com", getResources().getString(R.string.lbl_send_suggestion), getResources().getString(R.string.lbl_write_suggestion));
	}	
	
	protected void onStart() {
		super.onStart();
		getPrefs();
	}
	
	private void getPrefs() {
		//SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES, Activity.MODE_PRIVATE);
		//((ListPreference) findPreference("listProcess")).setValue(prefs.getString("listProcess", "1"));
		//((ListPreference) findPreference("listApplication")).setValue(prefs.getString("listApplication", "1"));
	}
	
	private void handlePuntuar() {
		Intent updateIntent = new Intent(Intent.ACTION_VIEW);
		updateIntent.setData(Uri.parse(URI_MARKET));
		startActivity(updateIntent);
	}
	
	private void mostrarDialogAcerca() {
		final Dialog dialogAcerca;
		
		dialogAcerca = new Dialog(this);
		dialogAcerca.setContentView(R.layout.custom_dialog_acerca);
		dialogAcerca.setTitle(getResources().getText(R.string.acerca));
		Button bot_cancelar = (Button) dialogAcerca.findViewById(R.id.bot_cerrar);
		
		bot_cancelar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialogAcerca.dismiss();
			}
		});
		
		dialogAcerca.show();
	}
	
}
