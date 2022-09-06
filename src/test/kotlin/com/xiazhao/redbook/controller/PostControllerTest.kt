package com.xiazhao.redbook.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.xiazhao.redbook.data.TestDataSet.DEFAULT_POST_DTOS_WITH_ID
import com.xiazhao.redbook.payload.PostDto
import com.xiazhao.redbook.service.PostService
import io.mockk.every
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
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
        val resultAction = mockMvc.perform(get("/api/posts").accept(MediaType.APPLICATION_JSON))
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
    fun createPost_withInvalidRequestBody_returnResponseEntityWithErrorMessage() {
        val postDto = PostDto(title = "1", description = "description", content = "content")
        mockMvc.perform(post("/api/posts")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(postDto))
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("\$.title").value(PostDto.TITLE_RESTRICTION_MIN_LENGTH_ERROR_MSG))
    }
}
