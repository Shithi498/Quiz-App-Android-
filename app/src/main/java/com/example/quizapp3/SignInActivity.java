package com.example.quizapp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp3.ViewModel.AuthViewModel;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private AuthViewModel viewModel;

    private EditText editEmail, editPass;
    private TextView signUpText;

    private Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        editEmail = findViewById(R.id.editTextText);
        editPass = findViewById(R.id.editTextText2);
        signUpText = findViewById(R.id.textView);
        signInBtn = findViewById(R.id.button);

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                String pass = editPass.getText().toString();
                if (!email.isEmpty() && !pass.isEmpty()) {
                    viewModel.signIn(email, pass);

                    viewModel.getFirebaseMutableLiveData().observe(SignInActivity.this, new Observer<FirebaseUser>() {
                        @Override
                        public void onChanged(FirebaseUser firebaseUser) {
                            if (firebaseUser != null) {
                                startActivity(new Intent(SignInActivity.this, ListActivity.class));
                            } else {
                                Toast.makeText(SignInActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignInActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
