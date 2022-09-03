package com.xiazhao.redbook.service.impl

import com.xiazhao.redbook.dao.PostRepository
import com.xiazhao.redbook.entity.Post
import com.xiazhao.redbook.exception.ResourceNotFoundException
import com.xiazhao.redbook.payload.PostDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException

/**
 * The tests are still failing due to the memory database not get cleared after each test case.
 * Need to rework on this once resolves the issue.
 */
@Disabled
@SpringBootTest
internal class PostServiceImplTest @Autowired constructor(
    private val postRepository: PostRepository,
    private val postServiceImpl: PostServiceImpl,
) {
    companion object {
        val DEFAULT_POSTS = listOf(
            Post(id = null, title = "title1", description = "desc1", content = "content1"),
            Post(id = null, title = "title2", description = "desc2", content = "content2"),
            Post(id = null, title = "title3", description = "desc3", content = "content3"),
            Post(id = null, title = "title4", description = "desc4", content = "content4"),
            Post(id = null, title = "title5", description = "desc5", content = "content5"),
        )
    }

    @BeforeEach
    fun setUp() {
        DEFAULT_POSTS.forEach { postRepository.save(it) }
    }

    @Test
    fun getAllPosts() {
        val allPostDtos = postServiceImpl.getAllPosts()
        assertEquals(DEFAULT_POSTS.size, allPostDtos.size)
        allPostDtos.zip(DEFAULT_POSTS) { postDto, post ->
            assertEquals(postDto, post)
        }
    }

    @Test
    fun createPost() {
        val requestPostDto = PostDto(title = "apple", description = "this", content = "hello world")
        val responsePostDto = postServiceImpl.createPost(requestPostDto)
        assertEquals(responsePostDto, requestPostDto)

        assertThrows<DataIntegrityViolationException> {
            postServiceImpl.createPost(requestPostDto)
        }
    }

    @Test
    fun getPostById() {
        DEFAULT_POSTS.forEachIndexed { index, post ->
            val postDto = postServiceImpl.getPostById(index.toLong() + 1)
            assertEquals(postDto, post)
        }

        assertThrows<ResourceNotFoundException> { postServiceImpl.getPostById(0) }
        assertThrows<ResourceNotFoundException> { postServiceImpl.getPostById(DEFAULT_POSTS.size.toLong() + 1) }
    }

    @Test
    fun updatePostById() {
        val requestPostDto = PostDto(title = "orange", description = "this", content = "hello world")
        val responsePostDto = postServiceImpl.updatePostById(1, requestPostDto)
        assertEquals(1, responsePostDto.id)
        assertEquals(responsePostDto, requestPostDto)

        assertThrows<ResourceNotFoundException> { postServiceImpl.updatePostById(0, requestPostDto) }
    }

    @Test
    fun deletePostById() {
        postServiceImpl.deletePostById(1)
        assertThrows<ResourceNotFoundException> { postServiceImpl.getPostById(1) }
        assertThrows<ResourceNotFoundException> { postServiceImpl.deletePostById(1) }
    }

    private fun assertEquals(postDto: PostDto, post: Post) {
        assertNotNull(postDto.id)
        assertEquals(post.title, postDto.title)
        assertEquals(post.description, postDto.description)
        assertEquals(post.content, postDto.content)
    }

    private fun assertEquals(
        responsePostDto: PostDto,
        requestPostDto: PostDto
    ) {
        assertNotNull(responsePostDto.id)
        assertEquals(requestPostDto.title, responsePostDto.title)
        assertEquals(requestPostDto.description, responsePostDto.description)
        assertEquals(requestPostDto.content, responsePostDto.content)
    }
}
