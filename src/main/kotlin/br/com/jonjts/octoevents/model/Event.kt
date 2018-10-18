package br.com.jonjts.octoevents.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity(name = "events")
class Event(
        @Expose(serialize = false, deserialize = false)
        @Column(name = "issue_id")
        val issueId: Long,

        @Expose
        val action: String,

        @Expose()
        @SerializedName("created_at")
        @Column(name = "created_at")
        val createdAt: java.sql.Timestamp,

        @Expose(serialize = false)
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = -1) {

    private constructor() : this(-1, "", Timestamp.valueOf(LocalDateTime.now()))
}