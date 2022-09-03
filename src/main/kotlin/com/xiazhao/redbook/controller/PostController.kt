package com.xiazhao.redbook.controller

import com.xiazhao.redbook.payload.PostDto
import com.xiazhao.redbook.service.PostService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts")
class PostController(private val postService: PostService) {

    @GetMapping
    fun getAllPosts() = ResponseEntity.ok(postService.getAllPosts())

    @PostMapping
    fun createPost(@RequestBody postDto: PostDto) = ResponseEntity(postService.createPost(postDto), HttpStatus.CREATED)

    @GetMapping("/{id}")
    fun getPostById(@PathVariable(name = "id") id: Long) = ResponseEntity.ok(postService.getPostById(id))

    @PutMapping("/{id}")
    fun updatePostById(@PathVariable(name = "id") id: Long, @RequestBody postDto: PostDto) =
        ResponseEntity.ok(postService.updatePostById(id, postDto))

    @DeleteMapping("/{id}")
    fun deletePostById(@PathVariable(name = "id") id: Long) =
        ResponseEntity.ok("Post deleted").also { postService.deletePostById(id) }

}
