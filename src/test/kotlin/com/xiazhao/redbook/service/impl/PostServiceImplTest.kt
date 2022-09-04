package com.xiazhao.redbook.service.impl

import com.xiazhao.redbook.dao.PostRepository
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
    companion object {
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

        val POST_DTO_WITHOUT_ID = PostDto(title = "apple", description = "this", content = "hello world")
        val POST_WITH_ID = Post(id = 6, title = "apple", description = "this", content = "hello world")
    }

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
