package io.orbeon.wrapper.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.RestTemplate
import java.io.IOException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest


@Configuration
class RestTemplateConfig {
    @Autowired
    val httpServletRequest: HttpServletRequest? = null

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder
            .additionalInterceptors(AuthorizationInterceptor())
            .build()
    }

    private inner class AuthorizationInterceptor : ClientHttpRequestInterceptor {
        @Throws(IOException::class)
        override fun intercept(
            request: HttpRequest,
            body: ByteArray,
            execution: ClientHttpRequestExecution
        ): ClientHttpResponse {
            var token = ""
            val session = httpServletRequest!!.session
            val cookies = httpServletRequest!!.cookies
            val project = session.getAttribute("projectId") as String?

            if (cookies != null) {
                for (cookie: Cookie in cookies) {
                    if (cookie.name.uppercase() == HttpHeaders.AUTHORIZATION.uppercase()) {
                        token = cookie.value
                        break
                    }
                }
            }
            if (project != null) {
                request.headers.add("ProjectId", project)
            }

            request.headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.type)
            request.headers.add(HttpHeaders.AUTHORIZATION, "OAuth $token")

            return execution.execute(request, body)
        }
    }
}