package com.example.fiszki;

public class RepeatQuestionDTO {
    private byte[] name_image;
    private String question;
    private String OptionPL;
    private String OptionEN;
    private boolean isAddToRepeatBoard;

    public RepeatQuestionDTO() {
    }

    public RepeatQuestionDTO(byte[] name_image, String question, String optionPL, String optionEN, boolean isAddToRepeatBoard) {
        this.name_image = name_image;
        this.question = question;
        OptionPL = optionPL;
        OptionEN = optionEN;
        isAddToRepeatBoard = isAddToRepeatBoard;
    }

    //construktor bez image
    public RepeatQuestionDTO(String question, String optionPL, String optionEN, boolean isAddToRepeatBoard) {
        this.question = question;
        OptionPL = optionPL;
        OptionEN = optionEN;
        isAddToRepeatBoard = isAddToRepeatBoard;
    }

    public byte[] getName_image() {
        return name_image;
    }

    public void setName_image(byte[] name_image) {
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

    public boolean getIsAddToRepeatBoard() {
        return isAddToRepeatBoard;
    }

    public void setAddToRepeatBoard(boolean addToRepeatBoard) {
        isAddToRepeatBoard = addToRepeatBoard;
    }
}
