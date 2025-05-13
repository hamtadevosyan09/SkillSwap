package com.myproject.skillswap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AiService {
    @POST("models/gemini-1.5-pro-002:generateContent")
    Call<AIResponse> getAIResponse(
            @Query("key") String apiKey,
            @Body AiRequest request
    );
}