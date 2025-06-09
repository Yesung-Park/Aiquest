package com.example.aiquest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> messages;
    private static final int USER = 0;
    private static final int BOT = 1;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isUser() ? USER : BOT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == USER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_user, parent, false);
            return new UserViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_bot, parent, false);
            return new BotViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).message.setText(message.getText());
            ((UserViewHolder) holder).time.setText(message.getTime());
        } else {
            ((BotViewHolder) holder).message.setText(message.getText());
            ((BotViewHolder) holder).time.setText(message.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView time;
        UserViewHolder(View view) {
            super(view);
            message = view.findViewById(R.id.text_message_user);
            time = view.findViewById(R.id.text_time_user);
        }
    }

    static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView time;
        BotViewHolder(View view) {
            super(view);
            message = view.findViewById(R.id.text_message_bot);
            time = view.findViewById(R.id.text_time_bot);
        }
    }
}