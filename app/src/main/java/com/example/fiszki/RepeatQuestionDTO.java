package com.example.fiszki;

public class RepeatQuestionDTO {
    private String name_image;
    private String question;
    private String OptionPL;
    private String OptionEN;
    private String Sentence;

    public RepeatQuestionDTO() {
    }

    public RepeatQuestionDTO(String name_image, String question, String optionPL, String optionEN, String sentence) {
        this.name_image = name_image;
        this.question = question;
        OptionPL = optionPL;
        OptionEN = optionEN;
        Sentence = sentence;
    }

    //construktor bez image
    public RepeatQuestionDTO(String question, String optionPL, String optionEN, String sentence) {
        this.question = question;
        OptionPL = optionPL;
        OptionEN = optionEN;
        Sentence = sentence;
    }

    public String getName_image() {
        return name_image;
    }

    public void setName_image(String name_image) {
        this.name_image = name_image;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionPL() {
        return OptionPL;
    }

    public void setOptionPL(String optionPL) {
        OptionPL = optionPL;
    }

    public String getOptionEN() {
        return OptionEN;
    }

    public void setOptionEN(String optionEN) {
        OptionEN = optionEN;
    }

    public String getSentence() {
        return Sentence;
    }

    public void setSentence(String sentence) {
        Sentence = sentence;
    }
}
