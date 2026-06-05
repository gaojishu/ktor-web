package com.example.infra.storage.provider.impl

import com.aliyun.oss.OSSClientBuilder
import com.example.infra.storage.config.AliyunStorageConfig
import com.example.infra.storage.dto.OssUploadPolicy
import com.example.infra.storage.enums.StorageProviderEnum
import com.example.infra.storage.enums.StorageVisibilityEnum
import com.example.infra.storage.provider.StorageProvider
import com.example.infra.storage.utils.FileUtil
import kotlinx.serialization.json.*
import org.koin.core.annotation.Single
import java.io.File
import java.io.InputStream
import java.net.URI
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Base64
import java.util.Date
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Single(binds = [StorageProvider::class])
class AliyunStorageProviderImpl(
    private val config: AliyunStorageConfig,
) : StorageProvider {

    override val provider: StorageProviderEnum = StorageProviderEnum.ALIYUN

    private val ossClient = OSSClientBuilder().build(config.endpoint, config.accessKeyId, config.accessKeySecret)

    private val host = "https://${config.bucket}.${config.endpoint}" // 构建 host
    private val expireSeconds = 7200L // 策略有效期：7200秒 = 2小时

    /**
     * 将对象 key 拼接 bucket 域名，生成可访问的完整 URL
     */
    override fun getFullUrl(path: String): String {
        val cleanKey = path.removePrefix("/")
        return "$host/$cleanKey"
    }

    /**
     * 在同一 bucket 内复制对象
     */
    override fun copy(from: String, to: String) {
        ossClient.copyObject(config.bucket, from, config.bucket, to)
    }

    /**
     * 删除指定路径的对象
     */
    override fun delete(path: String) {
        ossClient.deleteObject(config.bucket, path)
    }

    /**
     * 判断对象是否存在
     */
    override fun exists(path: String): Boolean {
        return ossClient.doesObjectExist(config.bucket, path)
    }

    /**
     * 生成带签名的临时访问 URL，默认有效期 1 小时
     */
    override fun generatePresignedUrl(path: String): String {
        val expiration = Date.from(Instant.now().plusSeconds(3600))
        return ossClient.generatePresignedUrl(config.bucket, path, expiration).toString()
    }

    /**
     * 重命名（移动）对象，在同一 bucket 内修改 key
     */
    override fun move(from: String, to: String) {
        ossClient.renameObject(
            config.bucket,
            from,
            to
        )
    }

    /**
     * 从完整 URL 中提取对象 key（去掉域名和前导斜杠）
     */
    override fun removeUrl(url: String): String {
        return URI.create(url).path.removePrefix("/")
    }

    /**
     * 生成前端直传 OSS 所需的上传凭证（Post Policy）
     */
    override fun uploadPolicy(): Map<String, String> {
        // 1. 计算过期时间（ISO 8601 格式）
        val expiration = Instant.now().plusSeconds(expireSeconds).toString() // Kotlin 默认输出 ISO-8601

        // 2. 构建 policy 文档并 Base64 编码
        val policyText = buildJsonObject {
            put("expiration", expiration)
            putJsonArray("conditions") {
                addJsonArray {
                    add(JsonPrimitive("content-length-range"))
                    add(JsonPrimitive(0))
                    add(JsonPrimitive(1024 * 1024 * 1024))
                }
                addJsonArray {
                    add(JsonPrimitive("starts-with"))
                    add(JsonPrimitive("\$key"))
                    add(JsonPrimitive(config.uploadPrefix))
                }
            }
        }.toString()
        
        val encodedPolicy = Base64.getEncoder().encodeToString(policyText.toByteArray(Charsets.UTF_8))

        // 3. 使用 HMAC-SHA1 签名
        val secretKeySpec = SecretKeySpec(config.accessKeySecret.toByteArray(StandardCharsets.UTF_8), "HmacSHA1")
        val mac = Mac.getInstance("HmacSHA1")
        mac.init(secretKeySpec)
        val signatureBytes = mac.doFinal(encodedPolicy.toByteArray(StandardCharsets.UTF_8))
        val signature = Base64.getEncoder().encodeToString(signatureBytes)

        // 4. 返回响应
        val dto = OssUploadPolicy(
            accessId = config.accessKeyId,
            host = host,
            policy = encodedPolicy,
            signature = signature,
            expire = expiration,
            prefix = config.uploadPrefix
        )

        return Json.encodeToJsonElement(dto).jsonObject.mapValues { (_, value) ->
            value.jsonPrimitive.content
        }
    }

    /**
     * 上传本地文件，按可见性规则生成 key 后写入 OSS
     */
    override fun upload(file: File, visibility: StorageVisibilityEnum): String {
        val key = FileUtil.rename(objectName = file.name, visibility.value)
        ossClient.putObject(config.bucket,key,file)
        return key
    }

    /**
     * 通过输入流上传文件，按可见性规则生成 key 后写入 OSS
     */
    override fun uploadStream(
        inputStream: InputStream,
        originalFileName: String,
        visibility: StorageVisibilityEnum,
    ): String {
        val key = FileUtil.rename(objectName = originalFileName,visibility = visibility.value)
        ossClient.putObject(config.bucket,key,inputStream)
        return key
    }
}
