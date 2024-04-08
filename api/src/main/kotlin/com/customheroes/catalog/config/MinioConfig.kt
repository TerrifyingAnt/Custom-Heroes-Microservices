package com.customheroes.catalog.config

import io.minio.MinioClient
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.InetSocketAddress
import java.net.Proxy


@Configuration
class MinioConfig {
    @Value("\${minio.url}")
    private val url: String? = null

    @Value("\${minio.accessKey}")
    private val accessKey: String? = null

    @Value("\${minio.secretKey}")
    private val secretKey: String? = null

    @Value("\${proxy.host}")
    private val proxyHost: String? = null

    @Value("\${minio.port}")
    private val proxyPort: Int? = null

    @Bean
    fun minioClient(): MinioClient {
        val httpClient = if (proxyHost != null && proxyPort != null) {
            val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(proxyHost, proxyPort))
            OkHttpClient.Builder()
                .proxy(proxy)
                .build()
        } else {
            OkHttpClient()
        }

        return MinioClient.builder()
            .endpoint(url)
            .credentials(accessKey, secretKey)
            .httpClient(httpClient)
            .build()
    }
}