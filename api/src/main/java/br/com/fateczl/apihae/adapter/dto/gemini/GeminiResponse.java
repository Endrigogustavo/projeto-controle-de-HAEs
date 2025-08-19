package br.com.fateczl.apihae.adapter.dto.gemini;

import java.util.List;

public class GeminiResponse {
    private List<Candidate> candidates;

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public static class Candidate {
        private GeminiRequest.Content content;

        public GeminiRequest.Content getContent() {
            return content;
        }
    }
}
