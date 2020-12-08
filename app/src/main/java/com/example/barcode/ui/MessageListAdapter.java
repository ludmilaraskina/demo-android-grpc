package com.example.barcode.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.barcode.db.entity.Message;

public class MessageListAdapter extends ListAdapter<Message, MessageViewHolder> {

    public MessageListAdapter(@NonNull DiffUtil.ItemCallback<Message> diffCallback) {
        super(diffCallback);
    }

    @Override
    @NonNull
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MessageViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message current = getItem(position);
        holder.bind(current.getText());
    }

    static class MessageDiff extends DiffUtil.ItemCallback<Message> {

        @Override
        public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.getUid() == newItem.getUid();
        }
    }
}
