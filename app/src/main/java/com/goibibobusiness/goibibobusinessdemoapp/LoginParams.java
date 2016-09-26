package com.goibibobusiness.goibibobusinessdemoapp;

import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class LoginParams extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display fragment as main content
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }

    /** Making preference fragment */
    public static class SettingsFragment extends PreferenceFragment
            implements SharedPreferences.OnSharedPreferenceChangeListener{

        SharedPreferences sharedPreferences;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load preferences from XML resource
            addPreferencesFromResource(R.xml.preferences);
        }

        @Override
        public void onResume() {
            super.onResume();

            sharedPreferences = getPreferenceManager().getSharedPreferences();

            // we want to watch the preference values' changes
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);


            Map<String, ?> preferencesMap = sharedPreferences.getAll();
            // iterate through the preference entries and update their summary if they are an instance of EditTextPreference
            for (Map.Entry<String, ?> preferenceEntry : preferencesMap.entrySet()) {
//                Log.v("goibibobusinessdemoapp", "-----------" + preferenceEntry.getKey());
                Preference pref = findPreference(preferenceEntry.getKey());
                if (preferenceEntry.getKey().equals("time_stamp")) {
                    // Need to update timestamp
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date currenTimeZone = (Date) calendar.getTime();
                    String time_now = sdf.format(currenTimeZone);

                    // updating timestamp
                    EditTextPreference edit_pref = (EditTextPreference) pref;
                    pref.setSummary(time_now);
                    edit_pref.setText(time_now);
                } else if (pref instanceof EditTextPreference) {
                    EditTextPreference edit_pref = (EditTextPreference) pref;
                    pref.setSummary(edit_pref.getText());
                }
            }
        }

        @Override
        public void onPause() {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                              String key) {
//            Log.v("goibibobusinessdemoapp", "edit==============================");

            Preference pref = findPreference(key);

            if (pref instanceof EditTextPreference) {
                EditTextPreference edit_pref = (EditTextPreference) pref;
                pref.setSummary(edit_pref.getText());
            }

        }

        private void updateSummary(EditTextPreference preference) {
            // set the EditTextPreference's summary value to its current text
            preference.setSummary(preference.getText());
        }

    }


}
