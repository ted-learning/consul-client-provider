package demo.consul.client.provider

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
class ConsulClientProviderApplication

fun main(args: Array<String>) {
	runApplication<ConsulClientProviderApplication>(*args)
}
