package com.uzair.devicetodevicenotification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterForUserRecycler extends RecyclerView.Adapter<AdapterForUserRecycler.MyUsersViewHolder>
{
    private Context context;
    private List<UserModel> userList;

    public AdapterForUserRecycler(Context context, List<UserModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View myView = LayoutInflater.from(context).inflate(R.layout.users_list_design, null);

        return new MyUsersViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyUsersViewHolder holder, int position)
    {

        final UserModel users = userList.get(position);

        holder.userName.setText(users.getUserName());

        holder.sendNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("notification");

                Map<String, String> notificationData = new HashMap<>();
                notificationData.put("from" , auth.getCurrentUser().getUid());
                ref.child(users.getUid()).push().setValue(notificationData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(context, "Successfully send notification", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyUsersViewHolder extends RecyclerView.ViewHolder
    {
        private TextView userName;
        private Button sendNotificationBtn;

        public MyUsersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userNameInDesign);
            sendNotificationBtn = itemView.findViewById(R.id.sendNotificationButton);
        }
    }
}
