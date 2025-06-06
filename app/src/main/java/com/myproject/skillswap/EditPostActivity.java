package com.myproject.skillswap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class EditPostActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription;
    private Button buttonSave;
    private ImageView imageViewGoBack;

    private String postId;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSave = findViewById(R.id.buttonSave);
        imageViewGoBack = findViewById(R.id.goback234);

        postId = getIntent().getStringExtra("postId");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        firestore = FirebaseFirestore.getInstance();

        editTextTitle.setText(title);
        editTextDescription.setText(description);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        imageViewGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {goBack();
            }
        });
    }

    private void saveChanges() {
        String newTitle = editTextTitle.getText().toString().trim();
        String newDescription = editTextDescription.getText().toString().trim();

        if (postId != null) {
            firestore.collection("posts").document(postId).update("title", newTitle, "description", newDescription)
                    .addOnSuccessListener(aVoid -> {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("postId", postId);
                        resultIntent.putExtra("title", newTitle);
                        resultIntent.putExtra("description", newDescription);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(EditPostActivity.this, "Failed to update post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void goBack() {
        onBackPressed();
    }
}
