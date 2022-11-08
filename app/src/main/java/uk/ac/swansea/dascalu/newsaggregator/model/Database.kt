package uk.ac.swansea.dascalu.newsaggregator.model

import android.util.Log
import com.dfl.newsapi.model.ArticleDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import uk.ac.swansea.dascalu.newsaggregator.FirebaseLoadedCallback
import kotlin.IllegalStateException

class Database private constructor (authenticator: FirebaseAuth,
                                    private val callback : FirebaseLoadedCallback) {
    companion object {
        private lateinit var databaseInstance: Database

        fun getInstance() : Database {
            if (::databaseInstance.isInitialized) {
                return databaseInstance
            } else {
                throw IllegalStateException("Database object does not exist! You must pass in an " +
                        "authenticator and callback in order to create one! You must call " +
                        "getDatabase(authenticator, callback) instead!")
            }
        }

        fun createDatabase(authenticator: FirebaseAuth, callback: FirebaseLoadedCallback) {
            databaseInstance = Database(authenticator, callback)
        }
    }

    private val database : FirebaseDatabase = Firebase.database
    private val userReference: DatabaseReference = database.getReference("users").child(
        authenticator.currentUser!!.uid)

    private lateinit var user : User

    private val userListener =  object: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val remoteUser = dataSnapshot.getValue<User>()
            if(remoteUser != null) {
                user = remoteUser

                user.setCustomKeywords()
                user.setBookmarks()
                user.customStreams.forEach { it.setKeywords() }

                callback.onLoaded()
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w("a", "loadPost:onCancelled", databaseError.toException())
        }
    }

    init {
        userReference.addValueEventListener(userListener)
    }

    fun getUser() : User {
        return user
    }

    fun getUserCustomStreamNames(): MutableList<String> {
        val namesList = mutableListOf<String>()

        for(stream in user.customStreams) {
            namesList.add(stream.name)
        }

        return namesList
    }

    fun addCustomNewsStream(name: String) {
        val existingNewsStreams = user.customStreams.filter {
            it.name.lowercase() == name.lowercase() }
        if(existingNewsStreams.size > 1) {
            throw IllegalStateException("Multiple news streams with the same name!")
        }

        if(existingNewsStreams.isEmpty()) {
            val newNewsStream = NewsStream(name, mutableListOf<Int>())
            user.customStreams.add(newNewsStream)
            userReference.setValue(user)
        }
    }

    fun removeCustomNewsStream(deletedName: String) {
        val hasChanged = user.customStreams.removeIf { stream ->
            stream.name.lowercase() == deletedName.lowercase() }

        if(hasChanged) {
            userReference.setValue(user)
        }
    }

    fun getKeywordList(): List<String> {
        return user.customKeywords
    }

    fun getKeywordsForStream(newsStreamName: String) : List<String> {
        if(newsStreamName == "Recommended") {
            return getKeywordsInBookmarks()
        } else {
            val allKeywords = getKeywordList()
            val streamKeywords = mutableListOf<String>()

            for(keyword in allKeywords) {
                if(isKeywordSelectedInStream(keyword, newsStreamName)) {
                    streamKeywords.add(keyword)
                }
            }

            return streamKeywords
        }
    }

    fun addCustomKeyword(keyword: String) {
        val existingKeywords = user.customKeywords.filter { it.lowercase() == keyword.lowercase() }
        if(existingKeywords.size > 1) {
            throw IllegalStateException("Multiple identical keywords!")
        }

        if(existingKeywords.isEmpty()) {
            user.customKeywords.add(keyword)
            userReference.setValue(user)
        }
    }

    fun removeCustomKeyword(keyword: String) {
        val hasChanged = user.customKeywords.removeIf { it.lowercase() == keyword.lowercase() }

        if(hasChanged) {
            userReference.setValue(user)
        }
    }

    fun isKeywordSelectedInStream(keyword: String, streamName: String): Boolean {
        val customNewsStream: NewsStream = user.customStreams.first { stream ->
            stream.name.lowercase() == streamName.lowercase() }

        val keywordIndex: Int = user.customKeywords.indexOf(keyword)

        return keywordIndex in customNewsStream.keywords
    }

    fun selectKeyword(customStreamName: String, keyword: String) {
        val keywordIndex: Int = user.customKeywords.indexOf(keyword)

        val customStream = user.customStreams.first { stream ->
            stream.name.lowercase() == customStreamName.lowercase() }
        customStream.keywords.add(keywordIndex)

        //check if the custom stream is not the All stream
        if(user.customStreams[0] != customStream) {
            //check if the keyword is not already added to the All stream
            if(keywordIndex !in user.customStreams[0].keywords) {
                user.customStreams[0].keywords.add(keywordIndex)
            }
        }

        userReference.setValue(user)
    }

    fun unSelectKeyword(customStreamName: String, keyword: String) {
        val keywordIndex: Int = user.customKeywords.indexOf(keyword)

        val customStream = user.customStreams.first { stream ->
            stream.name.lowercase() == customStreamName.lowercase() }
        customStream.keywords.remove(keywordIndex)

        userReference.setValue(user)
    }

    fun addBookmark(article: ArticleDto) {
        val gson = Gson()
        user.bookmarks.add(gson.toJson(article))

        userReference.child("bookmarks").setValue(user.bookmarks)
    }

    fun removeBookmarks(article: ArticleDto) {
        val jsonArticle = Gson().toJson(article)

        if(user.bookmarks.contains(jsonArticle)) {
            user.bookmarks.remove(jsonArticle)

            userReference.child("bookmarks").setValue(user.bookmarks)
        }
    }

    fun isBookmarked(article: ArticleDto) : Boolean {
        return Gson().toJson(article) in user.bookmarks
    }

    fun getBookmarks() : List<ArticleDto> {
        val bookmarks = mutableListOf<ArticleDto>()
        val gson = Gson()

        for(jsonBookmark in user.bookmarks) {
            val article : ArticleDto = gson.fromJson(jsonBookmark, ArticleDto::class.java)
            bookmarks.add(article)
        }

        return bookmarks
    }

    fun getKeywordsInBookmarks() : List<String> {
        val recommendedKeywordSet : MutableSet<String> = mutableSetOf<String>()
        val allKeywordList : List<String> = getKeywordList()
        val bookmarkedArticles = getBookmarks()

        for(article in bookmarkedArticles) {
            val titleWords = article.title.split(" ")
            for(word in titleWords) {
                for(keyword in allKeywordList) {
                    if(keyword.equals(word, true)) {
                        recommendedKeywordSet.add(word)
                    }
                }
            }

            val descriptionWords = article.description.split(" ")
            for(word in descriptionWords) {
                for(keyword in allKeywordList) {
                    if(keyword.equals(word, true)) {
                        recommendedKeywordSet.add(word)
                    }
                }
            }
        }

        return recommendedKeywordSet.toList()
    }

    fun getKeywordForArticle(article: ArticleDto) : String {
        val allKeywordList : List<String> = getKeywordList()

        val titleWords = article.title.split(" ")
        for(word in titleWords) {
            for(keyword in allKeywordList) {
                if(keyword.equals(word, true)) {
                    return keyword
                }
            }
        }

        val descriptionWords = article.description.split(" ")
        for(word in descriptionWords) {
            for(keyword in allKeywordList) {
                if(keyword.equals(word, true)) {
                    return keyword
                }
            }
        }

        return ""
    }
}