package com.xiazhao.redbook.service

import com.xiazhao.redbook.payload.PostDto

interface PostService {
    fun getAllPosts(): List<PostDto>
    fun createPost(postDto: PostDto): PostDto
    fun getPostById(id: Long): PostDto
    fun updatePostById(id: Long, postDto: PostDto): PostDto
    fun deletePostById(id: Long)
}
