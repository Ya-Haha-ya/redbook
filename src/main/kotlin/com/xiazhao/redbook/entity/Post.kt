package com.xiazhao.redbook.entity

import com.xiazhao.redbook.entity.Post.Companion.COLUMN_TITLE
import com.xiazhao.redbook.entity.Post.Companion.TABLE_NAME
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = TABLE_NAME, uniqueConstraints = [UniqueConstraint(columnNames = [COLUMN_TITLE])])
data class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Column(name = COLUMN_TITLE, nullable = false)
    var title: String,

    @Column(name = COLUMN_DESCRIPTION)
    private val description: String?,

    @Column(name = COLUMN_CONTENT)
    private val content: String?,

    @CreationTimestamp
    @Column(name = COLUMN_CREATE_DATA_TIME)
    private val createDateTime: LocalDateTime,

    @UpdateTimestamp
    @Column(name = COLUMN_UPDATE_DATA_TIME)
    private val updateDateTime: LocalDateTime,
) {
    companion object {
        const val TABLE_NAME = "posts"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_CREATE_DATA_TIME = "create_data_time"
        const val COLUMN_UPDATE_DATA_TIME = "update_data_time"
    }
}