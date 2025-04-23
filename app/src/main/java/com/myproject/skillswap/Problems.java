package com.myproject.skillswap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.firestore.FirebaseFirestore;

public class Problems extends Fragment {

    private TextView problemOfTheWeekTextView, solutionOfTheWeekTextView;
    private FirebaseFirestore db;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problems, container, false);
        db = FirebaseFirestore.getInstance();

        problemOfTheWeekTextView = view.findViewById(R.id.problemOfTheWeekTextView);
        solutionOfTheWeekTextView = view.findViewById(R.id.solutionOfTheWeekTextView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this::retrieveProblemAndSolution);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveProblemAndSolution();
    }

    private void retrieveProblemAndSolution() {
        swipeRefreshLayout.setRefreshing(true); // Start loading spinner

        db.collection("user_answers")
                .document("problem_of_the_week")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String problemOfTheWeek = documentSnapshot.getString("problem");
                        String solutionOfTheWeek = documentSnapshot.getString("solution");

                        if (problemOfTheWeek != null && !problemOfTheWeek.isEmpty()) {
                            problemOfTheWeekTextView.setText(problemOfTheWeek);
                        } else {
                            problemOfTheWeekTextView.setText("No problem of the day found");
                        }

                        if (solutionOfTheWeek != null && !solutionOfTheWeek.isEmpty()) {
                            solutionOfTheWeekTextView.setText(solutionOfTheWeek);
                        } else {
                            solutionOfTheWeekTextView.setText("Solution will be added by the end of the day");
                        }
                    } else {
                        Toast.makeText(getContext(), "Problem of the day not found", Toast.LENGTH_SHORT).show();
                    }
                    swipeRefreshLayout.setRefreshing(false); // Stop spinner
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false); // Stop spinner
                });
    }
}