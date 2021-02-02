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

//    public List<RepeatQuestionDTO> getRepeatQuestionDTOList() {
//        repeatQuestionList=getAllQuestionOnRepeatBoard();
//        repeatQuestionDTOList.clear();
//
//        for(int i=0; i<repeatQuestionList.size(); i++){
//            RepeatQuestionDTO repeatQuestionDTO= new RepeatQuestionDTO();
//
//            Question question= FirebaseConfiguration.getQuestionById(repeatQuestionList.get(i).getQuestion());
//            Option optionPL= FirebaseConfiguration.getGoodOptionPL(repeatQuestionList.get(i).getQuestion());
//            Option optionEN= FirebaseConfiguration.getGoodOptionEN(repeatQuestionList.get(i).getQuestion());
//
//            repeatQuestionDTO.setQuestion(question.getName());
//           // repeatQuestionDTO.setName_image(question.getName_image());
//            repeatQuestionDTO.setOptionPL(optionPL.getName());
//            repeatQuestionDTO.setOptionEN(optionEN.getName());
//            repeatQuestionDTO.setAddToRepeatBoard(isAddQuestionToRepeatBoard(question.getId()));
//            // repeatQuestionDTO.setName_image(questionDTO.getQuestion().getName_image()));
//            //  repeatQuestionDTO.setSentence(questionDTO.getQuestion().getSentence());
//
//            repeatQuestionDTOList.add(repeatQuestionDTO);
//        }
//
//        return repeatQuestionDTOList;
   // }

    public long saveQuestionToDBRepeatTable(RepeatQuestion question){
        //repeatQuestionList=getAllQuestionOnRepeatBoard();
//        RepeatQuestion repeatQuestion = question;
//
//        boolean isAddToRepeatBoard=isAddQuestionToRepeatBoard(question.getQuestion());
//        //spr czy pytenie nie jest w tablicy powtórek
//        if(isAddToRepeatBoard==false){
//            return 1;//FirebaseConfiguration.addQuestionToRepeatTable(repeatQuestion.getQuestion());
//        }
        return -1;
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
//        for (int i=0; i<repeatQuestionDTOList.size(); i++){
//            if(repeatQuestionDTOList.get(i).getQuestion().getId()==idQuestion){
//                isAddToRepeatBoard=true;
//                break;
//            }
//        }
        if(repeatQuestionDTOList.contains(questionDTO)){
            return true;
        }else {
            return false;
        }
    }

}
