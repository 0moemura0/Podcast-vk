package com.example.podcast_vk.rss

import com.example.podcast_vk.rss.RssHandler
import com.example.podcast_vk.rss.RssItem
import javax.xml.parsers.SAXParserFactory

class RssReader(private val rssUrl: String) {
    //Creates a new RssHandler which will do all the parsing.
    @get:Throws(Exception::class)
    val items: List<RssItem?>
        get() {
            val factory = SAXParserFactory.newInstance()
            val saxParser = factory.newSAXParser()
            //Creates a new RssHandler which will do all the parsing.
            val handler = RssHandler()
            //Pass SaxParser the RssHandler that was created.
            saxParser.parse(rssUrl, handler)
            return handler.getRssItemList()
        }

}