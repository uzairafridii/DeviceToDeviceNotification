package com.uzair.devicetodevicenotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisteringUser extends AppCompatActivity {

    private EditText userEmail , userPassword, userName;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registering_user);

        initViews();
    }

    private void initViews()
    {
        userEmail = findViewById(R.id.edUserEmail);
        userPassword = findViewById(R.id.edUserPassword);
        userName = findViewById(R.id.edUserName);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void registerUserBtnClick(View view)
    {
        String name = userName.getText().toString();
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        registerUserWithEmailAndPassword(name , email , password);


    }

    private void registerUserWithEmailAndPassword(final String name, final String email, final String password)
    {

        if(name.isEmpty() || email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog.setMessage("Wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email , password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {

                            if(task.isSuccessful())
                            {

                                // get the device token
                                FirebaseInstanceId.getInstance().getInstanceId()
                                        .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                    @Override
                                    public void onSuccess(InstanceIdResult instanceIdResult)
                                    {

                                        String deviceToken = instanceIdResult.getToken();

                                        Map<String, String> userData = new HashMap<>();
                                        userData.put("userName", name);
                                        userData.put("userEmail", email);
                                        userData.put("userPassword", password);
                                        userData.put("uid", mAuth.getCurrentUser().getUid());
                                        userData.put("deviceToken", deviceToken);


                                        // store data in database
                                        dbRef.child(mAuth.getCurrentUser().getUid())
                                                .setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(RegisteringUser.this, "Susscessfully register", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                    startActivity(new Intent(RegisteringUser.this , HomePage.class));
                                                    RegisteringUser.this.finish();

                                                }
                                                else
                                                {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(RegisteringUser.this, "Error in registering user", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                    }
                                });

                            }

                        }
                    });
        }
    }

    public void alreadyHaveAccount(View view)
    {
        startActivity(new Intent(this , MainActivity.class));
        this.finish();
    }
}
