package com.example.podcast_vk.entity.podcast;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "description", strict = false)
public class PodcastItemDescription {
    public PodcastItemDescription() {
    }

    public PodcastItemDescription(String d) {
        description = d;
    }

    @Namespace(prefix = "content")
    @Element(name = "encoded")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
