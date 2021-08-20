package com.example.podcast_vk.entity.podcast;

import org.simpleframework.xml.Element;

public class PodcastImage {
    public PodcastImage() {
    }

    public PodcastImage(String u, String t, String l) {
        url = u;
        title = t;
        link = l;
    }

    @Element(name = "url", required = false)
    private String url;
    @Element(name = "title", required = false)
    private String title;
    @Element(name = "link", required = false)
    private String link;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

