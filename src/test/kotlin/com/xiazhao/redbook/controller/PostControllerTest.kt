package com.xiazhao.redbook.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.xiazhao.redbook.data.TestDataSet.DEFAULT_POST_DTOS_WITH_ID
import com.xiazhao.redbook.exception.ResourceNotFoundException
import com.xiazhao.redbook.payload.PostDto
import com.xiazhao.redbook.service.PostService
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest
internal class PostControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper,
) {

    @MockkBean
    private lateinit var postService: PostService

    @Test
    fun getAllPosts_NoParameters_ReturnsPostDtoList() {
        every { postService.getAllPosts() } returns DEFAULT_POST_DTOS_WITH_ID
        val resultAction = mockMvc.perform(get("/api/posts")
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        DEFAULT_POST_DTOS_WITH_ID.forEachIndexed { index, postDto ->
            resultAction.andExpect(jsonPath("\$.[$index].id").value(postDto.id))
            .andExpect(jsonPath("\$.[$index].title").value(postDto.title))
            .andExpect(jsonPath("\$.[$index].description").value(postDto.description))
            .andExpect(jsonPath("\$.[$index].content").value(postDto.content))
        }
    }

    @Test
    fun createPost_WithInvalidRequestBody_ReturnResponseEntityWithErrorMessage() {
        val postDto = PostDto(title = "1", description = "description", content = "content")
        mockMvc.perform(post("/api/posts")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(postDto))
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("\$.title").value(PostDto.TITLE_RESTRICTION_MIN_LENGTH_ERROR_MSG))
    }

    @Test
    fun createPost_WithValidRequestBody_ReturnResponseEntityWithOk() {
        val requestPostDto = PostDto(title = "title", description = "this is description", content = "go to uk")
        val responsePostDto = requestPostDto.copy(id = 1)
        every { postService.createPost(any()) } returns responsePostDto
        mockMvc.perform(post("/api/posts")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(requestPostDto))
        ).andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(responsePostDto.id))
            .andExpect(jsonPath("\$.title").value(responsePostDto.title))
            .andExpect(jsonPath("\$.description").value(responsePostDto.description))
            .andExpect(jsonPath("\$.content").value(responsePostDto.content))
    }

    @Test
    fun getPost_WithValidId_ReturnPostDto() {
        val responsePostDto = PostDto(id = 1, title = "title", description = "this is description", content = "go to uk")
        every { postService.getPostById(any()) } returns responsePostDto
        mockMvc.perform(get("/api/posts/1")
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(1))
            .andExpect(jsonPath("\$.title").value(responsePostDto.title))
            .andExpect(jsonPath("\$.description").value(responsePostDto.description))
            .andExpect(jsonPath("\$.content").value(responsePostDto.content))
    }

    @Test
    fun getPost_WithInvalidId_ThrowException() {
        every { postService.getPostById(any()) }.throws(ResourceNotFoundException("Post", "id", 1))
        mockMvc.perform(get("/api/posts/1")
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound)
            .andExpect { assertTrue(it.resolvedException is ResourceNotFoundException) }
    }

    @Test
    fun updatePost_WithValidIdAndValidPostDto_ReturnPostDto() {
        val requestPostDto = PostDto(title = "apple", description = "this is description", content = "this is content")
        val responsePostDto = requestPostDto.copy(id = 1)
        every { postService.updatePostById(any(), any()) } returns responsePostDto
        mockMvc.perform(put("/api/posts/1")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(requestPostDto))
        ).andExpect { status().isOk }
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(responsePostDto.id))
            .andExpect(jsonPath("\$.title").value(responsePostDto.title))
            .andExpect(jsonPath("\$.description").value(responsePostDto.description))
            .andExpect(jsonPath("\$.content").value(responsePostDto.content))
    }

    @Test
    fun updatePost_WithValidIdAndInvalidPostDto_ThrowException() {
        val requestPostDto = PostDto(title = "h", description = "h", content = "")
        mockMvc.perform(put("/api/posts/1")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(requestPostDto))
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("\$.title").value(PostDto.TITLE_RESTRICTION_MIN_LENGTH_ERROR_MSG))
            .andExpect { jsonPath("\$.description").value(PostDto.TITLE_RESTRICTION_MIN_LENGTH_ERROR_MSG) }
            .andExpect(jsonPath("\$.content").value(PostDto.CONTENT_ERROR_MESSAGE))
    }

    @Test
    fun updatePost_WithInvalidIdAndValidPostDto_ThrowException() {
        val requestPostDto = PostDto(title = "hello", description = "hello", content = "hello")
        every { postService.updatePostById(any(), requestPostDto) }.throws(ResourceNotFoundException("Post", "id", 1))
        mockMvc.perform(put("/api/posts/1")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(requestPostDto))
        ).andExpect{ status().isNotFound }
            .andExpect { assertTrue(it.resolvedException is ResourceNotFoundException) }
    }

    @Test
    fun deletePost_WithValidId_NothingHappen() {
        every { postService.deletePostById(any()) }.returns(Unit)
        mockMvc.perform(delete("/api/posts/1")
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk)
    }
}
