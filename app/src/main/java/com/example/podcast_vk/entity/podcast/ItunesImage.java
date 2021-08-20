package com.example.podcast_vk.entity.podcast;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class ItunesImage {

    public ItunesImage() {
    }

    @Attribute(name = "href")
    private String href;

    public ItunesImage(String href) {
        this.href = href;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
