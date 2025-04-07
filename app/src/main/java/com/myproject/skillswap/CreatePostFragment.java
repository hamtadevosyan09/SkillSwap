package com.myproject.skillswap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreatePostFragment extends Fragment {
    private EditText editTextTitle, editTextDescription;
    private Button buttonPost, buttonBeginner, buttonAdvanced, buttonAmateur, buttonCategoryTech, buttonCategoryBusiness;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private boolean isPosting = false;
    private String selectedDifficulty = "";
    private String selectedCategory = "";

    private Button buttonCategoryGeo, buttonCategoryComb , buttonCategoryAlg, buttonCategoryNum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post1, container, false);
        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        buttonPost = view.findViewById(R.id.button_post);
        buttonCategoryGeo= view.findViewById(R.id.geometry);
        buttonCategoryAlg = view.findViewById(R.id.algebra);
        buttonCategoryNum= view.findViewById(R.id.number);
        buttonCategoryComb = view.findViewById(R.id.combinatorics);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPosting) {
                    return;
                }
                isPosting = true;
                postQuestion();
            }
        });

        buttonCategoryGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategory = "Geometry";
            }
        });

        buttonCategoryAlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategory = "Algebra";
            }
        });

        buttonCategoryNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategory = "Number_Theory";
            }
        });
        buttonCategoryComb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategory = "Combinatorics";
            }
        });

        return view;
    }

    private void postQuestion() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            isPosting = false;
            return;
        }
        String userId = currentUser.getUid();
        if (title.isEmpty() || title.length() < 2) {
            editTextTitle.setError("Title must be at least 2 characters long");
            isPosting = false;
            return;
        }
        if (description.isEmpty() || description.length() < 5) {
            editTextDescription.setError("Description must be at least 5 characters long");
            isPosting = false;
            return;
        }
        if (selectedCategory.isEmpty()) {
            Toast.makeText(getContext(), "Please select a category", Toast.LENGTH_SHORT).show();
            isPosting = false;
            return;
        }

        String creatorUserId = currentUser.getUid();
        String postId = firestore.collection("posts").document().getId();
        Post post = new Post(title, description, userId, creatorUserId, selectedCategory);
        post.setPostId(postId);
        firestore.collection("posts").document(postId).set(post)
                .addOnSuccessListener(documentReference -> {
                    editTextTitle.setText("");
                    editTextDescription.setText("");
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Post added successfully", Toast.LENGTH_SHORT).show();
                    }
                    isPosting = false;
                })
                .addOnFailureListener(e -> {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Failed to add post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    isPosting = false;
                });
    }
}