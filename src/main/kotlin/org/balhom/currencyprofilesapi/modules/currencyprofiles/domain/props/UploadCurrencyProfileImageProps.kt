package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props

import org.balhom.currencyprofilesapi.common.data.props.ObjectIdUserProps
import java.io.File

data class UploadCurrencyProfileImageProps(
    val objectIdUserProps: ObjectIdUserProps,
    val image: File,
    val mimetype: String,
)
