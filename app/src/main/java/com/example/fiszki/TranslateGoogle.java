package com.example.fiszki;

import com.gtranslate.Audio;
import com.gtranslate.Language;
import com.gtranslate.Translator;

import java.io.IOException;
import java.io.InputStream;

import javazoom.jl.decoder.JavaLayerException;

public class TranslateGoogle {
    public TranslateGoogle() {
    }

    public String translatePolishToEnglish(String textToTranslate) throws IOException, JavaLayerException {
        Translator translate = Translator.getInstance();
        String text = translate.translate(textToTranslate, Language.POLISH, Language.ENGLISH);

        Audio audio = Audio.getInstance();
        InputStream sound = audio.getAudio("I am a bus", Language.ENGLISH);
        audio.play(sound);

        return text;
    }

}
