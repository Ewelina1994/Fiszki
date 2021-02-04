package com.example.fiszki.services;

import android.content.Context;

import com.example.fiszki.FirebaseConfiguration;
import com.example.fiszki.entity.QuestionDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class QuizService {
    private static List<QuestionDTO> questionDTOList;
    public static int NUMBER_QUESTIONS = 10;

    public QuizService(){
//        questionDTOList=FirebaseConfiguration.getAllQuestionDTO();
//        Collections.shuffle(questionDTOList);

    }

    public static void setQuestionDTOList(List<QuestionDTO> questionDTOList) {
        QuizService.questionDTOList = questionDTOList;
    }

    public static List<QuestionDTO> getRandomQuestionInQuiz(Context context){
        //inicjalizacjia
        questionDTOList=FirebaseConfiguration.getAllQuestionDTO();
        Collections.shuffle(questionDTOList);

        Random random= new Random();
        List<QuestionDTO> questionRandom= new ArrayList<>();

        while (questionRandom.size()<NUMBER_QUESTIONS){
            if(!questionDTOList.isEmpty()){
                int randomIndex=random.nextInt(questionDTOList.size());
                if(!questionRandom.contains(questionDTOList.get(randomIndex))){
                    questionRandom.add(questionDTOList.get(randomIndex));
                }
            }
        }
        return questionRandom;
    }

    public static List<QuestionDTO> getListReapeatBoardQuestions() {
        List<QuestionDTO>repeatListQuestions= new ArrayList<>();
        for (QuestionDTO questionDTO : questionDTOList) {
            if(questionDTO.isIs_added_to_repaet_board()){

                repeatListQuestions.add(questionDTO);

            }
        }
        return repeatListQuestions;
    }

    public static void addToRepate(QuestionDTO questionDTO) {
        int index=questionDTOList.indexOf(questionDTO);
        questionDTOList.get(index).setIs_added_to_repaet_board(true);
    }

    public static void deleteFromToRepate(QuestionDTO questionDTO) {
        int index=questionDTOList.indexOf(questionDTO);
        questionDTOList.get(index).setIs_added_to_repaet_board(false);
    }


}
