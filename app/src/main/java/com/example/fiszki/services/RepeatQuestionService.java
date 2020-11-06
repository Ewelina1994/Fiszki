package com.example.fiszki.services;

import com.example.fiszki.QuestionDTO;
import com.example.fiszki.QuizDbHelper;
import com.example.fiszki.RepeatQuestionDTO;
import com.example.fiszki.entity.Option;
import com.example.fiszki.entity.Question;
import com.example.fiszki.entity.RepeatQuestion;

import java.util.ArrayList;
import java.util.List;

public class RepeatQuestionService {
    private List<RepeatQuestionDTO> repeatQuestionDTOList;
    private List<RepeatQuestion> repeatQuestionList;
    private QuizDbHelper quizDbHelper;

    public RepeatQuestionService(QuizDbHelper dbHelper) {
        this.quizDbHelper=dbHelper;
        repeatQuestionList=getAllQuestionOnRepeatBoard();
        repeatQuestionDTOList= new ArrayList<>();
    }

    public List<RepeatQuestionDTO> getRepeatQuestionDTOList() {
        repeatQuestionList=getAllQuestionOnRepeatBoard();

        for(int i=0; i<repeatQuestionList.size(); i++){
            RepeatQuestionDTO repeatQuestionDTO= new RepeatQuestionDTO();

            Question question=quizDbHelper.getQuestionById(repeatQuestionList.get(i).getQuestion());
            Option optionPL=quizDbHelper.getGoodOptionPL(repeatQuestionList.get(i).getQuestion());
            Option optionEN=quizDbHelper.getGoodOptionEN(repeatQuestionList.get(i).getQuestion());

            repeatQuestionDTO.setQuestion(question.getName());
            repeatQuestionDTO.setName_image(question.getName_image());
            repeatQuestionDTO.setOptionPL(optionPL.getName());
            repeatQuestionDTO.setOptionEN(optionEN.getName());
            // repeatQuestionDTO.setName_image(questionDTO.getQuestion().getName_image()));
            //  repeatQuestionDTO.setSentence(questionDTO.getQuestion().getSentence());

            repeatQuestionDTOList.add(repeatQuestionDTO);
        }

        return repeatQuestionDTOList;
    }

    public long saveQuestionToDBRepeatTable(QuestionDTO question){
        repeatQuestionList=getAllQuestionOnRepeatBoard();
        QuestionDTO questionDTO = question;

        boolean isAddToRepeatBoard=isAddQuestionToRepeatBoard(question);
        //spr czy pytenie nie jest w tablicy powtórek
        if(isAddToRepeatBoard==false){
            return quizDbHelper.addQuestionToRepeatTable(questionDTO.getQuestion());
        }
        return -1;
    }

    //???
    public long deleteQuestionToDBRepeatTable(QuestionDTO question){
        QuestionDTO questionDTO =question;
        return quizDbHelper.deleteQuestionFromRepeatTable(questionDTO.getQuestion());
    }

    public List<RepeatQuestion> getAllQuestionOnRepeatBoard(){
        return quizDbHelper.getAllQuestionFromRepeatTable();
    }

    //sprawdzenie czy pytanie jest juz w tablicy powtórek
    public boolean isAddQuestionToRepeatBoard(QuestionDTO question){
        repeatQuestionList=getAllQuestionOnRepeatBoard();
        boolean isAddToRepeatBoard=false;
        for (int i=0; i<repeatQuestionList.size(); i++){
            if(repeatQuestionList.get(i).getQuestion()==question.getQuestion().getId()){
                isAddToRepeatBoard=true;
                break;
            }
        }
        return isAddToRepeatBoard;
    }

}
