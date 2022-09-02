package com.xiazhao.redbook.service.impl

import com.xiazhao.redbook.dao.PostRepository
import com.xiazhao.redbook.entity.Post
import com.xiazhao.redbook.payload.PostDto
import com.xiazhao.redbook.service.PostService
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PostServiceImpl(
    @Autowired private val postRepository: PostRepository,
    @Autowired private val modelMapper: ModelMapper,
) : PostService {

    override fun getAllPosts(): List<PostDto> =
        postRepository.findAll()
            .map { modelMapper.map(it, PostDto::class.java) }

    override fun createPost(postDto: PostDto) =
        modelMapper.map(
            postRepository.save(modelMapper.map(postDto, Post::class.java)),
            PostDto::class.java,
        )
}
