package com.zhilearn.app.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zhilearn.app.data.AppDatabase;
import com.zhilearn.app.data.CharacterDao;

public class StatsViewModel extends AndroidViewModel {

    private CharacterDao characterDao;
    private MutableLiveData<Integer> totalCount = new MutableLiveData<>(0);
    private MutableLiveData<Integer> learnedCount = new MutableLiveData<>(0);
    private MutableLiveData<Float> averageMastery = new MutableLiveData<>(0f);

    public StatsViewModel(@NonNull Application application) {
        super(application);
        characterDao = AppDatabase.getInstance(application).characterDao();
        loadStats();
    }

    private void loadStats() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                int total = characterDao.getTotalCount();
                int learned = characterDao.getLearnedCount();
                Float avg = characterDao.getAverageMastery();
                if (avg == null) avg = 0f;

                totalCount.postValue(total);
                learnedCount.postValue(learned);
                averageMastery.postValue(avg);
                return null;
            }
        }.execute();
    }

    public void refresh() {
        loadStats();
    }

    public LiveData<Integer> getTotalCount() {
        return totalCount;
    }

    public LiveData<Integer> getLearnedCount() {
        return learnedCount;
    }

    public LiveData<Float> getAverageMastery() {
        return averageMastery;
    }
}
