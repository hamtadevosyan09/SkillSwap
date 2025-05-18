package com.myproject.skillswap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        TextView changeUsernameTextView = view.findViewById(R.id.change_username);
        TextView changePasswordTextView = view.findViewById(R.id.change_password);
        TextView policyTextView = view.findViewById(R.id.policy);
        TextView faqTextView = view.findViewById(R.id.FAQ);
        TextView logOutTextView = view.findViewById(R.id.log_out);
        TextView deleteAccountTextView = view.findViewById(R.id.Delete_account);
        ImageView backImageView = view.findViewById(R.id.back);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        changeUsernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChangeUsernameActivity.class));
            }
        });

        changePasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PasswordResetActivity.class));
            }
        });

        policyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PolicyActivity.class));
            }
        });

        faqTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FAQActivity.class));
            }
        });

        logOutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Log Out")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Log Out", (dialog, which) -> {
                            SharedPreferences sharedPref = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean("rememberMe", false).apply();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            requireActivity().finish();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });


        deleteAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DeleteAccountActivity.class));
            }
        });

        return view;
    }
}