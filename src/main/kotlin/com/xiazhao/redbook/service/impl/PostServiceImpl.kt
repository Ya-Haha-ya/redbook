package com.xiazhao.redbook.service.impl

import com.xiazhao.redbook.dao.PostRepository
import com.xiazhao.redbook.entity.Post
import com.xiazhao.redbook.exception.ResourceNotFoundException
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

    override fun getAllPosts(): List<PostDto> = postRepository.findAll().map { it.mapToDto() }

    override fun createPost(postDto: PostDto): PostDto =
        postRepository.save(modelMapper.map(postDto, Post::class.java)).mapToDto()

    override fun getPostById(id: Long): PostDto =
        postRepository.findById(id).orElseThrow { postNotFoundException(id) }.mapToDto()

    override fun updatePostById(id: Long, postDto: PostDto): PostDto =
        with(postRepository.findById(id).orElseThrow { postNotFoundException(id) }) {
            title = postDto.title
            description = postDto.description
            content = postDto.content
            postRepository.save(this).mapToDto()
        }

    override fun deletePostById(id: Long) =
        postRepository.findById(id).orElseThrow { postNotFoundException(id) }.let { postRepository.delete(it) }

    private fun postNotFoundException(id: Long) = ResourceNotFoundException("Post", "id", id)

    private fun Post.mapToDto() = modelMapper.map(this, PostDto::class.java)
}
