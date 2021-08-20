package com.example.podcast_vk.entity.podcast;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "item", strict = false)
public class PodcastItem {
    public PodcastItem() {

    }

    public PodcastItem(String title, String guid, String pubDate, String link, String image, PodcastItemDescription podcastItemDescription, Enclosure encl, String duration) {
        this.title = title;
        this.guid = guid;
        this.pubDate = pubDate;
        this.link = link;
        this.image = image;
        this.description = podcastItemDescription;
        this.enclosure = encl;
        this.duration = duration;
    }

    @Element(name = "title", required = false)
    private String title;
    @Element(name = "guid", required = false)
    private String guid;
    @Element(name = "pubDate", required = false)
    private String pubDate;
    @Element(name = "link", required = false)
    private String link;
    @Element(name = "image", required = false)
    private String image;
    @Element(name = "description", required = false)
    private PodcastItemDescription description;
    @Element(name = "enclosure", required = false)
    private Enclosure enclosure;
    //@Namespace(prefix = "itunes")
    @Element(name = "itunes:image", required = false)
    private ItunesImage itunes_image;

    @Namespace(prefix = "itunes")
    @Element(name = "duration", required = false)
    private String duration;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public PodcastItemDescription getDescription() {
        return description;
    }

    public void setDescription(PodcastItemDescription description) {
        this.description = description;
    }

    public Enclosure getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(Enclosure enclosure) {
        this.enclosure = enclosure;
    }

    public ItunesImage getItunes_image() {
        return itunes_image;
    }

    public void setItunes_image(ItunesImage itunes_image) {
        this.itunes_image = itunes_image;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
/*
*

    @Namespace(prefix = "itunes")
    @Element(name = "summary")
    val summary: String?,

    @Namespace(prefix = "itunes")
    @Element(name = "explicit")
    val explicit: String?,

    @Namespace(prefix = "itunes")
    @Element(name = "episodeType")
    val episodeType: String?,

    @Namespace(prefix = "itunes")
    @Element(name = "image")
    val image: String?,
)*/