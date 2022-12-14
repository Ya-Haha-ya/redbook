package com.xiazhao.redbook.controller

import com.xiazhao.redbook.payload.PostDto
import com.xiazhao.redbook.service.PostService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/posts")
class PostController(private val postService: PostService) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllPosts() = ResponseEntity.ok(postService.getAllPosts())

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Create a new post with PostDto", description = "Creates a new post with PostDto.")
    @Parameter(name = "postDto", description = "Post details with title, description and content.")
    @ApiResponse(responseCode = "201", description = "New post created successful")
    fun createPost(
        @Valid
        @RequestBody
        postDto: PostDto
    ) = ResponseEntity(postService.createPost(postDto), HttpStatus.CREATED)

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getPostById(@PathVariable(name = "id") id: Long) = ResponseEntity.ok(postService.getPostById(id))

    @PutMapping("/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updatePostById(@PathVariable(name = "id") id: Long, @Valid @RequestBody postDto: PostDto) =
        ResponseEntity.ok(postService.updatePostById(id, postDto))

    @DeleteMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deletePostById(@PathVariable(name = "id") id: Long) =
        ResponseEntity.ok("Post deleted").also { postService.deletePostById(id) }

}
