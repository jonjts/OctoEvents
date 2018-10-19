package br.com.jonjts.octoevents

import org.glassfish.jersey.server.ResourceConfig
import org.springframework.context.annotation.Configuration

@Configuration
open class JerseyConfig : ResourceConfig() {
    init {
        this.packages("br.com.jonjts.octoevents.api")
    }
}