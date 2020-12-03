package uk.ac.swansea.alexandru.newsaggregator

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import uk.ac.swansea.alexandru.newsaggregator.model.NewsStream
import uk.ac.swansea.alexandru.newsaggregator.model.User

class Database (private val authenticator: FirebaseAuth) {
    companion object {
        lateinit var instance: Database
    }

    private val database : FirebaseDatabase = Firebase.database
    private val newsTopicsReference : DatabaseReference = database.getReference("topics")
    private val userReference: DatabaseReference

    private lateinit var defaultNewsTopics : List<String>
    private lateinit var user : User

    private val newsTopicsListener =  object: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val topics = dataSnapshot.getValue<List<String>>()
            if(topics != null) {
                defaultNewsTopics = topics
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w("a", "loadPost:onCancelled", databaseError.toException())
        }
    }

    private val userListener =  object: ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val remoteUser = dataSnapshot.getValue<User>()
            if(remoteUser != null) {
                user = remoteUser

                user.setCustomKeywords()
                user.setCustomStreams()
                user.customStreams.forEach { it.setKeywords() }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w("a", "loadPost:onCancelled", databaseError.toException())
        }
    }

    init {
        val userID = authenticator.currentUser!!.uid
        userReference = database.getReference("users").child(userID)

        newsTopicsReference.addValueEventListener(newsTopicsListener)
        userReference.addValueEventListener(userListener)

        instance = this
    }

    fun getDefaultNewsTopics() : List<String> {
        return defaultNewsTopics
    }

    fun getUser() : User {
        return user
    }

    fun getUserCustomStreamNames(): List<String> {
        val namesList = mutableListOf<String>()

        for(stream in user.customStreams) {
            namesList.add(stream.name)
        }

        return namesList
    }

    fun addCustomNewsStream(name: String) {
        val newNewsStream = NewsStream(name, mutableListOf<Int>())
        user.customStreams.add(newNewsStream)
        userReference.setValue(user)
    }

    fun removeCustomNewsStream(deletedName: String) {
        val hasChanged = user.customStreams.removeIf { stream -> stream.name == deletedName }

        if(hasChanged) {
            userReference.setValue(user)
        }
    }

    fun getStreamKeywordConfiguration(customStreamName: String): Map<String, Boolean> {
        val keywordConfiguration: MutableMap<String, Boolean> = mutableMapOf<String, Boolean>()

        for(defaultTopic in defaultNewsTopics) {
            keywordConfiguration[defaultTopic] = false
        }

        for(customKeyword in user.customKeywords) {
            keywordConfiguration[customKeyword] = false
        }

        val customNewsStream: NewsStream = user.customStreams.first { stream -> stream.name == customStreamName }

        for(keywordIndex in customNewsStream.keywords) {
            if(keywordIndex < 0) {
                keywordConfiguration[defaultNewsTopics[-keywordIndex - 1]] = true
            } else {
                keywordConfiguration[user.customKeywords[keywordIndex]] = true
            }
        }

        return keywordConfiguration
    }

    fun getKeywordList(): List<String> {
        val keywordList = mutableListOf<String>()
        keywordList.addAll(defaultNewsTopics)
        keywordList.addAll(user.customKeywords)

        return keywordList
    }

    fun addCustomKeyword(keyword: String) {
        
    }

    fun isKeywordSelectedInStream(keyword: String, streamName: String): Boolean {
        val customNewsStream: NewsStream = user.customStreams.first { stream -> stream.name == streamName }

        var keywordIndex: Int
        if(keyword in defaultNewsTopics) {
            keywordIndex = -1 - defaultNewsTopics.indexOf(keyword)
        } else {
            keywordIndex = user.customKeywords.indexOf(keyword)
        }

        return keywordIndex in customNewsStream.keywords
    }

    fun selectKeyword(customStreamName: String, keyword: String) {
        var keywordIndex: Int = user.customKeywords.indexOf(keyword)
        if(keywordIndex == -1) {
            keywordIndex = -1 - defaultNewsTopics.indexOf(keyword)
        }

        val customStream = user.customStreams.first { stream -> stream.name == customStreamName }
        customStream.keywords.add(keywordIndex)

        userReference.setValue(user)
    }

    fun unSelectKeyword(customStreamName: String, keyword: String) {
        var keywordIndex: Int = user.customKeywords.indexOf(keyword)
        if(keywordIndex == -1) {
            keywordIndex = -1 - defaultNewsTopics.indexOf(keyword)
        }

        val customStream = user.customStreams.first { stream -> stream.name == customStreamName }
        customStream.keywords.remove(keywordIndex)

        userReference.setValue(user)
    }
}