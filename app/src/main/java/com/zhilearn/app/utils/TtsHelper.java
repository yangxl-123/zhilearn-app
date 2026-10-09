package com.zhilearn.app.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.Locale;

public class TtsHelper implements TextToSpeech.OnInitListener {

    private static final String TAG = "TtsHelper";
    private static volatile TtsHelper instance;
    private TextToSpeech tts;
    private boolean initialized = false;
    private Context context;

    public static TtsHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (TtsHelper.class) {
                if (instance == null) {
                    instance = new TtsHelper(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private TtsHelper(Context context) {
        this.context = context;
        tts = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.CHINESE);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w(TAG, "中文TTS不可用，尝试英文");
                tts.setLanguage(Locale.US);
            } else {
                initialized = true;
                tts.setSpeechRate(0.8f);
                tts.setPitch(1.1f);
            }
        }
    }

    public void speak(String text) {
        if (initialized && tts != null) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "zhilearn_utterance");
        }
    }

    public void speakChar(String character) {
        speak(character);
    }

    public void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            initialized = false;
        }
    }

    public boolean isInitialized() {
        return initialized;
    }
}
