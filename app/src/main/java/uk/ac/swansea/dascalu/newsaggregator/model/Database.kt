package uk.ac.swansea.dascalu.newsaggregator.model

import android.util.Log
import com.dfl.newsapi.model.ArticleDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
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

    private val userListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val remoteUser = dataSnapshot.getValue<User>()
            if(remoteUser != null) {
                user = remoteUser

                user.initialiseCustomKeywords()
                user.initialiseBookmarks()
                user.customStreams.forEach { it.initialiseKeywords() }

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

    fun getUserStreamNames(): MutableList<String> {
        return user.streamNames();
    }

    fun addNewsStream(streamName: String) : Boolean {
        val isStreamNew : Boolean = user.addNewsStream(streamName)
        if (isStreamNew) {
            userReference.setValue(user)
        }

        return isStreamNew
    }

    fun removeNewsStream(deletedStreamName: String) : Boolean {
        val hasChanged : Boolean = user.removeNewsStream(deletedStreamName)
        if(hasChanged) {
            userReference.setValue(user)
        }

        return hasChanged
    }

    fun getKeywordList(): List<String> {
        return user.customKeywords
    }

    fun getKeywordsForStream(newsStreamName: String) : List<String> {
        return user.getKeywordsForStream(newsStreamName)
    }

    fun addCustomKeyword(keyword: String) : Boolean {
        val isKeywordNew : Boolean = user.addCustomKeyword(keyword)
        if(isKeywordNew) {
            userReference.setValue(user)
        }

        return isKeywordNew
    }

    fun removeCustomKeyword(keyword: String) : Boolean {
        val hasChanged = user.removeCustomKeyword(keyword)
        if(hasChanged) {
            userReference.setValue(user)
        }

        return hasChanged
    }

    fun isKeywordSelectedInStream(keyword: String, streamName: String): Boolean {
        return user.isKeywordSelectedInStream(keyword, streamName)
    }

    fun selectKeyword(customStreamName: String, keyword: String) {
        if(user.selectKeyword(customStreamName, keyword)) {
            userReference.setValue(user)
        }
    }

    fun unSelectKeyword(customStreamName: String, keyword: String) {
        if(user.unSelectKeyword(customStreamName, keyword)) {
            userReference.setValue(user)
        }
    }

    fun addBookmark(article: ArticleDto) {
       if (user.addBookmark(article)) {
           userReference.child("bookmarks").setValue(user.bookmarks)
       }
    }

    fun removeBookmark(article: ArticleDto) {
        if(user.removeBookmark(article)) {
            userReference.child("bookmarks").setValue(user.bookmarks)
        }
    }

    fun isBookmarked(article: ArticleDto) : Boolean {
        return user.isBookmarked(article)
    }

    fun getBookmarks() : List<ArticleDto> {
        return user.bookmarkedArticlesDto();
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