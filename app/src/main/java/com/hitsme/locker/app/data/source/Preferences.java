package com.hitsme.locker.app.data.source;

import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.hitsme.locker.app.Constants;
import com.hitsme.locker.app.LockApplication;

import java.io.File;

/**
 * Created by 10093 on 2017/4/15.
 */

public class Preferences {
    public static final String DEFAULT_VAULTS_DIRECTORY= Environment.getExternalStorageDirectory().toString() + File.separator +"Lock" + File.separator + "Vaults";
    public static final String DEFAULT_EXTRACT_DIRECTORY= Environment.getExternalStorageDirectory().toString()  + File.separator +"Lock" + File.separator + "Unlocked";

    public static final boolean DEFAULT_IS_GRID_VIEW_IN_VAULT=false;

    public static void savePreference(String preference, Boolean value){
        SharedPreferences settings  =PreferenceManager.getDefaultSharedPreferences(LockApplication.getAppContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(preference, value);
        editor.commit();
    }


    public static String getPreference(String preference, String defaultValue) {
        SharedPreferences settings  =PreferenceManager.getDefaultSharedPreferences(LockApplication.getAppContext());
        return settings.getString(preference, defaultValue);
    }

    public static Boolean getPreference(String preference, Boolean defaultValue) {
        SharedPreferences settings  =PreferenceManager.getDefaultSharedPreferences(LockApplication.getAppContext());
        return settings.getBoolean(preference, defaultValue);
    }

    public static String getDefaultVaultDirectory(){
        return getPreference(Constants.Preferences.PREFERENCE_VAULT_DIRECTORY, DEFAULT_VAULTS_DIRECTORY);
    }

    public static String getDefaultUnlockDirectory(){
        return getPreference(Constants.Preferences.PREFERENCE_EXTRACT_DIRECTORY, DEFAULT_EXTRACT_DIRECTORY);
    }
    public static Boolean isGridViewInVault(){
        return getPreference(Constants.Preferences.PREFERENCE_IS_GRID_VIEW_IN_VAULT, DEFAULT_IS_GRID_VIEW_IN_VAULT);
    }
    public static void saveGridViewInVault(Boolean value){
        savePreference(Constants.Preferences.PREFERENCE_IS_GRID_VIEW_IN_VAULT, value);
    }
}
