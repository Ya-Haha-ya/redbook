package com.xiazhao.redbook.payload

import javax.validation.constraints.Size

data class PostDto(
    var id: Long? = null,
    @field:Size(min = TITLE_RESTRICTION_MIN_LENGTH, message = TITLE_RESTRICTION_MIN_LENGTH_ERROR_MSG)
    var title: String = "",
    var description: String? = null,
    var content: String? = null,
) {
    companion object {
        const val TITLE_RESTRICTION_MIN_LENGTH = 2
        const val TITLE_RESTRICTION_MIN_LENGTH_ERROR_MSG = "Post title should have at least $TITLE_RESTRICTION_MIN_LENGTH characters"
    }
}
