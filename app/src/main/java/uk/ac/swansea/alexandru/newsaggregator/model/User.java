package uk.ac.swansea.alexandru.newsaggregator.model;

import java.util.List;
import java.util.Map;

public class User {
    private Map<String, Integer> customKeywords;
    private List<NewsStream> customStreams;

    private User() {}

    public User(Map<String, Integer> customKeywords, List<NewsStream> customStreams) {
        this.customKeywords = customKeywords;
        this.customStreams = customStreams;
    }

    public Map<String, Integer> getCustomKeywords() {
        return customKeywords;
    }

    public List<NewsStream> getCustomStreams() {
        return customStreams;
    }
}
