package org.balhom.currencyprofilesapi.common.config

import io.quarkus.logging.Log
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.event.Observes
import jakarta.inject.Singleton
import java.util.TimeZone


@Singleton
class TimezoneConfig {
    fun setTimezone(@Observes startupEvent: StartupEvent?) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        Log.info("Using UTC Timezone")
    }
}