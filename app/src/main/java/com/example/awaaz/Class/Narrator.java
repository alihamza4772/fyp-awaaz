package com.example.awaaz.Class;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class Narrator {
    private int id;
    TextToSpeech textToSpeech;

    public Narrator(Context context) {
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Error");
                    } else {

                    }
                }
            }
        });

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void playAudio(String text) {
        textToSpeech.setSpeechRate(0.8f);
        textToSpeech.speak("The predicted text is " + text, TextToSpeech.QUEUE_FLUSH, null);

    }
}

