package com.xiazhao.redbook.payload

data class PostDto(
    var id: Long? = null,
    var title: String = "",
    var description: String? = null,
    var content: String? = null,
)
