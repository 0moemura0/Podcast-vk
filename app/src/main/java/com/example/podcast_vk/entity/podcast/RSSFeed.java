package com.example.podcast_vk.entity.podcast;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "rss", strict = false)
public class RSSFeed {
    public RSSFeed() {

    }

    public RSSFeed(Channel la) {
        channel = la;

    }

    @Element(name = "channel", required = false)
    private Channel channel;


    public Channel getChannel() {
        return channel;
    }

    public void setLanguage(Channel language) {
        this.channel = language;
    }
}
//@field:Element(name = "language", required = false)
//@param:Element(name = "language", required = false)
//@Path("channel")
//    var language: String? = null,
//
//@field:Element(name = "title", required = false)
//@param:Element(name = "title", required = false)
//@Path("channel")
//    var title: String? = null,
//
//@field:Element(name = "description", required = false)
//@param:Element(name = "description", required = false)
//@Path("channel")
//    var description: String? = null,
//
//@Namespace(prefix = "itunes")
//@field:Element(name = "category", required = false)
//@param:Element(name = "category", required = false)
//@Path("channel")
//@Convert(ItunesCategoryConverter::class)
//    var itunesCategory: ItunesCategory? = null,
//
//@field:Element(name = "pubDate", required = false)
//@param:Element(name = "pubDate", required = false)
//@Path("channel")
//    var pubDate: String? = null,
//
//@field:Element(name = "image", required = false)
//@param:Element(name = "image", required = false)
//@Path("channel")
//    var image: PodcastImage? = null,
//
//@Namespace(prefix = "itunes")
//@field:Element(name = "author", required = false)
//@param:Element(name = "author", required = false)
//@Path("channel")
//    var imagitunes_authore: String? = null,
//
////    @Namespace(prefix = "itunes")
////    @Element(name = "image")
////    @Path("channel")
////    @Convert(ItunesImageConverter::class)
////    var itunes_image: ItunesImage,
//
//@Namespace(prefix = "itunes")
//@field:Element(name = "explicit", required = false)
//@Path("channel")
//    var itunes_explicit: String? = null,
//
//@Namespace(prefix = "itunes")
//@field:Element(name = "owner", required = false)
//@Path("channel")
//    var itunes_owner: PodcastOwner? = null,
//
//@field:ElementList(entry = "item", inline = true, required = false)
//@Path("channel")
//    var podcastItems: List<PodcastItem>? = null,
//        )
//
