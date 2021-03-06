package com.example.practicamercadomunicipal.users;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.practicamercadomunicipal.R;
import com.example.practicamercadomunicipal.models.User;
import com.example.practicamercadomunicipal.models.Store;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final List<User> userList;
    private final Context context;

    public UsersAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_row, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        Glide.with(context).load(user.image).centerCrop().into(holder.userImageView);
        holder.userNameTextView.setText(user.name);
        holder.userEmailTextView.setText(user.email);
        holder.userBalanceTextView.setText(balanceToString(user.balance));
        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra("userNumber", position));
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        TextView userNameTextView;
        TextView userEmailTextView;
        TextView userBalanceTextView;
        ImageView userImageView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.user_name_textview);
            userEmailTextView = itemView.findViewById(R.id.user_email_textview);
            userImageView = itemView.findViewById(R.id.user_image_imageview);
            userBalanceTextView = itemView.findViewById(R.id.user_balance_textview);
        }
    }

    private static String balanceToString(double balance) {
        String balanceString = String.valueOf(balance);
        if (balanceString.endsWith(".0")) {
            balanceString = balanceString.substring(0, balanceString.length() - 2);
        }
        return balanceString + "€";
    }
}
