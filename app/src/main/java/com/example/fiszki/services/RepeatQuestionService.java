package com.example.fiszki.services;

import com.example.fiszki.QuestionDTO;
import com.example.fiszki.QuizDbHelper;
import com.example.fiszki.RepeatQuestionDTO;
import com.example.fiszki.entity.Option;

public class RepeatQuestionService {
    private RepeatQuestionDTO repeatQuestionDTO;
    private QuizDbHelper quizDbHelper;
    private QuestionDTO questionDTO;

    public RepeatQuestionService(QuizDbHelper dbHelper, QuestionDTO question) {
        this.quizDbHelper=dbHelper;
        this.questionDTO = question;
    }

    public RepeatQuestionDTO convertQuestionDTOtoRepeatQuestionDTO() {
        Option optionPL=quizDbHelper.getGoodOptionPL(questionDTO.getQuestion().getId());
        Option optionEN=quizDbHelper.getGoodOptionEN(questionDTO.getQuestion().getId());

        repeatQuestionDTO= new RepeatQuestionDTO();
        repeatQuestionDTO.setOptionPL(optionPL.getName());
        repeatQuestionDTO.setOptionEN(optionEN.getName());
        // repeatQuestionDTO.setName_image(questionDTO.getQuestion().getName_image()));
        //  repeatQuestionDTO.setSentence(questionDTO.getQuestion().getSentence());
        return repeatQuestionDTO;
    }

    public long saveQuestionToDBRepeatTable(){
        return quizDbHelper.addQuestionToRepeatTable(questionDTO.getQuestion());
    }

    //???
    public long deleteQuestionToDBRepeatTable(){
        return quizDbHelper.deleteQuestionFromRepeatTable(questionDTO.getQuestion());
    }
}
