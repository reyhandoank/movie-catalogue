package com.example.mymoviecatalogue.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.service.DailyReminder;
import com.example.mymoviecatalogue.service.ReleaseReminder;

import java.util.Objects;

public class SettingFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private ReleaseReminder releaseReminder;

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals(getResources().getString(R.string.key_locale_setting))) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(androidx.preference.Preference preference, Object newValue) {
        String key = preference.getKey();
        Boolean isOn = (Boolean) newValue;

        DailyReminder dailyReminder = new DailyReminder();

        if(key.equals(getResources().getString(R.string.key_daily_reminder))) {
            if (isOn) {
                dailyReminder.setRepeatingAlarm(Objects.requireNonNull(getContext()));
            } else {
                dailyReminder.cancelAlarm(Objects.requireNonNull(getContext()));
            }
        } else {
            if (isOn) {
                releaseReminder.setRepeatingAlarm(Objects.requireNonNull(getContext()));
            } else {
                releaseReminder.cancelAlarm(Objects.requireNonNull(getContext()));
            }
        }
        return true;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onSaveInstanceState(savedInstanceState);
        setPreferencesFromResource(R.xml.setting_pref, rootKey);

        releaseReminder = new ReleaseReminder();

        SwitchPreference dailyReminder = findPreference(getResources().getString(R.string.key_daily_reminder));
        assert dailyReminder != null;
        dailyReminder.setOnPreferenceChangeListener(this);
        SwitchPreference upcomingReminder = findPreference(getResources().getString(R.string.key_release_reminder));
        assert upcomingReminder != null;
        upcomingReminder.setOnPreferenceChangeListener(this);
        findPreference(getResources().getString(R.string.key_locale_setting)).setOnPreferenceClickListener(this);
    }
}
