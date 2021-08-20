package com.example.podcast_vk.entity.podcast

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Namespace(prefix = "itunes")
@Root(name = "owner", strict = false)
class PodcastOwner(
    @Element(name = "name")
    val ownerName: String?,
    @Element(name = "email")
    val email: String?
) {
}