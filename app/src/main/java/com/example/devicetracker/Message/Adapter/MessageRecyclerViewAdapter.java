package com.example.devicetracker.Message.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devicetracker.databinding.RecevingChatCardBinding;
import com.example.devicetracker.databinding.SendingChatCardBinding;
import com.example.devicetracker.models.Message;
import com.example.devicetracker.utils.CONSTANTS;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter {
    private LayoutInflater layoutInflater;
    private List<Message> messageList;


    public MessageRecyclerViewAdapter() {
        messageList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        if (viewType == CONSTANTS.ITEM_SENT) {
            SendingChatCardBinding binding = SendingChatCardBinding.inflate(layoutInflater, parent, false);
            return new SentViewHolder(binding);
        } else {
            RecevingChatCardBinding binding = RecevingChatCardBinding.inflate(layoutInflater, parent, false);
            return new ReceiverViewHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getClass() == SentViewHolder.class) {
            SentViewHolder viewHolder = (SentViewHolder) holder;
            viewHolder.mBinding.tvMessage.setText(messageList.get(position).getMessage());
            viewHolder.mBinding.tvMessage.requestFocus();
        } else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.mBinding.tvMessage.setText(messageList.get(position).getMessage());
            viewHolder.mBinding.tvMessage.requestFocus();

        }

    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        String currentUserId = FirebaseAuth.getInstance().getUid();

        if (message.getSenderId().equals(currentUserId)) {
            return CONSTANTS.ITEM_SENT;
        } else {
            return CONSTANTS.ITEM_RECEIVE;
        }
    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    public void setMessageList(List<Message> list) {
        if (list != null) {
            messageList = list;
            notifyDataSetChanged();
        } else {
            messageList.clear();
            notifyDataSetChanged();
        }
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {

        SendingChatCardBinding mBinding;

        public SentViewHolder(@NonNull SendingChatCardBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        RecevingChatCardBinding mBinding;

        public ReceiverViewHolder(@NonNull RecevingChatCardBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

    }
}
