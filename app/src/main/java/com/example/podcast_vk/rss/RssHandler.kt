package com.example.podcast_vk.rss

import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler

class RssHandler : DefaultHandler() {
    private val rssItemList: MutableList<RssItem?>
    private var currentItem: RssItem? = null
    private var parsingTitle = false
    private var parsingLink = false
    private var parsingDescription = false
    fun getRssItemList(): List<RssItem?> {
        return rssItemList
    }

    //Called when an opening tag is reached, such as <item> or <title>
    @Throws(SAXException::class)
    override fun startElement(
        uri: String,
        localName: String,
        qName: String,
        attributes: Attributes
    ) {
        if (qName == "item") currentItem = RssItem() else if (qName == "title") parsingTitle =
            true else if (qName == "link") parsingLink =
            true else if (qName == "description") parsingDescription =
            true else if (qName == "media:thumbnail" || qName == "media:content" || qName == "image") {
            if (attributes.getValue("url") != null) currentItem?.imageUrl =
                attributes.getValue("url")
        }
    }

    //Called when a closing tag is reached, such as </item> or </title>
    @Throws(SAXException::class)
    override fun endElement(uri: String, localName: String, qName: String) {
        if (qName == "item") {
            //End of an item so add the currentItem to the list of items.
            rssItemList.add(currentItem)
            currentItem = null
        } else if (qName == "title") parsingTitle = false else if (qName == "link") parsingLink =
            false else if (qName == "description") parsingDescription = false
    }

    //Goes through character by character when parsing whats inside of a tag.
    @Throws(SAXException::class)
    override fun characters(ch: CharArray, start: Int, length: Int) {
        if (currentItem != null) {
            //If parsingTitle is true, then that means we are inside a <title> tag so the text is the title of an item.
            if (parsingTitle) currentItem!!.title =
                String(
                    ch,
                    start,
                    length
                )
            else if (parsingLink) currentItem!!.link =
                String(
                    ch,
                    start,
                    length
                )
            else if (parsingDescription) currentItem!!.description = String(ch, start, length)
        }
    }

    init {
        //Initializes a new ArrayList that will hold all the generated RSS items.
        rssItemList = ArrayList<RssItem?>()
    }
}


