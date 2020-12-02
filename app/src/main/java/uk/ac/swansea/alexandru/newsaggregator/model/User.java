package uk.ac.swansea.alexandru.newsaggregator.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class User {
    private List<String> customKeywords;
    private List<NewsStream> customStreams;

    private User() {}

    public User(List<String> customKeywords, List<NewsStream> customStreams) {
        this.customKeywords = customKeywords;
        this.customStreams = customStreams;
    }

    public List<String> getCustomKeywords() {
        return customKeywords;
    }

    public List<NewsStream> getCustomStreams() {
        return customStreams;
    }

    public void setCustomKeywords() {
        if(this.customKeywords == null) {
            this.customKeywords = new LinkedList<>();
        }
    }

    public void setCustomStreams() {
        if(customStreams == null) {
            this.customStreams = new LinkedList<>();
        }
    }
}
