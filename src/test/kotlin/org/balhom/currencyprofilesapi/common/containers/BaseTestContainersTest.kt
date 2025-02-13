package org.balhom.currencyprofilesapi.common.containers

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.kafka.KafkaContainer
import org.testcontainers.utility.DockerImageName

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseTestContainersTest {

    companion object {
        private val mongoContainer = MongoDBContainer(
            DockerImageName.parse("mongo:5.0")
        )
        private val kafkaContainer = KafkaContainer(
            DockerImageName.parse(
                "apache/kafka:3.7.2"
            )
        )
        private val s3Container = S3TestContainer()

        @BeforeAll
        @JvmStatic
        fun startContainers() {
            mongoContainer.start()
            kafkaContainer.start()
            s3Container.start()

            System.setProperty(
                "quarkus.mongodb.connection-string",
                mongoContainer.replicaSetUrl
            )
            System.setProperty(
                "kafka.bootstrap.servers",
                kafkaContainer.bootstrapServers
            )

            System.setProperty(
                "quarkus.s3.endpoint-override",
                s3Container.getEndpointOverride(LocalStackContainer.Service.S3)
                    .toString()
            )
            System.setProperty(
                "quarkus.s3.sync-client.type",
                "apache"
            )
            System.setProperty(
                "quarkus.s3.aws.region",
                s3Container.region
            )
            System.setProperty(
                "quarkus.s3.aws.credentials.access-key-id",
                s3Container.accessKey
            )
            System.setProperty(
                "quarkus.s3.aws.credentials.secret-access-key",
                s3Container.secretKey
            )
        }

        @AfterAll
        @JvmStatic
        fun stopContainers() {
            mongoContainer.stop()
            kafkaContainer.stop()
            s3Container.stop()
        }
    }
}