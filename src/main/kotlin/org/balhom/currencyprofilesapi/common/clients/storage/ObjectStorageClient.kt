package org.balhom.currencyprofilesapi.common.clients.storage

import org.balhom.currencyprofilesapi.common.data.models.FileData

interface ObjectStorageClient {

    fun doesObjectExist(objectKey: String): Boolean

    fun getObjectUrl(objectKey: String, expirationMinutes: Long): String

    fun uploadObject(fileData: FileData)

    fun deleteObject(objectKey: String)
}
