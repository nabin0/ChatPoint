package com.example.chatpoint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chatpoint.Adapters.ChatAdaptar;
import com.example.chatpoint.Models.MessagesModel;
import com.example.chatpoint.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<MessagesModel> messagesModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderId = auth.getUid();
        String receiverId = getIntent().getStringExtra("userId");
        String username = getIntent().getStringExtra("username");
        String profilePicture = getIntent().getStringExtra("profilePicture");

        binding.userName.setText(username);
        Picasso.get().load(profilePicture).placeholder(R.drawable.user_avtar).into(binding.profilePicture);

        // Setting back arrow function
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });


         messagesModels = new ArrayList<>();


        final ChatAdaptar chatAdaptar = new ChatAdaptar(messagesModels, ChatDetailActivity.this, receiverId);
        binding.chatRecyclerView.setAdapter(chatAdaptar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.chatRecyclerView.setLayoutManager(linearLayoutManager);

        // Chatting backend code

        final String senderRoom = senderId + receiverId;
        final String receiverRoom = receiverId + senderId;




        database.getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesModels.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                MessagesModel model = dataSnapshot.getValue(MessagesModel.class);
                model.setMessageId(dataSnapshot.getKey());
                messagesModels.add(model);
                }
                chatAdaptar.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), (CharSequence) error.toException(), Toast.LENGTH_SHORT).show();
            }
        });




        binding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.putMessage.getText().toString().isEmpty()){
                    return;
                }

                String message = binding.putMessage.getText().toString();

                final MessagesModel model = new MessagesModel(senderId, message);
                model.setTimestamp(new Date().getTime());
                binding.putMessage.setText("");
                database.getReference().child("chats")
                        .child(senderRoom)
                        .push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .push()
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        });
                    }
                });

            }
        });
    }
}