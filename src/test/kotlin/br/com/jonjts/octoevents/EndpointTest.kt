package br.com.jonjts.octoevents

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.http.*


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EndpointTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun `Endpoint not found`() {
        val result = testRestTemplate.getForEntity("/not/found", String::class.java)
        assertEquals(result.statusCode, HttpStatus.NOT_FOUND)
    }

    @Test
    fun `Events endpoint found`() {
        val result = testRestTemplate.getForEntity("/issues/-1/events", String::class.java)
        assertNotNull(result)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertEquals(result.body, "[]")
    }

    @Test
    fun `Payload endpoint found`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity = HttpEntity<String>("{}", headers)

        val result = testRestTemplate.postForEntity("/payload", entity, String::class.java)
        assertNotNull(result)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertEquals(result.body, "I'm here")
    }

}