package com.xiazhao.redbook.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/status")
class RedbookController {

    companion object {
        const val OK_STATUS = "Server is up"
    }

    @GetMapping
    fun status() = ResponseEntity.ok(OK_STATUS)

}
