package com.xiazhao.redbook.payload

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class PostDto(
    var id: Long? = null,
    @field:Size(min = TITLE_RESTRICTION_MIN_LENGTH, message = TITLE_RESTRICTION_MIN_LENGTH_ERROR_MSG)
    var title: String = "",
    @field:Size(min = DESCRIPTION_MIN_LENGTH, message = DESCRIPTION_MIN_LENGTH_ERROR_MSG)
    var description: String? = null,
    @field:NotBlank(message = CONTENT_ERROR_MESSAGE)
    var content: String? = null,
) {
    companion object {
        const val TITLE_RESTRICTION_MIN_LENGTH = 2
        const val TITLE_RESTRICTION_MIN_LENGTH_ERROR_MSG = "Post title should have at least $TITLE_RESTRICTION_MIN_LENGTH characters"
        const val DESCRIPTION_MIN_LENGTH = 2
        const val DESCRIPTION_MIN_LENGTH_ERROR_MSG = "Post description should have at least $DESCRIPTION_MIN_LENGTH"
        const val CONTENT_ERROR_MESSAGE = "Post content should not be blank"
    }
}
