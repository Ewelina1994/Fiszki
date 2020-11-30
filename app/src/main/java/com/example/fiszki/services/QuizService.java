package com.example.fiszki.services;

import com.example.fiszki.QuestionDTO;
import com.example.fiszki.QuizDbHelper;
import com.example.fiszki.entity.Option;
import com.example.fiszki.entity.Question;
import com.example.fiszki.enums.DifficultyEnum;
import com.example.fiszki.enums.LanguageEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizService {
    ArrayList<QuestionDTO> listQuestionDTO;
    private List<Question> questionListAll;
    private List<Question> randomQuestionInQuiz=new ArrayList<>();
    private QuizDbHelper quizDbHelper;
    LanguageEnum language;

    public QuizService(QuizDbHelper dbHelper, DifficultyEnum difficulty){
        quizDbHelper=dbHelper;
        questionListAll = dbHelper.getAllQuestions();
        Collections.shuffle(questionListAll);
        language=checkWhatLanguage(difficulty);
        listQuestionDTO=getRandomQuestionInQuiz(questionListAll);
    }

    public ArrayList<QuestionDTO> getRandomQuestionInQuiz(List<Question> list){
        Random random= new Random();
        ArrayList<Question> questionRandom= new ArrayList<>();
        ArrayList<QuestionDTO>questionsDTO=new ArrayList<>();

        while (questionRandom.size()<5){
            int randomIndex=random.nextInt(list.size());
            if(!questionRandom.contains(list.get(randomIndex))){
                questionRandom.add(list.get(randomIndex));
                List<Option>options;
                options=quizDbHelper.getOptionsToQuiz(list.get(randomIndex).getId(), language);
                questionsDTO.add(new QuestionDTO(list.get(randomIndex), options));
            }

        }
        return questionsDTO;
    }

    public LanguageEnum checkWhatLanguage(DifficultyEnum difficulty) {
        switch (difficulty) {
            case Łatwy:
                return LanguageEnum.PL;
            case Średni:
                return  LanguageEnum.EN;
            default:
                return LanguageEnum.EN;
        }
    }

    public List<Question> getQuestionListAll() {
        return questionListAll;
    }

    public void setQuestionListAll(List<Question> questionListAll) {
        this.questionListAll = questionListAll;
    }

    public ArrayList<QuestionDTO> getRandomQuestionInQuiz() {
        return listQuestionDTO;
    }

    public void setRandomQuestionInQuiz(ArrayList<Question> randomQuestionInQuiz) {
        this.randomQuestionInQuiz = randomQuestionInQuiz;
    }
}
