package com.xiazhao.redbook.service.impl

import com.xiazhao.redbook.dao.PostRepository
import com.xiazhao.redbook.entity.Post
import com.xiazhao.redbook.exception.ResourceNotFoundException
import com.xiazhao.redbook.payload.PostDto
import com.xiazhao.redbook.service.PostService
import org.modelmapper.ModelMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val modelMapper: ModelMapper,
) : PostService {

    companion object {
        private val logger = LoggerFactory.getLogger(PostServiceImpl::class.java)
    }

    override fun getAllPosts(): List<PostDto> = postRepository.findAll().map { it.mapToDto() }
        .also { logger.info("getAllPost() get called") }

    override fun createPost(postDto: PostDto): PostDto =
        postRepository.save(modelMapper.map(postDto, Post::class.java)).mapToDto()
            .also { logger.info("createPost() get called") }

    override fun getPostById(id: Long): PostDto =
        postRepository.findById(id).orElseThrow { postNotFoundException(id) }.mapToDto()
            .also { logger.info("getPostById() get called") }

    override fun updatePostById(id: Long, postDto: PostDto): PostDto =
        with(postRepository.findById(id).orElseThrow { postNotFoundException(id) }) {
            title = postDto.title
            description = postDto.description
            content = postDto.content
            postRepository.save(this).mapToDto()
        }.also { logger.info("updatePostById() get called") }

    override fun deletePostById(id: Long) =
        postRepository.findById(id).orElseThrow { postNotFoundException(id) }.let { postRepository.delete(it) }
            .also { logger.info("deletePostById() get called") }

    private fun postNotFoundException(id: Long) = ResourceNotFoundException("Post", "id", id)

    private fun Post.mapToDto() = modelMapper.map(this, PostDto::class.java)
}
