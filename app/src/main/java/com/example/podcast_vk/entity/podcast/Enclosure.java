package com.example.podcast_vk.entity.podcast;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "enclosure", strict = false)
public class Enclosure {

    public Enclosure() {
    }

    public Enclosure(String t, String l, String u) {
        type = t;
        length = l;
        url = u;
    }

    @Attribute(name = "type")
    private String type;
    @Attribute(name = "length")
    private String length;
    @Attribute(name = "url")
    private String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
