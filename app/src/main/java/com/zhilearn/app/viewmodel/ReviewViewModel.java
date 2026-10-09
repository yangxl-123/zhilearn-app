package com.zhilearn.app.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zhilearn.app.data.AppDatabase;
import com.zhilearn.app.data.CharacterDao;
import com.zhilearn.app.model.CharacterData;

import java.util.List;

public class ReviewViewModel extends AndroidViewModel {

    private CharacterDao characterDao;
    private MutableLiveData<List<CharacterData>> dueForReview = new MutableLiveData<>();

    public ReviewViewModel(@NonNull Application application) {
        super(application);
        characterDao = AppDatabase.getInstance(application).characterDao();
        loadDueForReview();
    }

    private void loadDueForReview() {
        new AsyncTask<Void, Void, List<CharacterData>>() {
            @Override
            protected List<CharacterData> doInBackground(Void... voids) {
                long now = System.currentTimeMillis();
                return characterDao.getDueForReview(now);
            }

            @Override
            protected void onPostExecute(List<CharacterData> characters) {
                dueForReview.setValue(characters);
            }
        }.execute();
    }

    public void refresh() {
        loadDueForReview();
    }

    public LiveData<List<CharacterData>> getDueForReview() {
        return dueForReview;
    }

    public void markAsReviewed(String character, boolean remembered) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                String ch = params[0];
                boolean remembered = Boolean.parseBoolean(params[1]);

                CharacterData cd = characterDao.getCharacter(ch);
                if (cd != null) {
                    long now = System.currentTimeMillis();
                    float newMastery;
                    long interval;

                    if (remembered) {
                        // 记住了，掌握度提升，间隔拉长
                        newMastery = Math.min(cd.getMasteryLevel() + 0.15f, 1.0f);
                        interval = calculateInterval(cd.getReviewCount());
                    } else {
                        // 没记住，掌握度下降，间隔缩短
                        newMastery = Math.max(cd.getMasteryLevel() - 0.1f, 0.05f);
                        interval = 1; // 明天再复习
                    }

                    long next = now + interval * 24 * 60 * 60 * 1000L;
                    characterDao.updateMastery(ch, newMastery, now, next);
                }
                return null;
            }

            private long calculateInterval(int reviewCount) {
                switch (reviewCount) {
                    case 0: return 1;
                    case 1: return 3;
                    case 2: return 7;
                    case 3: return 15;
                    default: return 30;
                }
            }
        }.execute(character, String.valueOf(remembered));
    }
}
