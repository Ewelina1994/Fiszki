package com.example.fiszki.services;

import android.annotation.SuppressLint;

import com.example.fiszki.entity.QuestionDTO;
import com.example.fiszki.entity.RepeatQuestion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class RepeatQuestionService {
    private static List<QuestionDTO> repeatQuestionDTOList= new ArrayList<>();

    public RepeatQuestionService() {
    }

    public static void addQuestionToRepeatBoard(QuestionDTO questionDTO){
        repeatQuestionDTOList.add(questionDTO);
    }
    public static boolean deleteQuestionToRepeatBoard(QuestionDTO question){
        return repeatQuestionDTOList.remove(question);
    }

    @SuppressLint("NewApi")
    public static List<QuestionDTO> getAllQuestionOnRepeatBoard(){
       repeatQuestionDTOList= repeatQuestionDTOList.stream().filter(f->f.isIs_added_to_repaet_board()).collect(Collectors.toList());;
        return repeatQuestionDTOList;
    }

    //sprawdzenie czy pytanie jest juz w tablicy powtórek
    public static boolean isAddQuestionToRepeatBoard(QuestionDTO questionDTO){
        repeatQuestionDTOList=getAllQuestionOnRepeatBoard();
        if(repeatQuestionDTOList.contains(questionDTO)){
            return true;
        }else {
            return false;
        }
    }

}
