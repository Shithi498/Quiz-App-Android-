package com.example.quizapp3.repository;

import androidx.annotation.NonNull;

import com.example.quizapp3.Model.QuizListModel;
import com.example.quizapp3.ViewModel.QuizListViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class QuizListRepository {
    public static OnFirestoreTaskComplete onFirestoreTaskComplete;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference reference = firebaseFirestore.collection("Quiz");


    public QuizListRepository(OnFirestoreTaskComplete onFirestoreTaskComplete){
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public QuizListRepository(QuizListViewModel viewModel) {
        this.onFirestoreTaskComplete = (OnFirestoreTaskComplete) viewModel;
    }

    public void getQuizData(){
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    onFirestoreTaskComplete.quizDataLoaded(task.getResult().toObjects(QuizListModel.class));
                } else {
                    onFirestoreTaskComplete.onError(task.getException());
                }
            }
        });
    }

    public interface OnFirestoreTaskComplete {
        void quizDataLoaded(List<QuizListModel> quizListModels);
        void onError(Exception e);
    }
}
