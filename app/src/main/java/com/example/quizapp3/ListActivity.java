package com.example.quizapp3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.quizapp3.Adapter.QuizListAdapter;
import com.example.quizapp3.Model.QuizListModel;
import com.example.quizapp3.ViewModel.QuizListViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ListActivity extends AppCompatActivity implements QuizListAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private QuizListViewModel viewModel;
    private QuizListAdapter adapter;
    private ImageView signOut;
   FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

       viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(QuizListViewModel.class);

        recyclerView = findViewById(R.id.QuizList);
        progressBar = findViewById(R.id.progressBar3);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        adapter = new QuizListAdapter(this);
        recyclerView.setAdapter(adapter);
        auth = FirebaseAuth.getInstance();
        signOut=findViewById(R.id.imageView4);
        viewModel.getQuizListLiveData().observe(this, new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                progressBar.setVisibility(View.GONE);
                adapter.setQuizListModels(quizListModels);
                adapter.notifyDataSetChanged();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(ListActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ListActivity.this, DetailActivity.class);
        intent.putExtra("position", position); // Pass the clicked position
        startActivity(intent);
    }
}
