package com.example.fiszki.services;

import android.content.Context;

import com.example.fiszki.FirebaseConfiguration;
import com.example.fiszki.entity.QuestionDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizService {
    private static List<QuestionDTO> questionDTOList;
    public static int NUMBER_QUESTIONS = 10;

    public QuizService(){
//        questionDTOList=FirebaseConfiguration.getAllQuestionDTO();
//        Collections.shuffle(questionDTOList);

    }

    public static void setQuestionDTOList(List<QuestionDTO> questionDTOList) {
        QuizService.questionDTOList = questionDTOList;
    }

    public static List<QuestionDTO> getAllQuestionFromDatabase() {
        return FirebaseConfiguration.getAllQuestionDTO();
    }
    public static List<QuestionDTO> getRandomQuestionInQuiz(Context context){
        questionDTOList=getAllQuestionFromDatabase();
        //inicjalizacjia
        Collections.shuffle(questionDTOList);

        Random random= new Random();
        List<QuestionDTO> questionRandom= new ArrayList<>();

        while (questionRandom.size()<getNumberQuestion()){
            if(!questionDTOList.isEmpty()){
                int randomIndex=random.nextInt(questionDTOList.size());
                if(!questionRandom.contains(questionDTOList.get(randomIndex))){
                    questionRandom.add(questionDTOList.get(randomIndex));
                }
            }
        }
        return questionRandom;
    }

    public static int getNumberQuestion() {
        return NUMBER_QUESTIONS;
    }
}
