package com.myproject.skillswap;

import java.util.List;

public class AIResponse {
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

    private static class Candidate {
        private Content content;
    }

    private static class Content {
        private List<Part> parts;
    }

    private static class Part {
        private String text;
    }
}