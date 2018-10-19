package br.com.jonjts.octoevents


import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.*
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import java.util.logging.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment


@Component
class WebhookConfig {

    private val log = Logger.getLogger(WebhookConfig::class.qualifiedName)

    @Autowired
    private val env: Environment? = null

    @PostConstruct
    fun init() {
        //Create a new webhook
        createNewWebhook()
    }

    /*
    Create a new webhook using parameter defined in application.properties.
    If exist a webhook with the same payload url defined in application.properties,<br>
    the webhook will not be created
     */
    private fun createNewWebhook(){
        var json = createJsonToNewHook()

        var urlApi = HttpUrl.Builder().scheme("https").
                host("api.github.com").
                addPathSegment("repos").
                addPathSegment(env!!.getProperty("hook.repository_owner", "")).
                addPathSegment(env!!.getProperty("hook.repository", "")).
                addPathSegment("hooks").
                build()

        val body = RequestBody.create(okhttp3.MediaType.parse("application/json"), json.toString())
        var credentials = Credentials.basic(env!!.getProperty("hook.user",""),
                env!!.getProperty("hook.password", ""))
        var request = Request.Builder().
                url(urlApi).
                post(body).
                addHeader("authorization", credentials)
                .build()

        var client = OkHttpClient()
        var response = client.newCall(request).execute()
        log.info(response.toString())
    }

    private fun createJsonToNewHook() : JsonObject {
        var events = JsonArray()
        events.add("issues")

        var config = JsonObject()
        config.addProperty("url", env!!.getProperty("hook.payload_url","")+"/payload")
        config.addProperty("content_type", "json")

        var json = JsonObject()
        json.addProperty("name", "web")
        json.addProperty("active", true)
        json.add("events", events)
        json.add("config", config)

        return json
    }
}