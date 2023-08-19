package com.sahil4.quotesapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.sahil4.quotesapp.models.MyPreference;
import com.sahil4.quotesapp.repositories.PreferenceRepository;

import java.util.List;

public class PreferencesViewModel extends AndroidViewModel {
    private final PreferenceRepository preferenceRepository;

    public PreferencesViewModel(@NonNull Application application) {
        super(application);
        preferenceRepository = new PreferenceRepository(application);
    }

    public List<MyPreference> getPreferences() {
        return preferenceRepository.getPreferences();
    }

    public void updatePreference(MyPreference preference) {
        preferenceRepository.updatePreference(preference);
    }

    public void savePreferences() {
        preferenceRepository.savePreferences();
    }
}
