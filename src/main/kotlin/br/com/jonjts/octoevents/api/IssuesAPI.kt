package br.com.jonjts.octoevents.api

import br.com.jonjts.octoevents.model.Event
import br.com.jonjts.octoevents.repository.EventRepository
import com.google.gson.JsonParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.LocalDateTime
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import com.google.gson.GsonBuilder


@Component
@Path("/")
class IssuesAPI {

    @Autowired
    lateinit var repository: EventRepository

    @POST
    @Path("/payload")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun payload(msg: String): String {
        var jsonParser = JsonParser()
        var jsonObject = jsonParser.parse(msg).asJsonObject
        if (!jsonObject.has("action")) {
            return "I'm here"
        }
        var action = jsonObject.get("action").asString
        var issue = jsonObject.getAsJsonObject("issue")
        var issueId = issue.get("id").asLong

        repository.save(Event(issueId, action, Timestamp.valueOf(LocalDateTime.now())))
        return "Payload received"
    }


    @GET
    @Path("/issues/{id}/events")
    @Produces(MediaType.APPLICATION_JSON)
    fun getEvents(@PathParam("id") id: Long): String {
            val builder = GsonBuilder()
            builder.excludeFieldsWithoutExposeAnnotation()
            val gson = builder.create()
            return gson.toJson(repository.getByIssueId(id))
    }

}