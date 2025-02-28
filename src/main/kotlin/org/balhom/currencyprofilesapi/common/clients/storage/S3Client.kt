package org.balhom.currencyprofilesapi.common.clients.storage

import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.common.data.models.FileData
import org.eclipse.microprofile.config.inject.ConfigProperty
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.HeadObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.S3Exception
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import java.time.Duration

@ApplicationScoped
class S3Client(
    val s3Client: S3Client,
    val s3Presigner: S3Presigner,
    @ConfigProperty(name = "quarkus.s3.bucket.name") val bucketName: String
) : ObjectStorageClient {

    override fun doesObjectExist(objectKey: String): Boolean {
        return try {
            val headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build()

            s3Client.headObject(headObjectRequest)
            true
        } catch (e: S3Exception) {
            if (e.statusCode() == 404) {
                false
            } else {
                throw e
            }
        }
    }

    override fun getObjectUrl(objectKey: String, expirationMinutes: Long): String {
        val presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(
                Duration.ofMinutes(expirationMinutes)
            )
            .getObjectRequest(
                buildGetRequest(objectKey)
            )
            .build()

        val presignedUrl = s3Presigner
            .presignGetObject(presignRequest)
        return presignedUrl.url().toString()
    }

    override fun uploadObject(fileData: FileData) {
        s3Client.putObject(
            buildPutRequest(fileData),
            RequestBody.fromFile(fileData.data)
        )
    }

    override fun deleteObject(objectKey: String) {
        s3Client.deleteObject(
            buildDeleteRequest(objectKey)
        )
    }

    private fun buildPutRequest(fileData: FileData): PutObjectRequest {
        return PutObjectRequest
            .builder()
            .bucket(bucketName)
            .key(fileData.filePath)
            .contentType(fileData.mimetype)
            .build()
    }

    private fun buildGetRequest(objectKey: String?): GetObjectRequest {
        return GetObjectRequest
            .builder()
            .bucket(bucketName)
            .key(objectKey)
            .build()
    }

    private fun buildDeleteRequest(objectKey: String?): DeleteObjectRequest {
        return DeleteObjectRequest
            .builder()
            .bucket(bucketName)
            .key(objectKey)
            .build()
    }
}