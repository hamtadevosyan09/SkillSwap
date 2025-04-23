package com.myproject.skillswap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myproject.skillswap.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class MyQuestFragment extends Fragment implements PostAdapter.OnPostOptionsClickListener {

    private RecyclerView recyclerView;
    private List<Post> postList;
    private List<Post> allPosts = new ArrayList<>();
    private PostAdapter postAdapter;
    private FirebaseFirestore firestore;
    private String currentUserId;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActivityResultLauncher<Intent> editPostLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_quest, container, false);

        firestore = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        postList = new ArrayList<>();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        }

        postAdapter = new PostAdapter(postList, currentUserId, this);
        recyclerView.setAdapter(postAdapter);

        loadPosts();

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::loadPosts);

        editPostLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String postId = data.getStringExtra("postId");
                            String newTitle = data.getStringExtra("title");
                            String newDescription = data.getStringExtra("description");

                            if (postId != null && newTitle != null && newDescription != null) {
                                for (int i = 0; i < allPosts.size(); i++) {
                                    Post post = allPosts.get(i);
                                    if (post.getPostId().equals(postId)) {
                                        post.setTitle(newTitle);
                                        post.setDescription(newDescription);
                                        postAdapter.notifyDataSetChanged();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
        );

        return view;
    }

    private void loadPosts() {
        if (firestore != null) {
            firestore.collection("posts").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<Post> newPostList = new ArrayList<>();
                    for (DocumentChange documentChange : task.getResult().getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            Post post = documentChange.getDocument().toObject(Post.class);
                            if (post != null && post.getCreatorUserId() != null && post.getCreatorUserId().equals(currentUserId)) {
                                post.setPostId(documentChange.getDocument().getId());
                                newPostList.add(0, post); // Add at the beginning
                            }
                        }
                    }
                    allPosts.clear();
                    allPosts.addAll(newPostList);
                    postList.clear();
                    postList.addAll(newPostList);
                    postAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Toast.makeText(requireContext(), "Failed to load posts: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    @Override
    public void onPostOptionsClicked(View view, int position, Post post) {
        if (post != null && post.getCreatorUserId() != null && currentUserId != null
                && post.getCreatorUserId().equals(currentUserId)) {
            showPostOptionsMenu(view, post);
        }
    }

    private void showPostOptionsMenu(View view, Post post) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.inflate(R.menu.post_options_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_edit) {
                Intent intent = new Intent(requireContext(), EditPostActivity.class);
                intent.putExtra("postId", post.getPostId());
                intent.putExtra("title", post.getTitle());
                intent.putExtra("description", post.getDescription());
                editPostLauncher.launch(intent);
                return true;
            } else if (itemId == R.id.action_delete) {
                deletePost(post);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void deletePost(Post post) {
        String postId = post.getPostId();
        int position = postList.indexOf(post);

        if (postId != null) {
            firestore.collection("posts").document(postId).delete()
                    .addOnSuccessListener(aVoid -> {
                        allPosts.remove(post);
                        postList.remove(position);
                        postAdapter.notifyItemRemoved(position);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Failed to delete post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(requireContext(), "Post ID is null, unable to delete post", Toast.LENGTH_SHORT).show();
        }
    }
}