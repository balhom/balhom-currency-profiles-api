package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.persistence.mongo

import io.quarkus.mongodb.panache.kotlin.PanacheMongoRepository
import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.persistence.mongo.data.CurrencyProfileMongoEntity

@ApplicationScoped
class CurrencyProfileMongoRepository : PanacheMongoRepository<CurrencyProfileMongoEntity> {
}
