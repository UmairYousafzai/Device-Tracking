package com.example.devicetracker.Message.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devicetracker.Message.ChatUserListFragmentDirections;
import com.example.devicetracker.adapter.AssignedUserRecyclerAdapter;
import com.example.devicetracker.databinding.ChatUserCardBinding;
import com.example.devicetracker.databinding.UserCardBinding;
import com.example.devicetracker.models.AssignedUser;

import java.util.ArrayList;
import java.util.List;

public class ChatUserListAdapter extends RecyclerView.Adapter<ChatUserListAdapter.ChatUserListViewHolder> {

    private LayoutInflater layoutInflater;
    private List<AssignedUser> assignedUserList;
    private Fragment fragment;

    public ChatUserListAdapter(Fragment fragment) {

        assignedUserList= new ArrayList<>();
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ChatUserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        ChatUserCardBinding binding = ChatUserCardBinding.inflate(layoutInflater, parent, false);

        return new ChatUserListViewHolder(binding);    }

    @Override
    public void onBindViewHolder(@NonNull ChatUserListViewHolder holder, int position) {
        AssignedUser user= new AssignedUser();
        user= assignedUserList.get(position);

        holder.mBinding.setUser(user);
        holder.mBinding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return assignedUserList==null?0:assignedUserList.size();
    }

    public void setAssignUserList(List<AssignedUser> list) {

        if (list.size()>0)
        {
            assignedUserList= list;
            notifyDataSetChanged();
        }
        else {
            assignedUserList.clear();
            notifyDataSetChanged();
        }
    }

    public class ChatUserListViewHolder extends RecyclerView.ViewHolder
    {
        ChatUserCardBinding mBinding;

        public ChatUserListViewHolder(@NonNull ChatUserCardBinding binding) {
            super(binding.getRoot());
            mBinding= binding;
            mBinding.chatUserCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (getAdapterPosition() != RecyclerView.NO_POSITION)
                    {
                        AssignedUser user=assignedUserList.get(getAdapterPosition());
                        NavController navController= NavHostFragment.findNavController(fragment);

                        ChatUserListFragmentDirections.ActionChatsToChatFragment action =ChatUserListFragmentDirections.actionChatsToChatFragment();
                        action.setRecevierID(user.getId());
                        navController.navigate(action);
                    }


                }
            });
        }
    }
}
