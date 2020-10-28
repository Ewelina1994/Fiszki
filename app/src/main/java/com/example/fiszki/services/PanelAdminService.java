package com.example.fiszki.services;

import com.example.fiszki.QuizDbHelper;

public class PanelAdminService {
    QuizDbHelper dbHelper;

    public PanelAdminService(QuizDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }
}
