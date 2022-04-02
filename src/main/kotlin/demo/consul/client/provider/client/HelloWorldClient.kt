package demo.consul.client.provider.client

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate


@Configuration
class HelloWorldClient {
    @LoadBalanced
    @Bean
    fun loadBalancedRest(): RestTemplate {
        return RestTemplateBuilder().rootUri("http://consul-client-provider").build()
    }

    @Bean
    fun discoveryClientRest(): RestTemplate {
        RestTemplateBuilder().buildRequestFactory()
        return RestTemplateBuilder().rootUri("http://consul-client-provider").build()
    }
}