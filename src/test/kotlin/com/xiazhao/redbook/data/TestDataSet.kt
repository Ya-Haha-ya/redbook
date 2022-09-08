package com.xiazhao.redbook.data

import com.xiazhao.redbook.entity.Post
import com.xiazhao.redbook.payload.PostDto

object TestDataSet {

    val DEFAULT_POSTS = listOf(
        Post(id = 1, title = "title1", description = "desc1", content = "content1"),
        Post(id = 2, title = "title2", description = "desc2", content = "content2"),
        Post(id = 3, title = "title3", description = "desc3", content = "content3"),
        Post(id = 4, title = "title4", description = "desc4", content = "content4"),
        Post(id = 5, title = "title5", description = "desc5", content = "content5"),
    )

    val DEFAULT_POST_DTOS = listOf(
        PostDto(title = "title1", description = "desc1", content = "content1"),
        PostDto(title = "title2", description = "desc2", content = "content2"),
        PostDto(title = "title3", description = "desc3", content = "content3"),
        PostDto(title = "title4", description = "desc4", content = "content4"),
        PostDto(title = "title5", description = "desc5", content = "content5"),
    )

    val DEFAULT_POST_DTOS_WITH_ID = listOf(
        PostDto(id = 1, title = "title1", description = "desc1", content = "content1"),
        PostDto(id = 2, title = "title2", description = "desc2", content = "content2"),
        PostDto(id = 3, title = "title3", description = "desc3", content = "content3"),
        PostDto(id = 4, title = "title4", description = "desc4", content = "content4"),
        PostDto(id = 5, title = "title5", description = "desc5", content = "content5"),
    )

    val POST_DTO_WITHOUT_ID = PostDto(title = "apple", description = "this", content = "hello world")
    val POST_WITH_ID = Post(id = 6, title = "apple", description = "this", content = "hello world")
}
