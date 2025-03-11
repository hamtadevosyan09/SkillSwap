package com.myproject.skillswap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class LearnFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn, container, false);

        TextView geometryTextView = view.findViewById(R.id.geometrySection);
        TextView numberTheoryTextView = view.findViewById(R.id.numberTheorySection);
        TextView algebraTextView = view.findViewById(R.id.algebraSection);
        TextView combinatoricsTextView = view.findViewById(R.id.combinatoricsSection);
        TextView goodWebsitesTextView = view.findViewById(R.id.websitesSection);

        geometryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GeometryActivity.class));
            }
        });

        numberTheoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NumberTheoryActivity.class));
            }
        });

        algebraTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AlgebraActivity.class));
            }
        });

        combinatoricsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CombinatoricsActivity.class));
            }
        });

        goodWebsitesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), websitesActivity.class));
            }
        });

        return view;
    }
}
