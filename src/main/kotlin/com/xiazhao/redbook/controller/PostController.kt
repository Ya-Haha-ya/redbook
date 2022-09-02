package com.xiazhao.redbook.controller

import com.xiazhao.redbook.payload.PostDto
import com.xiazhao.redbook.service.PostService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts")
class PostController(@Autowired private val postService: PostService) {

    @GetMapping
    fun getAllPosts() = ResponseEntity.ok(postService.getAllPosts())

    @PostMapping
    fun createPost(@RequestBody postDto: PostDto) = ResponseEntity(postService.createPost(postDto), HttpStatus.CREATED)
}
