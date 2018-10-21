package br.com.jonjts.octoevents.repository

import br.com.jonjts.octoevents.model.Event
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

@Repository
interface EventRepository : CrudRepository<Event, Long> {

    @Query("select count(*) > 0 from events  where issue_id = :issue_id AND action LIKE :action")
    fun exist(@Param("issue_id") issueId: Long, @Param("action") action: String): Boolean

    fun getByIssueId(issueId: Long):List<Event>
}

