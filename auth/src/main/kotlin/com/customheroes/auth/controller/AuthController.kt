package com.customheroes.auth.controller

import com.customheroes.auth.model.dto.TokensDto
import com.customheroes.auth.model.dto.TokensResponseDto
import com.customheroes.auth.model.dto.UserInfoDto
import com.customheroes.auth.model.dto.UserRequestDto
import com.customheroes.auth.model.postgres.User
import com.customheroes.auth.repository.UserRepository
import com.customheroes.auth.util.Constants.CLIENT_ID
import com.customheroes.auth.util.Constants.CLIENT_SECRET
import com.customheroes.auth.util.Constants.KEYCLOAK_URL
import com.customheroes.auth.util.toUserInfoDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate


@RestController
class AuthController {

    var passwordEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Autowired
    private var userRepository: UserRepository? = null

    /** эндпоинт обновления access по refresh */
    @GetMapping("/refresh")
    fun update(@RequestParam(name="refresh_token", required = true) refreshToken: String): TokensDto {
        val restTemplate = RestTemplate()
        val headers = org.springframework.http.HttpHeaders()

        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val body: MultiValueMap<String, String> = LinkedMultiValueMap()
        body.add("refresh_token", refreshToken)
        body.add("client_id", CLIENT_ID)
        body.add("grant_type", "refresh_token")
        body.add("client_secret", CLIENT_SECRET)

        val requestEntity = HttpEntity(body, headers)
        val responseEntity: ResponseEntity<TokensResponseDto> = restTemplate.exchange(
            KEYCLOAK_URL,
            HttpMethod.POST,
            requestEntity,
            TokensResponseDto::class.java
        )

        val response = responseEntity.body
        return TokensDto(response?.accessToken ?: "", response?.refreshToken ?: "")
    }

    /** эндпоинт регистрации пользователя */
    @PostMapping("/register")
    fun registerUser(@RequestBody user: UserRequestDto) {
        val nonNullUserRepository = userRepository ?: return
        val users = nonNullUserRepository.findByLogin(user.login)
        if (users == null) {
            val hashedPassword = passwordEncoder.encode(user.password)
            val postgresUser = User(0, user.login, hashedPassword, "customer", "-1", user.fullName, "")
            nonNullUserRepository.save(postgresUser)
            val addedUser = nonNullUserRepository.findByLogin(postgresUser.login ?: "")
        }
    }

}