package com.xiazhao.redbook.payload

import java.util.*

data class ErrorDetails(
    val timestamp: Date,
    val message: String,
    val details: String,
)
