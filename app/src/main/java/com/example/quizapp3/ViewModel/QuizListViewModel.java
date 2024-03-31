package com.example.quizapp3.ViewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.quizapp3.Model.QuizListModel;
import com.example.quizapp3.repository.QuizListRepository;
import com.example.quizapp3.repository.QuizListRepository.OnFirestoreTaskComplete;

import java.util.List;


public class QuizListViewModel extends ViewModel  {
    private MutableLiveData<List<QuizListModel>> quizListLiveData= new MutableLiveData<>();

    private QuizListRepository repository;

    public MutableLiveData<List<QuizListModel>> getQuizListLiveData() {

        return quizListLiveData;
    }

    public QuizListViewModel(){

        repository = new QuizListRepository(new OnFirestoreTaskComplete() {
            @Override
            public void quizDataLoaded(List<QuizListModel> quizListModels) {
                quizListLiveData.setValue(quizListModels);
            }

            @Override
            public void onError(Exception e) {
                Log.d("QuizError", "onError" + e.getMessage());
            }
        });
        repository.getQuizData();
    }

}
