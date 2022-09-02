package com.xiazhao.redbook.service

import com.xiazhao.redbook.payload.PostDto

interface PostService {
    fun getAllPosts(): List<PostDto>
    fun createPost(postDto: PostDto): PostDto
}
