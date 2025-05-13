package com.myproject.skillswap;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/";
    private static AiService aiService;

    public static AiService getAiService() {
        if (aiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            aiService = retrofit.create(AiService.class);
        }
        return aiService;
    }
}