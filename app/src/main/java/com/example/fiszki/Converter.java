package com.example.fiszki;

import com.example.fiszki.entity.QuestionDTO;
import com.example.fiszki.entity.RepeatQuestion;

public class Converter {
    public Converter() {
    }

    public static RepeatQuestion questionDTOtoRepeatQuestion(QuestionDTO questionDTO){
        RepeatQuestion repeatQuestion= new RepeatQuestion();
        repeatQuestion.setQuestion(questionDTO.getQuestion().getId());
        return repeatQuestion;
    }

    public RepeatQuestion repeatQuestionDTOtoRepeatQuestion(RepeatQuestionDTO repeatQuestionDTO) {
        RepeatQuestion repeatQuestion= new RepeatQuestion();
       // Question question=FirebaseConfiguration.getQuestionByName(repeatQuestionDTO.getQuestion());
       // repeatQuestion.setQuestion(question.getId());

        return repeatQuestion;
    }
}
