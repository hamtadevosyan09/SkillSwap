package com.myproject.skillswap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity implements CommentsAdapter.OnCommentActionListener {

    private EditText commentEditText;
    private Button postCommentButton;
    private RecyclerView commentsRecyclerView;
    private CommentsAdapter commentsAdapter;
    private List<Comment> commentList;
    private FirebaseFirestore firestore;
    private String currentUserId;
    private String postId;
    private String postCreatorId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        postId = getIntent().getStringExtra("postId");
        postCreatorId = getIntent().getStringExtra("postCreatorId");

        if (postId == null || postCreatorId == null) {
            Toast.makeText(this, "Post ID or Post Creator ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        firestore = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        commentEditText = findViewById(R.id.commentEditText);
        postCommentButton = findViewById(R.id.postCommentButton);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> onBackPressed());

        commentList = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(commentList, this);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentsAdapter);

        loadComments();

        postCommentButton.setOnClickListener(v -> postComment());
    }

    private void loadComments() {
        if (postId != null) {
            firestore.collection("comments").document(postId).collection("post_comments")
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Log.e("Firestore", "Error loading comments: " + error.getMessage());
                            Toast.makeText(this, "Failed to load comments", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (value != null) {
                            for (DocumentChange documentChange : value.getDocumentChanges()) {
                                Comment comment = documentChange.getDocument().toObject(Comment.class);
                                comment.setDocumentId(documentChange.getDocument().getId());

                                switch (documentChange.getType()) {
                                    case ADDED:
                                        commentList.add(comment);
                                        commentsAdapter.notifyItemInserted(commentList.size() - 1);
                                        break;
                                    case MODIFIED:
                                        int index = commentList.indexOf(comment);
                                        if (index != -1) {
                                            commentList.set(index, comment);
                                            commentsAdapter.notifyItemChanged(index);
                                        }
                                        break;
                                    case REMOVED:
                                        int removedIndex = commentList.indexOf(comment);
                                        if (removedIndex != -1) {
                                            commentList.remove(removedIndex);
                                            commentsAdapter.notifyItemRemoved(removedIndex);
                                        }
                                        break;
                                }
                            }
                        }
                    });
        }
    }
    private void postComment() {
        String commentText = commentEditText.getText().toString().trim();

        if (TextUtils.isEmpty(commentText)) {
            Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show();
            return;
        }

        Comment comment = new Comment(currentUserId, commentText);

        if (postId != null) {
            firestore.collection("comments").document(postId).collection("post_comments")
                    .add(comment)
                    .addOnSuccessListener(documentReference -> {
                        commentEditText.setText("");
                        Toast.makeText(this, "Comment posted", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to post comment: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Post ID is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public String getCurrentUserId() {
        return currentUserId;
    }

    @Override
    public String getPostCreatorId() {
        return postCreatorId;
    }

    @Override
    public void onEditComment(Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Comment");

        final EditText input = new EditText(this);
        input.setText(comment.getText());
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String editedText = input.getText().toString().trim();
            if (!TextUtils.isEmpty(editedText)) {
                comment.setText(editedText);

                firestore.collection("comments").document(postId).collection("post_comments")
                        .document(comment.getDocumentId())
                        .set(comment)
                        .addOnSuccessListener(aVoid -> {
                            commentsAdapter.notifyDataSetChanged();
                            Toast.makeText(this, "Comment edited successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to edit comment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onDeleteComment(Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this comment?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            commentList.remove(comment);
            commentsAdapter.notifyDataSetChanged();
            deleteCommentFromFirebase(comment);
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteCommentFromFirebase(Comment comment) {
        firestore.collection("comments").document(postId).collection("post_comments")
                .document(comment.getDocumentId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Comment deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete comment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onReportComment(Comment comment) {
    }
}