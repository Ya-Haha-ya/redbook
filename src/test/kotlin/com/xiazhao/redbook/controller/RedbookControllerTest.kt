package com.xiazhao.redbook.controller

import com.xiazhao.redbook.controller.RedbookController.Companion.OK_STATUS
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest
internal class RedbookControllerTest(
    @Autowired val redbookController: RedbookController
) {

    @Test
    fun testStatus() {
        val statusEntity = redbookController.status()
        assertEquals(statusEntity.statusCode, HttpStatus.OK)
        assertEquals(statusEntity.body, OK_STATUS)
    }

}
