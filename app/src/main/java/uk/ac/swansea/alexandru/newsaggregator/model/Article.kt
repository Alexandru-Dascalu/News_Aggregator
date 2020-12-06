package uk.ac.swansea.alexandru.newsaggregator.model

class Article {
    private var reason: String? = null
    private var image: Int = 0
    private var title: String? = null
    private var description: String? = null
    private var source: String? = null
    private var publishingTime: String? = null

    fun getReason(): String {
        return reason.toString()
    }

    fun setReason(reason: String) {
        this.reason = reason
    }

    fun getImage(): Int {
        return image
    }

    fun setImage(image: Int) {
        this.image = image
    }

    fun getTitle(): String {
        return title.toString()
    }

    fun setTitle(title: String) {
        this.title = title
    }
    fun getDescription(): String {
        return description.toString()
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun getSource(): String {
        return source.toString()
    }

    fun setSource(reason: String) {
        this.source = reason
    }

    fun getTime(): String {
        return publishingTime.toString()
    }

    fun setTime(reason: String) {
        this.publishingTime = reason
    }
}