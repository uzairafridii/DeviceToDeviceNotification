package com.uzair.devicetodevicenotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    private EditText userEmail , userPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

    }

    private void initViews()
    {
        userEmail = findViewById(R.id.edEmail);
        userPassword = findViewById(R.id.edPassword);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

    }


    public void loginBtnClick(View view)
    {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        loginUserWithEmailAndPassword(email , password);
    }

    private void loginUserWithEmailAndPassword(String email, String password)
    {

        if(email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this, "Email and password required", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.setMessage("Wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                startActivity(new Intent(MainActivity.this , HomePage.class));

                            } else {
                                Toast.makeText(MainActivity.this, "Error in login", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }

    }

    public void dontHaveAccount(View view)
    {
        startActivity(new Intent(this , RegisteringUser.class));
        this.finish();
    }
}
