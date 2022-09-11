package com.xiazhao.redbook.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class RootController {
    @GetMapping
    fun hello(): String {
        return "hello from Redbook with Kotlin"
    }
}
