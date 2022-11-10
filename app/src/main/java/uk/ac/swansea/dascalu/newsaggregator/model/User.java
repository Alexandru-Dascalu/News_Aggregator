package uk.ac.swansea.dascalu.newsaggregator.model;

import com.dfl.newsapi.model.ArticleDto;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class User {
    private List<String> bookmarks;
    private List<String> customKeywords;
    private List<NewsStream> customStreams;

    private User() {}

    public User(List<String> bookmarks, List<String> customKeywords, List<NewsStream> customStreams) {
        this.customKeywords = customKeywords;
        this.customStreams = customStreams;
        this.bookmarks = bookmarks;
    }

    public List<String> getBookmarks() {
        return bookmarks;
    }

    public List<String> getCustomKeywords() {
        return customKeywords;
    }

    public List<NewsStream> getCustomStreams() {
        return customStreams;
    }

    public void initialiseCustomKeywords() {
        if(this.customKeywords == null) {
            this.customKeywords = new LinkedList<>();
        }
    }

    public void initialiseBookmarks() {
        if(this.bookmarks == null) {
            this.bookmarks = new LinkedList<>();
        }
    }

    public List<String> streamNames() {
        List<String> namesList = new ArrayList<String>();
        for(NewsStream stream: this.customStreams) {
            namesList.add(stream.getName());
        }

        return namesList;
    }

    public boolean addNewsStream(String newStreamName) {
        //check if there is already a news stream with that name
        Optional<NewsStream> existingNewsStream = getNewsStream(newStreamName);

        //if there is not, make new empty stream with that name
        if(!existingNewsStream.isPresent()) {
            NewsStream newStream = new NewsStream(newStreamName, new ArrayList<Integer>());
            this.customStreams.add(newStream);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeNewsStream(String deletedStreamName) {
        return this.customStreams.removeIf(stream ->
                stream.getName().equalsIgnoreCase(deletedStreamName));
    }

    private Optional<NewsStream> getNewsStream(String newsStreamName) {
        return customStreams.stream().filter(stream ->
                stream.getName().equalsIgnoreCase(newsStreamName)).findFirst();
    }

    public List<String> getKeywordsForStream(String newsStreamName) {
        if(newsStreamName.equalsIgnoreCase("Recommended")) {
            return getKeywordsInBookmarks();
        } else {
            // find news stream with the given name
            Optional<NewsStream> newsStream = getNewsStream(newsStreamName);
            List<String> streamKeywords = new ArrayList<>();

            // check if such a stream exists
            if(newsStream.isPresent()) {
                // Go through the list of keyword indexes for that stream
                for(Integer keywordIndex: newsStream.get().getKeywords()) {
                    // lookup the keywords in the list of all keywords for the user.
                    streamKeywords.add(customKeywords.get(keywordIndex));
                }
            // if no stream exists with the given name, throw exception
            } else {
                throw new IllegalArgumentException(String.format(
                        "No news stream with the name %s exists!", newsStreamName));
            }

            return streamKeywords;
        }
    }

    private List<String> getKeywordsInBookmarks() {
        Set<String> recommendedKeywordSet = new HashSet<>();
        List<ArticleDto> bookmarkedArticles = bookmarkedArticlesDto();

        for(ArticleDto article: bookmarkedArticles) {
            String[] titleWords = article.getTitle().split(" ");
            for(String word: titleWords) {
                for(String keyword: customKeywords) {
                    if(keyword.equalsIgnoreCase(word)) {
                        recommendedKeywordSet.add(word);
                    }
                }
            }

            String[] descriptionWords = article.getDescription().split(" ");
            for(String word: descriptionWords) {
                for(String keyword: descriptionWords) {
                    if(keyword.equalsIgnoreCase(word)) {
                        recommendedKeywordSet.add(word);
                    }
                }
            }
        }

        return new ArrayList<String>(recommendedKeywordSet);
    }

    public boolean addCustomKeyword(String keyword) {
        boolean keywordIsNew = !customKeywords.contains(keyword);
        if(keywordIsNew) {
            customKeywords.add(keyword);
        }

        return keywordIsNew;
    }

    public boolean removeCustomKeyword(String keywordToRemove) {
        return customKeywords.removeIf(keyword -> keyword.equalsIgnoreCase(keywordToRemove));
    }

    public List<ArticleDto> bookmarkedArticlesDto() {
        List<ArticleDto> bookmarks = new ArrayList<>();
        Gson gson = new Gson();

        for(String jsonArticle: this.bookmarks) {
            bookmarks.add(gson.fromJson(jsonArticle, ArticleDto.class));
        }

        return bookmarks;
    }

    public Boolean isKeywordSelectedInStream(String keyword, String streamName) {
        Optional<NewsStream> customNewsStream = getNewsStream(streamName);
        // check if such a stream exists
        if(customNewsStream.isPresent()) {
            // Go through the list of keyword indexes for that stream
            for(Integer keywordIndex: customNewsStream.get().getKeywords()) {
                /* lookup the keyword for that index in the list of all keywords for this user.
                 * Return true if there is a match with the given keyword.*/
                if(this.customKeywords.get(keywordIndex).equalsIgnoreCase(keyword)) {
                    return true;
                }
            }

            // if no match was found, the stream does not have that keyword
            return false;
        // if no stream exists with the given name, throw exception
        } else {
            throw new IllegalArgumentException(String.format(
                    "No news stream with the name %s exists!", streamName));
        }
    }

    public boolean selectKeyword(String streamName, String keyword) {
        Optional<NewsStream> newsStream = getNewsStream(streamName);

        //check if there is a stream with the given name
        if (newsStream.isPresent()) {
            int keywordIndex = this.customKeywords.indexOf(keyword);
            //check if the user has this keyword in their list
            if (keywordIndex != -1) {
                //check if news stream does not already have that keyword
                if (!newsStream.get().getKeywords().contains(keywordIndex)) {
                    newsStream.get().getKeywords().add(keywordIndex);
                    return true;
                } else {
                    return false;
                }
            } else {
                throw new IllegalArgumentException(String.format("The keyword %s is not among " +
                        "the keywords of the current user!", keyword));
            }
        } else {
            throw new IllegalArgumentException(String.format(
                    "No news stream with the name %s exists!", streamName));
        }
    }

    public boolean unSelectKeyword(String streamName, String keyword) {
        Optional<NewsStream> newsStream = getNewsStream(streamName);

        //check if there is a stream with the given name
        if (newsStream.isPresent()) {
            int keywordIndex = this.customKeywords.indexOf(keyword);
            //check if the user has this keyword in their list
            if (keywordIndex != -1) {
                //check if news stream actually has that keyword selected
                if (newsStream.get().getKeywords().contains(keywordIndex)) {
                    newsStream.get().getKeywords().remove(Integer.valueOf(keywordIndex));
                    return true;
                } else {
                    return false;
                }
            } else {
                throw new IllegalArgumentException(String.format("The keyword %s is not among " +
                        "the keywords of the current user!", keyword));
            }
        } else {
            throw new IllegalArgumentException(String.format(
                    "No news stream with the name %s exists!", streamName));
        }
    }

    public boolean addBookmark(ArticleDto article) {
        Gson gson = new Gson();
        String stringArticle = gson.toJson(article);

        // only add the bookmark if the article is not already bookmarked
        if(!this.bookmarks.contains(stringArticle)) {
            this.bookmarks.add(stringArticle);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeBookmark(ArticleDto article) {
        Gson gson = new Gson();
        // List.remove checks if the article to be removed is actually present
        return this.bookmarks.remove(gson.toJson(article));
    }

    public boolean isBookmarked(ArticleDto article) {
        Gson gson = new Gson();
        return this.bookmarks.contains(gson.toJson(article));
    }
}
