package com.zhilearn.app.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zhilearn.app.data.AppDatabase;
import com.zhilearn.app.data.CharacterDao;
import com.zhilearn.app.model.CharacterData;
import com.zhilearn.app.utils.TtsHelper;

import java.util.List;

public class LearnViewModel extends AndroidViewModel {

    private static final String TAG = "LearnViewModel";
    private CharacterDao characterDao;
    private MutableLiveData<List<CharacterData>> unlearnedCharacters = new MutableLiveData<>();
    private MutableLiveData<CharacterData> currentCharacter = new MutableLiveData<>();
    private MutableLiveData<Boolean> learnComplete = new MutableLiveData<>(false);
    private int currentIndex = 0;

    public LearnViewModel(@NonNull Application application) {
        super(application);
        characterDao = AppDatabase.getInstance(application).characterDao();
        loadUnlearnedCharacters();
    }

    private void loadUnlearnedCharacters() {
        new AsyncTask<Void, Void, List<CharacterData>>() {
            @Override
            protected List<CharacterData> doInBackground(Void... voids) {
                return characterDao.getUnlearnedCharacters(10);
            }

            @Override
            protected void onPostExecute(List<CharacterData> characters) {
                unlearnedCharacters.setValue(characters);
                if (characters != null && characters.size() > 0) {
                    currentCharacter.setValue(characters.get(0));
                }
            }
        }.execute();
    }

    public LiveData<List<CharacterData>> getUnlearnedCharacters() {
        return unlearnedCharacters;
    }

    public LiveData<CharacterData> getCurrentCharacter() {
        return currentCharacter;
    }

    public LiveData<Boolean> isLearnComplete() {
        return learnComplete;
    }

    public void moveToNext() {
        List<CharacterData> list = unlearnedCharacters.getValue();
        if (list == null || list.isEmpty()) return;

        currentIndex++;
        if (currentIndex < list.size()) {
            currentCharacter.setValue(list.get(currentIndex));
        } else {
            learnComplete.setValue(true);
        }
    }

    public void saveLearningResult(String character, float writingScore, float practiceScore) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                String ch = params[0];
                float wScore = Float.parseFloat(params[1]);
                float pScore = Float.parseFloat(params[2]);

                CharacterData cd = characterDao.getCharacter(ch);
                if (cd != null) {
                    float avgScore = (wScore + pScore) / 2f / 100f; // 转成0-1
                    long now = System.currentTimeMillis();
                    long next = now + 24 * 60 * 60 * 1000; // 1天后复习

                    if (!cd.isLearned()) {
                        characterDao.markAsLearned(ch);
                    }
                    characterDao.updateMastery(ch, avgScore, now, next);
                }
                return null;
            }
        }.execute(character, String.valueOf(writingScore), String.valueOf(practiceScore));
    }

    public void speakCurrent() {
        CharacterData ch = currentCharacter.getValue();
        if (ch != null) {
            TtsHelper.getInstance(getApplication()).speak(ch.getCharacter());
        }
    }
}
