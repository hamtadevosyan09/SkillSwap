package com.myproject.skillswap;

import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.List;

public class AiRequest {
    @SerializedName("contents")
    private List<Content> contents;

    public AiRequest(String message) {
        this.contents = Collections.singletonList(new Content(message));
    }

    public List<Content> getContents() {
        return contents;
    }

    public static class Content {
        @SerializedName("parts")
        private List<Part> parts;

        public Content(String message) {
            this.parts = Collections.singletonList(new Part(message));
        }

        public List<Part> getParts() {
            return parts;
        }
    }

    public static class Part {
        @SerializedName("text")
        private String text;

        public Part(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}