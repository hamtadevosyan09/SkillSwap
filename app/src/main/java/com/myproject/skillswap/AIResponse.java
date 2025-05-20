package com.myproject.skillswap;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AIResponse {
    @SerializedName("candidates")
    private List<Candidate> candidates;

    public String getResponse() {
        if (candidates != null && !candidates.isEmpty()) {
            Candidate candidate = candidates.get(0);
            if (candidate.content != null && !candidate.content.parts.isEmpty()) {
                return candidate.content.parts.get(0).text;
            }
        }
        return "No response available";
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    private static class Candidate {
        @SerializedName("content")
        private Content content;
    }

    private static class Content {
        @SerializedName("parts")
        private List<Part> parts;
    }

    private static class Part {
        @SerializedName("text")
        private String text;
    }
}