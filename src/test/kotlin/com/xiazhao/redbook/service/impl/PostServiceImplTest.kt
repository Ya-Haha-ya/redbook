package com.xiazhao.redbook.service.impl

import com.xiazhao.redbook.dao.PostRepository
import com.xiazhao.redbook.data.TestDataSet.DEFAULT_POSTS
import com.xiazhao.redbook.data.TestDataSet.DEFAULT_POST_DTOS
import com.xiazhao.redbook.data.TestDataSet.POST_DTO_WITHOUT_ID
import com.xiazhao.redbook.data.TestDataSet.POST_WITH_ID
import com.xiazhao.redbook.entity.Post
import com.xiazhao.redbook.exception.ResourceNotFoundException
import com.xiazhao.redbook.payload.PostDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.dao.DataIntegrityViolationException
import java.util.*

@SpringBootTest
@MockBean(PostRepository::class)
internal class PostServiceImplTest @Autowired constructor(
    private val postRepository: PostRepository,
    private val postServiceImpl: PostServiceImpl,
) {

    lateinit var postDatabase: MutableMap<Long?, Post>

    @BeforeEach
    fun setUp() {
        postDatabase = DEFAULT_POSTS.associateBy { it.id }.toMutableMap()
    }

    @Test
    @DisplayName("getAllPosts_NoParameters_ReturnsPostDtoList")
    fun getAllPosts_NoParameters_ReturnsPostDtoList() {
        `when`(postRepository.findAll()).thenReturn(DEFAULT_POSTS)
        val allPostDtos = postServiceImpl.getAllPosts()
        assertEquals(DEFAULT_POSTS.size, allPostDtos.size)
        allPostDtos.zip(DEFAULT_POSTS) { postDto, post ->
            assertEquals(postDto, post)
        }
    }

    @Test
    fun createPost_WithValidPostDto_ReturnSavedPostDtoWithId() {
        val responsePost = POST_WITH_ID
        val requestPostDto = POST_DTO_WITHOUT_ID
        `when`(postRepository.save(any(Post::class.java))).thenReturn(responsePost)
        val responsePostDto = postServiceImpl.createPost(requestPostDto)
        assertEquals(responsePostDto, responsePost)
    }

    @Test
    fun createPost_TitleDuplicated_ThrowException() {
        `when`(postRepository.save(any(Post::class.java))).thenThrow(DataIntegrityViolationException(""))
        assertThrows<DataIntegrityViolationException> {
            postServiceImpl.createPost(DEFAULT_POST_DTOS[0])
        }
    }

    @Test
    fun getPostById_withValidId_ReturnPostDtoWithSameId() {
        val responsePost = DEFAULT_POSTS[0]
        `when`(postRepository.findById(anyLong())).thenReturn(Optional.of(responsePost))
        val postDto = postServiceImpl.getPostById(1)
        assertEquals(postDto, responsePost, isCheckId = true)
    }

    @Test
    fun getPostById_WithInvalidId_ThrowException() {
        `when`(postRepository.findById(anyLong())).thenReturn(Optional.empty())
        assertThrows<ResourceNotFoundException> { postServiceImpl.getPostById(1) }
    }

    @Test
    fun updatePostById_WithValidId_ReturnPostDtoWithSameIdAndPostContents() {
        val responsePost = DEFAULT_POSTS[0]
        `when`(postRepository.findById(anyLong())).thenReturn(Optional.of(responsePost))
        `when`(postRepository.save(any(Post::class.java))).thenReturn(POST_WITH_ID.copy().apply { id = responsePost.id })
        val responsePostDto = postServiceImpl.updatePostById(responsePost.id ?: 0, POST_DTO_WITHOUT_ID)
        assertEquals(responsePost.id, responsePostDto.id)
        assertEquals(responsePostDto, POST_DTO_WITHOUT_ID)
        verify(postRepository, times(1)).save(any())
    }

    @Test
    fun updatePostById_WithInvalidId_ThrowException() {
        val responsePost = DEFAULT_POSTS[0]
        `when`(postRepository.findById(anyLong())).thenReturn(Optional.empty())
        assertThrows<ResourceNotFoundException> {
            postServiceImpl.updatePostById(responsePost.id ?: 0, POST_DTO_WITHOUT_ID)
        }
        verify(postRepository, times(0)).save(any())
    }

    @Test
    fun deletePostById_WithValidId_NothingHappen() {
        val requestPost = DEFAULT_POSTS[0]
        `when`(postRepository.findById(anyLong())).thenReturn(Optional.of(requestPost))
        postServiceImpl.deletePostById(1)
        verify(postRepository, times(1)).delete(any())
    }
    @Test
    fun deletePostById_WithInvalidId_ThrowException() {
        `when`(postRepository.findById(anyLong())).thenReturn(Optional.empty())
        assertThrows<ResourceNotFoundException> { postServiceImpl.deletePostById(1) }
        verify(postRepository, times(0)).delete(any())
    }

    private fun assertEquals(postDto: PostDto, post: Post, isCheckId: Boolean = false) {
        if (isCheckId) assertEquals(post.id, postDto.id)
        assertEquals(post.title, postDto.title)
        assertEquals(post.description, postDto.description)
        assertEquals(post.content, postDto.content)
    }

    private fun assertEquals(
        responsePostDto: PostDto,
        requestPostDto: PostDto,
        isCheckId: Boolean = false,
    ) {
        if (isCheckId) assertEquals(requestPostDto.id, responsePostDto.id)
        else assertNotNull(responsePostDto.id)
        assertEquals(requestPostDto.title, responsePostDto.title)
        assertEquals(requestPostDto.description, responsePostDto.description)
        assertEquals(requestPostDto.content, responsePostDto.content)
    }
}
