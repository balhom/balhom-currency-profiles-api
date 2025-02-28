package org.balhom.currencyprofilesapi.common.seeders

import io.quarkus.logging.Log

class TestSeeder {

    companion object {
        fun startSeeders(
            keycloakUrl: String
        ) {
            Log.info("Initializing Test Data Seeder...")

            IdpUserTestSeeder.insertData(keycloakUrl)

            Log.info("Test Data Seeder initialized")
        }
    }
}
