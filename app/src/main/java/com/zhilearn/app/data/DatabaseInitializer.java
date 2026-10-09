package com.zhilearn.app.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zhilearn.app.model.CharacterData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class DatabaseInitializer {

    private static final String TAG = "DatabaseInitializer";
    private static final AtomicBoolean initialized = new AtomicBoolean(false);
    private static final AtomicBoolean initializing = new AtomicBoolean(false);

    /**
     * 异步初始化字库。
     * 在后台线程执行数据库读取与 JSON 解析，避免在主线程访问 Room 触发崩溃。
     * 回调在主线程执行，可安全操作 UI。
     */
    public static void init(Context context, OnInitCallback callback) {
        if (initialized.get()) {
            if (callback != null) callback.onReady();
            return;
        }
        if (initializing.getAndSet(true)) {
            // 已有初始化任务在跑，等待它完成
            new Thread(() -> {
                // 简单轮询等待（最多 5 秒）
                for (int i = 0; i < 50 && !initialized.get(); i++) {
                    try { Thread.sleep(100); } catch (InterruptedException e) { break; }
                }
                if (callback != null) callback.onReady();
            }).start();
            return;
        }

        Context appContext = context.getApplicationContext();
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    AppDatabase db = AppDatabase.getInstance(appContext);
                    // 后台线程访问数据库
                    List<CharacterData> existing = db.characterDao().getAllCharacters();
                    if (existing != null && existing.size() > 0) {
                        Log.d(TAG, "字库已存在，共 " + existing.size() + " 字，跳过初始化");
                        return true;
                    }
                    List<CharacterData> characters = loadCharactersFromJson(appContext);
                    if (characters != null && characters.size() > 0) {
                        db.characterDao().insertAll(characters);
                        Log.d(TAG, "已加载 " + characters.size() + " 个汉字到数据库");
                        return true;
                    }
                    Log.w(TAG, "字库为空，未能加载任何汉字");
                    return false;
                } catch (Exception e) {
                    Log.e(TAG, "初始化字库失败: " + e.getMessage(), e);
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    initialized.set(true);
                }
                initializing.set(false);
                if (callback != null) callback.onReady();
            }
        }.execute();
    }

    /**
     * 阻塞式初始化，仅在后台线程调用。
     * 若必须在启动阶段同步等待，调用方需自行确保不在主线程。
     */
    public static void initSync(Context context) {
        if (initialized.get()) return;
        AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
        List<CharacterData> existing = db.characterDao().getAllCharacters();
        if (existing != null && existing.size() > 0) {
            initialized.set(true);
            return;
        }
        List<CharacterData> characters = loadCharactersFromJson(context);
        if (characters != null && characters.size() > 0) {
            db.characterDao().insertAll(characters);
            Log.d(TAG, "已加载 " + characters.size() + " 个汉字到数据库");
        }
        initialized.set(true);
    }

    public static boolean isInitialized() {
        return initialized.get();
    }

    public interface OnInitCallback {
        void onReady();
    }

    private static List<CharacterData> loadCharactersFromJson(Context context) {
        List<CharacterData> result = new ArrayList<>();
        try {
            InputStream is = context.getResources().openRawResource(
                context.getResources().getIdentifier("characters", "raw", context.getPackageName())
            );
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
            );
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            JSONObject json = new JSONObject(sb.toString());
            JSONArray chars = json.getJSONArray("characters");

            for (int i = 0; i < chars.length(); i++) {
                JSONObject obj = chars.getJSONObject(i);
                CharacterData c = new CharacterData(
                    obj.getString("character"),
                    obj.getString("pinyin"),
                    obj.getString("meaning"),
                    obj.getInt("strokes"),
                    obj.getString("radical"),
                    obj.getInt("difficulty"),
                    obj.optString("word1", ""),
                    obj.optString("word2", ""),
                    obj.optString("sentence", "")
                );
                result.add(c);
            }

        } catch (IOException | JSONException e) {
            Log.e(TAG, "加载字库JSON失败: " + e.getMessage());
        }
        return result;
    }
}
