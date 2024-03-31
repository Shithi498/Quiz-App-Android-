package com.example.quizapp3.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.quizapp3.repository.AuthRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {
    private MutableLiveData<FirebaseUser> firebaseMutableLiveData;
   private FirebaseUser currentUser;
    private AuthRepository repository;


    public AuthViewModel(@NonNull Application application) {
        super(application);

        repository = new AuthRepository(application);
        currentUser= repository.getCurrentUser();
        firebaseMutableLiveData = repository.getFirebaseUserMutableLiveData();
    }
    public MutableLiveData<FirebaseUser> getFirebaseMutableLiveData() {
        return firebaseMutableLiveData;
    }

    public FirebaseUser getCurrentUser() {
     ;
        return currentUser;
    }




    public void signUp(String email,String pass){
        repository.signUp(email,pass);
    }
    public void signIn(String email,String pass){
        repository.signIn(email,pass);
    }

    public void signOut(){
        repository.singOut();
    }
}
