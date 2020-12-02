package com.example.fiszki;

import com.example.fiszki.entity.Option;
import com.example.fiszki.entity.Question;
import com.example.fiszki.entity.RepeatQuestion;

public class Converter {
    FirebaseConfiguration firebaseConfiguration;
    public Converter(FirebaseConfiguration firebaseConfiguration) {
        this.firebaseConfiguration=firebaseConfiguration;
    }

    public static RepeatQuestion questionDTOtoRepeatQuestion(QuestionDTO questionDTO){
        RepeatQuestion repeatQuestion= new RepeatQuestion();
        repeatQuestion.setQuestion(questionDTO.getQuestion().getId());
        return repeatQuestion;
    }

    public RepeatQuestion repeatQuestionDTOtoRepeatQuestion(RepeatQuestionDTO repeatQuestionDTO) {
        RepeatQuestion repeatQuestion= new RepeatQuestion();
        Question question=firebaseConfiguration.getQuestionByName(repeatQuestionDTO.getQuestion());
        repeatQuestion.setQuestion(question.getId());

        return repeatQuestion;
    }
}
