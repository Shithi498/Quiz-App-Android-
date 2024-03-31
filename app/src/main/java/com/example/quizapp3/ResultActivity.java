package com.example.quizapp3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quizapp3.ViewModel.QuestionViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {

    private QuestionViewModel viewModel;
    private TextView correctAnswer, wrongAnswer, notAnswered;
    private TextView percentTv;
    private ProgressBar scoreProgressbar;
    private String quizId;
    private Button homeBtn;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(QuestionViewModel.class);

        correctAnswer = findViewById(R.id.textView6);
        wrongAnswer = findViewById(R.id.textView7);
        notAnswered = findViewById(R.id.textView13);

        percentTv = findViewById(R.id.textView10);
        scoreProgressbar = findViewById(R.id.progressBar);

        homeBtn = findViewById(R.id.button5);


        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, ListActivity.class));
            }
        });



        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("quizId")) {
            quizId = intent.getStringExtra("quizId");
            if (quizId != null) {
                viewModel.setQuizId(quizId);
                viewModel.getResults();

                viewModel.getResultMutableLiveData().observe(this, new Observer<HashMap<String, Long>>() {
                    @Override
                   public void onChanged(HashMap<String, Long> stringLongHashMap) {

                            Long correct = stringLongHashMap.get("correct");
                            Long wrong = stringLongHashMap.get("wrong");
                            Long notAnswer = stringLongHashMap.get("notAnswered");

                            correctAnswer.setText(String.valueOf(correct));
                            wrongAnswer.setText(String.valueOf(wrong));
                            notAnswered.setText(String.valueOf(notAnswer));

                            Long total = correct + wrong + notAnswer;
                            Long percent = (correct * 100) / total;

                            percentTv.setText(String.valueOf(percent));
                            scoreProgressbar.setProgress(percent.intValue());
                        }

                });
            }
        }
    }
}
