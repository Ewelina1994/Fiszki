package com.example.fiszki;

import android.content.Context;
import android.speech.tts.TextToSpeech;

public class TextToSpeachImpl extends TextToSpeech {
    static public float PITCH;
    static public float SPEED;

    public TextToSpeachImpl(Context context, OnInitListener listener) {
        super(context, listener);
        setPitch(PITCH);
        setSpeechRate(SPEED);

    }

    public void audio(String textToAudio){
        speak(textToAudio, TextToSpeech.QUEUE_FLUSH, null);
    }
}
