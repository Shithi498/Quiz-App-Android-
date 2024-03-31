package com.example.quizapp3;

import static com.example.quizapp3.R.string.totalquscount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp3.Model.QuestionModel;
import com.example.quizapp3.ViewModel.QuestionViewModel;

import java.util.HashMap;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    private QuestionViewModel viewModel;

    private ProgressBar progressBar;
    private Button option1Btn, option2Btn, option3Btn, nextQuesBtn;
    private TextView questionTv, ansFeedBackTv, questionNumberTv, timerCountTv;
    private ImageView closeQuizBtn;
    private String quizId;

    private long totalQuestions;

    private int currentQusNo = 0;
    private boolean canAnswer = false;
    private long timer;
    private CountDownTimer countDownTimer;
    private int notAnswered = 0;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;
    private String answer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(QuestionViewModel.class);

        closeQuizBtn = findViewById(R.id.imageView3);
        option1Btn = findViewById(R.id.Option1Btn);
        option2Btn = findViewById(R.id.Option2Btn);
        option3Btn = findViewById(R.id.Option3Btn);
        nextQuesBtn = findViewById(R.id.button3);
        ansFeedBackTv = findViewById(R.id.textView11);
        questionNumberTv = findViewById(R.id.textView9);
        timerCountTv = findViewById(R.id.textView10);
        questionTv = findViewById(R.id.textView12);
        progressBar = findViewById(R.id.progressBar);

        quizId = getIntent().getStringExtra("quizId");
        totalQuestions = getIntent().getLongExtra(getString(totalquscount), 0);

        viewModel.setQuizId(quizId);
        viewModel.getQuestions();


        option1Btn.setOnClickListener(this);
        option2Btn.setOnClickListener(this);
        option3Btn.setOnClickListener(this);
        nextQuesBtn.setOnClickListener(this);

        closeQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuizActivity.this,ListActivity.class));
            }
        });
        loadData();
    }

    private void loadData() {
        enableOptions();
        loadQuestion(1);
    }

    private void enableOptions() {
        option1Btn.setVisibility(View.VISIBLE);
        option2Btn.setVisibility(View.VISIBLE);
        option3Btn.setVisibility(View.VISIBLE);
        option1Btn.setEnabled(true);
        option2Btn.setEnabled(true);
        option3Btn.setEnabled(true);

        ansFeedBackTv.setVisibility(View.INVISIBLE);
        nextQuesBtn.setVisibility(View.INVISIBLE);
    }

    private void loadQuestion(int i) {
        currentQusNo = i;
        viewModel.getQuestionMutableLiveData().observe(this, new Observer<List<QuestionModel>>() {
            @Override
            public void onChanged(List<QuestionModel> questionModels) {
                int index = i - 1;

                    questionTv.setText(String.valueOf(currentQusNo)+") "+questionModels.get(index).getQuestion());
                    option1Btn.setText(questionModels.get(index).getOption_a());
                    option2Btn.setText(questionModels.get(index).getOption_b());
                    option3Btn.setText(questionModels.get(index).getOption_c());
                    timer = questionModels.get(index).getTimer();
                    answer = questionModels.get(index).getAnswer();

                    questionNumberTv.setText(String.valueOf(currentQusNo));
                    startTimer();
                }

        });

        canAnswer = true;
    }

    private void startTimer() {
        timerCountTv.setText(String.valueOf(timer));
        progressBar.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(timer * 1000, 1000) {
            @Override
            public void onTick(long l) {
                timerCountTv.setText(l / 1000 + "");

                Long percent = l / (timer * 10);
                progressBar.setProgress(percent.intValue());
            }

            @Override
            public void onFinish() {
                canAnswer = false;
                ansFeedBackTv.setText("Times Up!! No answer selected");
                notAnswered++;
                showNextBtn();
            }
        }.start();
    }

    private void showNextBtn() {
        if (currentQusNo == totalQuestions) {
            nextQuesBtn.setText("Submit");
            nextQuesBtn.setEnabled(true);
            nextQuesBtn.setVisibility(View.VISIBLE);

        } else {
            nextQuesBtn.setVisibility(View.VISIBLE);
            nextQuesBtn.setEnabled(true);
            ansFeedBackTv.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        int op = v.getId();


        if(op== R.id.Option1Btn ){
            verifyAnswer(option1Btn);
        }
        else  if(op== R.id.Option2Btn ){
            verifyAnswer(option2Btn);
        }
        else if(op== R.id.Option3Btn ){
            verifyAnswer(option3Btn);
        }
        else if(op==R.id.button3){
         if (currentQusNo == totalQuestions) {
            submitResult();
        } else {
            currentQusNo++;
            loadQuestion(currentQusNo);
            resetOptions();
        }}
    }

    private void resetOptions() {
        ansFeedBackTv.setVisibility(View.INVISIBLE);
        nextQuesBtn.setVisibility(View.INVISIBLE);
        nextQuesBtn.setEnabled(false);
        option1Btn.setBackground(ContextCompat.getDrawable(this, R.color.light_sky));
        option2Btn.setBackground(ContextCompat.getDrawable(this, R.color.light_sky));
        option3Btn.setBackground(ContextCompat.getDrawable(this, R.color.light_sky));
    }

    private void submitResult() {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("correct", correctAnswer);
        resultMap.put("wrong", wrongAnswer);
        resultMap.put("notAnswered", notAnswered);

        viewModel.addResults(resultMap);
        Intent intent=new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("quizId", quizId);
        startActivity(intent);

    }

    private void verifyAnswer(Button button) {
        if (canAnswer) {
            if (answer.equals(button.getText())) {
                button.setBackground(ContextCompat.getDrawable(this, R.color.green));
                correctAnswer++;
                ansFeedBackTv.setText("Correct Answer");
            } else {
                button.setBackground(ContextCompat.getDrawable(this, R.color.red));
                wrongAnswer++;
                ansFeedBackTv.setText("Wrong Answer \nCorrect Answer :" + answer);
            }
        }
        canAnswer = false;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        showNextBtn();
    }
}
