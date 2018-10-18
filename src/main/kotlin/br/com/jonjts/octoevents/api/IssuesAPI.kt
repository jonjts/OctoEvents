package br.com.jonjts.octoevents.api

import br.com.jonjts.octoevents.model.Event
import br.com.jonjts.octoevents.repo.EventRepository
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.LocalDateTime
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import okhttp3.HttpUrl
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
    fun load(msg: String): String {
        var jsonParser = JsonParser()
        var jsonObject = jsonParser.parse(msg).asJsonObject
        if(!jsonObject.has("action")){
            return "I'm here"
        }

        var action = jsonObject.get("action").asString
        var issue = jsonObject.getAsJsonObject("issue")
        var issueId = issue.get("id").asLong
        //var createdAt = convertDateTime(issue.get("created_at").asString)

        repository.save(Event(issueId, action, Timestamp.valueOf(LocalDateTime.now())))
        return msg.toString()
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

    @POST
    @Path("/issues/init")
    @Produces(MediaType.APPLICATION_JSON)
    fun init(@HeaderParam("user") user: String,
             @HeaderParam("password") password: String,
             @HeaderParam("payload_url") url: String) : String{

        var json = createJsonToNewHook(url)

        var urlApi = HttpUrl.Builder().scheme("https").
                host("api.github.com").
                addPathSegment("repos").
                addPathSegment("jonjts").
                addPathSegment("MeuNegocio").
                addPathSegment("hooks").
                build()

        val body = RequestBody.create(okhttp3.MediaType.parse("application/json"), json.toString())
        var credentials = Credentials.basic(user, password)
        var request = Request.Builder().
                url(urlApi).
                post(body).
                addHeader("authorization", credentials)
                .build()

        var client = OkHttpClient()
        var response = client.newCall(request).execute()

        return response.body()!!.string()
    }

    private fun createJsonToNewHook(url: String) : JsonObject {
        var events = JsonArray()
        events.add("issues")

        var config = JsonObject()
        config.addProperty("url", url+"/payload")
        config.addProperty("content_type", "json")

        var json = JsonObject();
        json.addProperty("name", "web")
        json.addProperty("active", true)
        json.add("events", events)
        json.add("config", config)

        return json
    }

}