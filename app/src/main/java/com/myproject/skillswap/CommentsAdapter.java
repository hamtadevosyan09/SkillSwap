package com.myproject.skillswap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<Comment> commentList;
    private OnCommentActionListener commentActionListener;

    public CommentsAdapter(List<Comment> commentList, OnCommentActionListener commentActionListener) {
        this.commentList = commentList;
        this.commentActionListener = commentActionListener;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView commentTextView;
        private ImageView threeDots;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            threeDots = itemView.findViewById(R.id.threedots);
        }

        public void bind(Comment comment) {
            commentTextView.setText(comment.getText());
            threeDots.setOnClickListener(v -> {
                if (commentActionListener != null && comment.getUserId() != null) {
                    String currentUserId = commentActionListener.getCurrentUserId();
                    String postCreatorId = commentActionListener.getPostCreatorId();

                    if (currentUserId != null && currentUserId.equals(comment.getUserId())) {
                        showPopupMenu(v, comment);
                    } else if (currentUserId != null && currentUserId.equals(postCreatorId)) {
                        showPopupMenu(v, comment);
                    } else {
                        Toast.makeText(v.getContext(), "Options for other users", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(v.getContext(), "Error: Null object reference", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void showPopupMenu(View view, Comment comment) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);

            String currentUserId = commentActionListener.getCurrentUserId();
            String postCreatorId = commentActionListener.getPostCreatorId();

            if (currentUserId != null && postCreatorId != null && comment.getUserId() != null) {
                if (currentUserId.equals(comment.getUserId())) {
                    popupMenu.inflate(R.menu.post_options_menu);
                }
                if (currentUserId.equals(postCreatorId)) {
                    popupMenu.inflate(R.menu.post_options_menu_one);
                } else {
                    popupMenu.inflate(R.menu.menu_post_options2);
                }

                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.action_edit) {
                        commentActionListener.onEditComment(comment);
                        return true;
                    } else if (item.getItemId() == R.id.action_delete) {
                        commentActionListener.onDeleteComment(comment);
                        return true;
                    } else if (item.getItemId() == R.id.action_report) {
                        commentActionListener.onReportComment(comment);
                        return true;
                    }
                    return false;
                });
                popupMenu.show();
            } else {
                Toast.makeText(view.getContext(), "Error: Null object reference", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface OnCommentActionListener {
        String getCurrentUserId();

        String getPostCreatorId();

        void onEditComment(Comment comment);

        void onDeleteComment(Comment comment);

        void onReportComment(Comment comment);
    }
}