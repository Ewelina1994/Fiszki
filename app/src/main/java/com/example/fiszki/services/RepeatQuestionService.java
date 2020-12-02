package com.example.fiszki.services;

import com.example.fiszki.FirebaseConfiguration;
import com.example.fiszki.RepeatQuestionDTO;
import com.example.fiszki.entity.Option;
import com.example.fiszki.entity.Question;
import com.example.fiszki.entity.RepeatQuestion;

import java.util.ArrayList;
import java.util.List;

public class RepeatQuestionService {
    private List<RepeatQuestionDTO> repeatQuestionDTOList;
    private List<RepeatQuestion> repeatQuestionList;
    private FirebaseConfiguration firebaseConfiguration;

    public RepeatQuestionService(FirebaseConfiguration firebaseConfiguration) {
        this.firebaseConfiguration =firebaseConfiguration;
        repeatQuestionList=getAllQuestionOnRepeatBoard();
        repeatQuestionDTOList= new ArrayList<>();
    }

    public List<RepeatQuestionDTO> getRepeatQuestionDTOList() {
        repeatQuestionList=getAllQuestionOnRepeatBoard();
        repeatQuestionDTOList.clear();

        for(int i=0; i<repeatQuestionList.size(); i++){
            RepeatQuestionDTO repeatQuestionDTO= new RepeatQuestionDTO();

            Question question= firebaseConfiguration.getQuestionById(repeatQuestionList.get(i).getQuestion());
            Option optionPL= firebaseConfiguration.getGoodOptionPL(repeatQuestionList.get(i).getQuestion());
            Option optionEN= firebaseConfiguration.getGoodOptionEN(repeatQuestionList.get(i).getQuestion());

            repeatQuestionDTO.setQuestion(question.getName());
           // repeatQuestionDTO.setName_image(question.getName_image());
            repeatQuestionDTO.setOptionPL(optionPL.getName());
            repeatQuestionDTO.setOptionEN(optionEN.getName());
            repeatQuestionDTO.setAddToRepeatBoard(isAddQuestionToRepeatBoard(question.getId()));
            // repeatQuestionDTO.setName_image(questionDTO.getQuestion().getName_image()));
            //  repeatQuestionDTO.setSentence(questionDTO.getQuestion().getSentence());

            repeatQuestionDTOList.add(repeatQuestionDTO);
        }

        return repeatQuestionDTOList;
    }

    public long saveQuestionToDBRepeatTable(RepeatQuestion question){
        //repeatQuestionList=getAllQuestionOnRepeatBoard();
        RepeatQuestion repeatQuestion = question;

        boolean isAddToRepeatBoard=isAddQuestionToRepeatBoard(question.getQuestion());
        //spr czy pytenie nie jest w tablicy powtórek
        if(isAddToRepeatBoard==false){
            return firebaseConfiguration.addQuestionToRepeatTable(repeatQuestion.getQuestion());
        }
        return -1;
    }

    //???
    public long deleteQuestionToDBRepeatTable(RepeatQuestion question){
        return firebaseConfiguration.deleteQuestionFromRepeatTable(question.getQuestion());
    }

    public List<RepeatQuestion> getAllQuestionOnRepeatBoard(){
        return firebaseConfiguration.getAllQuestionFromRepeatTable();
    }

    //sprawdzenie czy pytanie jest juz w tablicy powtórek
    public boolean isAddQuestionToRepeatBoard(long idQuestion){
        repeatQuestionList=getAllQuestionOnRepeatBoard();
        boolean isAddToRepeatBoard=false;
        for (int i=0; i<repeatQuestionList.size(); i++){
            if(repeatQuestionList.get(i).getQuestion()==idQuestion){
                isAddToRepeatBoard=true;
                break;
            }
        }
        return isAddToRepeatBoard;
    }

}
