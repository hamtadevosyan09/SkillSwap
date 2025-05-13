package com.myproject.skillswap;

import java.util.Collections;
import java.util.List;

public class AiRequest {
    private List<Content> contents;

    public AiRequest(String message) {
        this.contents = Collections.singletonList(new Content(message));
    }

    public static class Content {
        private List<Part> parts;

        public Content(String message) {
            this.parts = Collections.singletonList(new Part(message));
        }
    }

    public static class Part {
        private String text;

        public Part(String text) {
            this.text = text;
        }
    }
}
