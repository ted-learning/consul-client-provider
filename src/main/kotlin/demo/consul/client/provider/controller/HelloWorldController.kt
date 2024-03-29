package demo.consul.client.provider.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class HelloWorldController {

    @GetMapping("/hello")
    fun hello(): Map<String,String>{
//        Thread.sleep(3_000)
        return mapOf("data" to "Hello, I'm consul-client-provider")
    }
}