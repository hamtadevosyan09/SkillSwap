package com.myproject.skillswap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Problems extends Fragment {

    private TextView problemOfTheWeekTextView, solutionOfTheWeekTextView;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problems, container, false);
        db = FirebaseFirestore.getInstance();

        // Initialize TextViews for the problem and the solution
        problemOfTheWeekTextView = view.findViewById(R.id.problemOfTheWeekTextView);
        solutionOfTheWeekTextView = view.findViewById(R.id.solutionOfTheWeekTextView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveProblemAndSolution();
    }

    private void retrieveProblemAndSolution() {
        // Fetch the document "problem_of_the_week" which contains both the problem and the solution
        db.collection("user_answers")
                .document("problem_of_the_week")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get the problem and solution from the document
                        String problemOfTheWeek = documentSnapshot.getString("problem");
                        String solutionOfTheWeek = documentSnapshot.getString("solution");

                        if (problemOfTheWeek != null && !problemOfTheWeek.isEmpty()) {
                            problemOfTheWeekTextView.setText(problemOfTheWeek);
                        } else {
                            Toast.makeText(getContext(), "No problem of the day found", Toast.LENGTH_SHORT).show();
                        }

                        // Check if a solution is available
                        if (solutionOfTheWeek != null && !solutionOfTheWeek.isEmpty()) {
                            solutionOfTheWeekTextView.setText(solutionOfTheWeek);
                        } else {
                            solutionOfTheWeekTextView.setText("Solution will be added by the end of the day");
                        }
                    } else {
                        Toast.makeText(getContext(), "Problem of the day not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
