package com.example.fiszki;

import android.content.Context;

import com.example.fiszki.entity.Option;
import com.example.fiszki.entity.Question;
import com.example.fiszki.entity.QuestionDTO;
import com.example.fiszki.services.QuizService;
import com.google.firebase.FirebaseApp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static org.mockito.BDDMockito.*;
import org.hamcrest.CoreMatchers;

@RunWith(MockitoJUnitRunner.class)
public class QuizServiceTest {

    @Mock
    Context context;

    @Spy
    QuizService quizService;

    @Before
    public void init() {
        given(quizService.getAllQuestionFromDatabase()).willReturn(prepareMockDate());

        given(quizService.getNumberQuestion()).willReturn(3);
        mock(FirebaseConfiguration.class);
        //given(FirebaseApp.getInstance()).willReturn(new FirebaseApp(context, null, null));

    }

    @Test
    public void getRandomQuestions() {
        Assert.assertEquals(quizService.getRandomQuestionInQuiz(context).size(), expectedRandomQuestion().size());
    }

    public List<QuestionDTO> expectedRandomQuestion() {
        List<QuestionDTO> expectedquestionDTOList= new ArrayList<>();
        Question q1= new Question( "Question 1");
        Question q2= new Question("Question 2");
        Question q3= new Question("Question 3");

        List<Option> listOption= new ArrayList<>();
        listOption.add(new Option());
        listOption.add(new Option());
        listOption.add(new Option());
        expectedquestionDTOList.add(new QuestionDTO(q1, listOption));
        expectedquestionDTOList.add(new QuestionDTO(q2, listOption));
        expectedquestionDTOList.add(new QuestionDTO(q3, listOption));

        return expectedquestionDTOList;
    }

    public List<QuestionDTO> prepareMockDate() {
        List<QuestionDTO> questionDTOList= new ArrayList<>();
        Question q1= new Question( "Question 1");
        Question q2= new Question("Question 2");
        Question q3= new Question("Question 3");
        Question q4= new Question("Question 4");
        Question q5= new Question("Question 5");
        Question q6= new Question( "Question 6");
        Question q7= new Question("Question 7");
        Question q8= new Question("Question 8");
        Question q9= new Question("Question 9");
        Question q10= new Question("Question 10");

        List<Option> listOption= new ArrayList<>();
        listOption.add(new Option());
        listOption.add(new Option());
        listOption.add(new Option());

        questionDTOList.add(new QuestionDTO(q1, listOption));
        questionDTOList.add(new QuestionDTO(q2, listOption));
        questionDTOList.add(new QuestionDTO(q3, listOption));
        questionDTOList.add(new QuestionDTO(q4, listOption));
        questionDTOList.add(new QuestionDTO(q5, listOption));
        questionDTOList.add(new QuestionDTO(q6, listOption));
        questionDTOList.add(new QuestionDTO(q7, listOption));
        questionDTOList.add(new QuestionDTO(q8, listOption));
        questionDTOList.add(new QuestionDTO(q9, listOption));
        questionDTOList.add(new QuestionDTO(q10, listOption));

        return questionDTOList;
    }

}
