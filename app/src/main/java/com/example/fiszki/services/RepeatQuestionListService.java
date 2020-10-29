package com.example.fiszki.services;

import com.example.fiszki.RepeatQuestionDTO;

import java.util.ArrayList;
import java.util.List;

public class RepeatQuestionListService {
    List<RepeatQuestionDTO> repeatQuestionDTOList;

    public RepeatQuestionListService() {
        repeatQuestionDTOList= new ArrayList<>();

    }

    public void addToRepeatQuestionDTOList(RepeatQuestionDTO r){
        repeatQuestionDTOList.add(r);
    }

    public  List<RepeatQuestionDTO> getAll(){
        return repeatQuestionDTOList;
    }
}
