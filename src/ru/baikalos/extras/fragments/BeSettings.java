/*
 * Copyright (C) 2017 AICP
 * Copyleft      2018 BaikalOS
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package ru.baikalos.extras.fragments;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v14.preference.SwitchPreference;
import android.widget.Toast;

import ru.baikalos.extras.BaseSettingsFragment;
import ru.baikalos.extras.Constants;
import ru.baikalos.extras.LauncherActivity;
import ru.baikalos.extras.R;
import ru.baikalos.extras.utils.Util;

public class BeSettings extends BaseSettingsFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String PREF_THEME = "be_theme";

    private static final String PREF_BE_LAUNCHER = "be_launcher_enabled";

    private ComponentName mBeLauncherComponent;

    private ListPreference mTheme;
    private SwitchPreference mBeLauncher;

    @Override
    protected int getPreferenceResource() {
        return R.xml.be_settings;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PackageManager pm = getContext().getPackageManager();
        mBeLauncherComponent = new ComponentName(getContext(),
            LauncherActivity.class);

        mTheme = (ListPreference) findPreference(PREF_THEME);
        mTheme.setOnPreferenceChangeListener(this);
        mBeLauncher = (SwitchPreference) findPreference(PREF_BE_LAUNCHER);
        mBeLauncher.setChecked(pm.getComponentEnabledSetting(mBeLauncherComponent) !=
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
        mBeLauncher.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Util.setSummaryToValue(mTheme);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mTheme) {
            Util.setSummaryToValue(mTheme, newValue);
            if (!mTheme.getValue().equals(newValue)) {
                getActivity().recreate();
            }
            return true;
        } else if (preference == mBeLauncher) {
            setBeLauncherEnabled((Boolean) newValue);
            return true;
        } else {
            return false;
        }
    }

    private void setBeLauncherEnabled(boolean enabled) {
        PackageManager pm = getContext().getPackageManager();
        pm.setComponentEnabledSetting(mBeLauncherComponent, enabled
                ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                : PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        Toast.makeText(getContext(), R.string.be_launcher_enabled_update, Toast.LENGTH_LONG)
                .show();
    }
}
