package com.example.devicetracker.Message;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.devicetracker.Message.Adapter.MessageRecyclerViewAdapter;
import com.example.devicetracker.Notification.SendNoticationClass;
import com.example.devicetracker.R;
import com.example.devicetracker.databinding.FragmentChatBinding;
import com.example.devicetracker.models.Message;
import com.example.devicetracker.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ChatFragment extends Fragment {
    private FragmentChatBinding mBinding;
    private String receiverID;
    private MessageRecyclerViewAdapter adapter;
    private String senderRoom, receiverRoom;
    private DatabaseReference databaseReference;
    private List<Message> messageList = new ArrayList<>();
    private SendNoticationClass sendNoticationClass= new SendNoticationClass();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentChatBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        assert getArguments() != null;
        receiverID = ChatFragmentArgs.fromBundle(getArguments()).getRecevierID();
        senderRoom = FirebaseAuth.getInstance().getUid() + receiverID;
        receiverRoom = receiverID + FirebaseAuth.getInstance().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        setUpRecyclerView();
        btnListener();
        getMessages();
    }

    private void setUpRecyclerView() {

        mBinding.chatMessageRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new MessageRecyclerViewAdapter();
        mBinding.chatMessageRecyclerview.setAdapter(adapter);
    }

    private void getMessages() {

        databaseReference.child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageList.clear();

                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                            Message message = new Message();

                            message = snapshot1.getValue(Message.class);
                            assert message != null;
                            message.setMessageId(snapshot1.getKey());
                            messageList.add(message);

                        }
                        adapter.setMessageList(messageList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void btnListener() {

        mBinding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messageString = mBinding.etMessage.getText().toString();
                mBinding.etMessage.setText("");

                if (messageString.length() > 0) {

                    Date date = new Date();
                    Message message = new Message();

                    message.setMessage(messageString);
                    message.setSenderId(FirebaseAuth.getInstance().getUid());
                    message.setTimestamp(date.getTime());
                    databaseReference.child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .push()
                            .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            databaseReference.child("chats")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .push()
                                    .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    sendNotification(message.getMessage());
                                }
                            });
                        }
                    });

                } else {
                    Toast.makeText(requireContext(), "Please type message.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendNotification(String message1 ) {

        FirebaseDatabase.getInstance().getReference("Users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User senderUser=new User();
                senderUser= snapshot.getValue(User.class);
                String senderUserEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
                String message = senderUser.getUserName()+ " : "+ message1;
                sendNoticationClass.UpdateToken();
                FirebaseDatabase.getInstance().getReference().child("Tokens").child(receiverID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String receiverUserToken = dataSnapshot.getValue(String.class);

                        sendNoticationClass.sendNotifications(receiverUserToken, senderUserEmail, "Message", message, requireContext());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}