package com.example.quizapp3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quizapp3.Model.QuizListModel;
import com.example.quizapp3.ViewModel.QuizListViewModel;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private TextView title, difficulty, totalQuestion;
    private Button startQuizBtn;
    private int position;
    private ProgressBar progressBar;
    private QuizListViewModel viewModel;
    private ImageView subImage,back;
    private String quizId;
    private long totalqusCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = findViewById(R.id.textView3);
        difficulty = findViewById(R.id.textView6);
        totalQuestion = findViewById(R.id.textView7);
        startQuizBtn = findViewById(R.id.button);
        progressBar = findViewById(R.id.detailprogressBar);
        subImage = findViewById(R.id.imageView2);
        back = findViewById(R.id.back);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(QuizListViewModel.class);

        position = getIntent().getIntExtra("position",0);

        viewModel.getQuizListLiveData().observe(this, new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                QuizListModel quiz = quizListModels.get(position);
               title.setText(quiz.getTitle());
                difficulty.setText(quiz.getDifficulty());
                totalQuestion.setText(String.valueOf(quiz.getQuestions()));
                Glide.with(DetailActivity.this).load(quiz.getImage()).into(subImage);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                }, 2000);
                totalqusCount = quiz.getQuestions();
                quizId = quiz.getQuizId();
            }
        });

        startQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DetailActivity.this, QuizActivity.class);
                intent.putExtra("quizId", quizId);
                intent.putExtra("totalqusCount",totalqusCount);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DetailActivity.this, ListActivity.class);

                startActivity(intent);
            }
        });
    }
}

