package org.balhom.currencyprofilesapi.common.containers

import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CreateBucketRequest

class S3TestContainer(imageName: String = "localstack/localstack:1.4.0") :
        LocalStackContainer(DockerImageName.parse(imageName).withTag("1.4.0")) {

    private val bucketName = "test-bucket"

    init {
        withServices(Service.S3)
    }

    override fun start() {
        super.start()
        createS3Bucket()
    }

    private fun createS3Bucket() {
        val s3Client = S3Client.builder()
            .endpointOverride(getEndpointOverride(Service.S3))
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                )
            )
            .build()

        s3Client.createBucket(
            CreateBucketRequest.builder()
                .bucket(bucketName)
                .build()
        )
    }
}
