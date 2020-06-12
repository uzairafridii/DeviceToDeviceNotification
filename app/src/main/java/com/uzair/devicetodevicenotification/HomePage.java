package com.uzair.devicetodevicenotification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    private RecyclerView userList;
    private LinearLayoutManager layoutManager;
    private List<UserModel> list;
    private AdapterForUserRecycler adapter;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initViews();
        getAllUsers();
    }

    private void initViews()
    {
        userList = findViewById(R.id.usersList);
        layoutManager = new LinearLayoutManager(this);
        userList.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        adapter = new AdapterForUserRecycler(this , list);

        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }


    private void getAllUsers()
    {
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                UserModel userModel  = dataSnapshot.getValue(UserModel.class);
                list.add(userModel);
                userList.setAdapter(adapter);

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }


}
