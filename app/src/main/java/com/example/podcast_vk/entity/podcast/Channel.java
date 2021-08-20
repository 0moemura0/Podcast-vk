package com.example.podcast_vk.entity.podcast;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import java.util.List;

@Root(name = "channel", strict = false)
public class Channel {
    public Channel() {

    }

    public Channel(String la, String ti, String de, String pub) {
        language = la;
        title = ti;
        description = de;
        pubDate = pub;
    }

    @Element(required = false)
    private String language;
    @Element(required = false)
    private String title;
    @Element(required = false)
    private String description;
    @Element(required = false)
    private String pubDate;

    @Element(name = "itunes:category", required = false)
    @Convert(ItunesCategoryConverter.class)
    private ItunesCategory itunesCategory;

    @Namespace(prefix = "itunes")
    @Element(name = "author", required = false)
    private String imagines_author;

    @ElementList(entry = "item", inline = true, required = false)
    private List<PodcastItem> podcastItems;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public ItunesCategory getItunesCategory() {
        return itunesCategory;
    }

    public void setItunesCategory(ItunesCategory itunesCategory) {
        this.itunesCategory = itunesCategory;
    }

//    public PodcastImage getImage() {
//        return image;
//    }

//    public void setImage(PodcastImage image) {
//        this.image = image;
//    }

    public String getImagines_author() {
        return imagines_author;
    }

    public void setImagines_author(String imagines_author) {
        this.imagines_author = imagines_author;
    }

    public List<PodcastItem> getPodcastItems() {
        return podcastItems;
    }

    public void setPodcastItems(List<PodcastItem> podcastItems) {
        this.podcastItems = podcastItems;
    }

}
