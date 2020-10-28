package com.example.fiszki.services;


import com.example.fiszki.QuestionDTO;

import java.util.ArrayList;
import java.util.List;

public class RepeatBoardService {
    private List<QuestionDTO> repeatList;

    public RepeatBoardService() {
        this.repeatList = new ArrayList<>();
    }

    public void addToList(QuestionDTO question) {
        repeatList.add(question);
    }

    public List<QuestionDTO> getAll() {
        return repeatList;
    }
}
