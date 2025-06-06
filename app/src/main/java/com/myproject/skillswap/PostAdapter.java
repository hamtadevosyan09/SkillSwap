package com.myproject.skillswap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private String currentUserId;
    private OnPostOptionsClickListener optionsClickListener;

    public PostAdapter(List<Post> postList, String currentUserId, OnPostOptionsClickListener optionsClickListener) {
        this.postList = postList;
        this.currentUserId = currentUserId;
        this.optionsClickListener = optionsClickListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setPosts(List<Post> posts) {
        postList.clear();
        postList.addAll(posts);
        notifyDataSetChanged();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView usernameTextView;
        private ImageView threeDots;
        private TextView replyText;
        private ImageView replyPic;
        private TextView categoryTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            threeDots = itemView.findViewById(R.id.three_dots);
            replyText = itemView.findViewById(R.id.reply_text);
            replyPic = itemView.findViewById(R.id.reply_pic);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);

            threeDots.setOnClickListener(v -> {
                if (optionsClickListener != null) {
                    optionsClickListener.onPostOptionsClicked(v, getAdapterPosition(), postList.get(getAdapterPosition()));
                }
            });

            replyText.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, CommentsActivity.class);
                Post post = postList.get(getAdapterPosition());
                intent.putExtra("postId", post.getPostId());
                intent.putExtra("postCreatorId", post.getCreatorUserId());
                context.startActivity(intent);
            });

            replyPic.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, CommentsActivity.class);
                Post post = postList.get(getAdapterPosition());
                intent.putExtra("postId", post.getPostId());
                intent.putExtra("postCreatorId", post.getCreatorUserId());
                context.startActivity(intent);
            });
        }

        public void bind(Post post) {
            titleTextView.setText(post.getTitle());
            descriptionTextView.setText(post.getDescription());
            categoryTextView.setText(post.getCategory());
            setCategoryColor(post.getCategory());

            // ✅ Show username from Post object
            usernameTextView.setText(post.getUsername() != null ? post.getUsername() : "Username");

            threeDots.setVisibility(View.VISIBLE);
            loadCommentCount(post.getPostId());
        }

        private void setCategoryColor(String category) {
            int color;
            switch (category) {
                case "Number Theory":
                    color = R.color.green;
                    break;
                case "Geometry":
                    color = R.color.teal_200;
                    break;
                case "Algebra":
                    color = R.color.red;
                    break;
                case "Combinatorics":
                    color = R.color.orange;
                    break;
                default:
                    color = R.color.blue;
                    break;
            }
            categoryTextView.setBackgroundResource(color);
        }

        private void loadCommentCount(String postId) {
            FirebaseFirestore.getInstance()
                    .collection("comments")
                    .document(postId)
                    .collection("post_comments")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        int count = queryDocumentSnapshots.size();
                        replyText.setText("Reply (" + count + ")");
                    })
                    .addOnFailureListener(e -> {
                        replyText.setText("Reply");
                    });
        }
    }

    public interface OnPostOptionsClickListener {
        void onPostOptionsClicked(View view, int position, Post post);
    }
}