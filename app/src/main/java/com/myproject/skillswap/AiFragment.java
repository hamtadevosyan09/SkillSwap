package com.myproject.skillswap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.myproject.skillswap.databinding.FragmentAiBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AiFragment extends Fragment {
    private FragmentAiBinding binding;
    private static List<ChatMessage> chatMessages = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private String apiKey;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadApiKey();
    }

    private void loadApiKey() {
        try {
            Properties properties = new Properties();
            InputStream inputStream = getContext().getAssets().open("local.properties");
            properties.load(inputStream);
            apiKey = properties.getProperty("GEMINI_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                Log.e("AI", "API key is null or empty");
                apiKey = "";
            }
        } catch (IOException e) {
            Log.e("AI", "Failed to load API key: " + e.getMessage());
            apiKey = "";
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAiBinding.inflate(inflater, container, false);
        if (binding == null) {
            Log.e("AI", "Binding is null");
            return null;
        }
        setupRecyclerView();
        setupSendButton();
        return binding.getRoot();
    }

    private void setupRecyclerView() {
        if (chatMessages == null) {
            chatMessages = new ArrayList<>();
        }
        chatAdapter = new ChatAdapter(chatMessages);
        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.chatRecyclerView.setAdapter(chatAdapter);
        // Scroll to the latest message if the list is not empty
        if (!chatMessages.isEmpty()) {
            binding.chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
        }
    }

    private void setupSendButton() {
        binding.sendButton.setOnClickListener(v -> {
            if (binding.messageEditText == null) {
                Log.e("AI", "messageEditText is null");
                return;
            }
            String message = binding.messageEditText.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
                binding.messageEditText.setText("");
            }
        });
    }

    private void sendMessage(String message) {
        // Add user message to chat
        chatMessages.add(new ChatMessage(message, true));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        binding.chatRecyclerView.scrollToPosition(chatMessages.size() - 1);

        // Send message to Gemini API
        AiRequest request = new AiRequest(message);
        if (apiKey.isEmpty()) {
            Log.e("AI", "API key is empty, cannot make API call");
            chatMessages.add(new ChatMessage("Error: API key is missing", false));
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            binding.chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
            return;
        }
        ApiClient.getAiService().getAIResponse(apiKey, request).enqueue(new Callback<AIResponse>() {
            @Override
            public void onResponse(Call<AIResponse> call, Response<AIResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chatMessages.add(new ChatMessage(response.body().getResponse(), false));
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    binding.chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
                } else {
                    Log.e("AI", "Response code: " + response.code());
                    String errorMessage = "Error: Failed to get response";
                    try {
                        if (response.errorBody() != null) {
                            errorMessage = "Error: " + response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e("AI", "Error parsing error body: " + e.getMessage());
                    }
                    chatMessages.add(new ChatMessage(errorMessage, false));
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    binding.chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<AIResponse> call, Throwable t) {
                Log.e("AI", "Request failed: " + t.getMessage());
                chatMessages.add(new ChatMessage("Error: " + t.getMessage(), false));
                chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                binding.chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}