package com.example.loginapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageHelper {

    private static final String PREF_NAME = "SavingslyPrefs";
    private static final String KEY_LANGUAGE = "app_language";

    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_FILIPINO = "fil";

    public static String getLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LANGUAGE, LANGUAGE_ENGLISH);
    }

    public static void setLanguage(Context context, String languageCode) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply();
        applyLanguage(context);
    }

    public static Context applyLanguage(Context context) {
        String lang = getLanguage(context);
        Locale locale = Locale.forLanguageTag(lang);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);

        return context.createConfigurationContext(config);
    }
}
