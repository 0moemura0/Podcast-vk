package com.example.podcast_vk.entity.podcast

import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode

class ItunesCategory(val category: String?)

class ItunesCategoryConverter : Converter<ItunesCategory> {
    override fun read(node: InputNode): ItunesCategory {
        val name: InputNode? = node.getAttribute("text")
        return ItunesCategory(name?.value)
    }

    override fun write(node: OutputNode?, value: ItunesCategory?) {
        val name: String? = value?.category
        node!!.setAttribute("text", name)
    }
}