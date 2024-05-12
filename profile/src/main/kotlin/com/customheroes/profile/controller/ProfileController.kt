package com.customheroes.profile.controller

import com.customheroes.profile.model.dto.UserInfoDto
import com.customheroes.profile.repository.UserRepository
import com.customheroes.profile.util.toUserInfoDto
import io.minio.*
import io.minio.errors.MinioException
import io.minio.http.Method
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.TimeUnit


@RestController
class ProfileController {

    @Autowired
    val userRepository: UserRepository? = null

    @Value("\${minio.bucketName}")
    private val bucketName: String? = null

    @Autowired
    var minioClient: MinioClient? = null

    @GetMapping("/me")
    fun getMe(@RequestHeader("x-user") userId: String): UserInfoDto? {
        val postgresUserId = userId.split(":").last().toInt()
        val nonNullUserRepository = userRepository ?: return null
        val postgresUser = nonNullUserRepository.findById(postgresUserId).get()
        val getPreassignedLink = getImagesLinks(postgresUser.login ?: "") ?: ""
        return postgresUser.toUserInfoDto(getPreassignedLink)
    }

    @GetMapping("/work_update_avatar")
    fun workUpdateAvatar(@RequestHeader("x-user") userId: String): Map<String, String>? {
        val link = getPreassignedLinkForUploading(userId.split(":").last())
        return link
    }

    @PostMapping("/update_user")
    fun updateUser(@RequestHeader("x-user") userId: String, @RequestParam(name = "user_name") userName: String, @RequestParam(name = "user_phone") userPhone: String) {
        val nonNullUserRepository = userRepository ?: return
        if (userName != "") {
            nonNullUserRepository.updateUserName(userId.split(":").last().toInt(), userName)
        }
        if (userPhone != "") {
            nonNullUserRepository.updateUserPhone(userId.split(":").last().toInt(), userPhone)
        }
    }

    /** получение ссылок на 10 минут для аватарки*/
    private fun getImagesLinks(userLogin: String): String? {
        val nonNullMinioClient = minioClient ?: return null
        val reqParams: MutableMap<String, String> = HashMap()

        val objects = nonNullMinioClient.listObjects(ListObjectsArgs.builder()
            .bucket(bucketName)
            .prefix(userLogin)
            .recursive(true)
            .build())


        reqParams["response-content-type"] = "image/*"
        if (objects.count() > 0) {
            val url: String = nonNullMinioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .`object`(objects.first().get().objectName())
                    .expiry(10, TimeUnit.MINUTES)
                    .extraQueryParams(reqParams)
                    .build()
            )
            println(url)
            return url
        }
        return ""
    }

    /** получение ссылок на кучу часов для загрузки автарки*/
    private fun getPreassignedLinkForUploading(userId: String): Map<String, String>? {
        val nonNullUserRepository = userRepository ?: return null
        val userLogin = nonNullUserRepository.findById(userId.toInt()).get().login
        val nonNullMinioClient = minioClient ?: return null
        val policy = PostPolicy(bucketName ?: "", ZonedDateTime.now().plusHours(6).plusMinutes(5))
        val key = "${userLogin}/avatar"
        policy.addEqualsCondition("key", key)
        policy.addContentLengthRangeCondition(0, 200 * 1024 * 1024);
        policy.addEqualsCondition("Content-Type", "image/*")
        return nonNullMinioClient.getPresignedPostFormData(policy)

    }



}