package demo.consul.client.provider.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject


@SpringBootTest
internal class HelloWorldControllerTest {

    @Autowired
    lateinit var loadBalancedRest: RestTemplate

    @Autowired
    lateinit var discoveryClient: DiscoveryClient

    @Autowired
    lateinit var discoveryClientRest: RestTemplate

    @Test
    fun `test hello world`() {
        for (i in 1..100) {
            Thread.sleep(1000)
            try {
                val response = loadBalancedRest.getForObject<Map<String, String>>("/hello")
                assertThat(response["data"]).isNotEmpty
                println("$i.succeed")
            } catch (e: Exception) {
                println("$i.failed")
            }
        }
    }

    @Test
    fun serviceUrl() {
        for (i in 1..100) {
            Thread.sleep(1000)
            try {
                val list = discoveryClient.getInstances("consul-client-provider").map {
                    it.uri
                }
                val response = discoveryClientRest.getForObject<Map<String, String>>("${list.random()}/hello")
                assertThat(response["data"]).isEqualTo("Hello World~")
                println("$i.succeed, size: ${list.size}, nodes: $list")
            } catch (e: Exception) {
                println("$i.failed")
            }
        }
    }


}