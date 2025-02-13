package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props

import org.balhom.currencyprofilesapi.common.data.props.ObjectIdUserProps
import java.io.File
import java.util.UUID

class MockUploadCurrencyProfileImagePropsFactory {
    companion object {
        fun create(): UploadCurrencyProfileImageProps {
            return UploadCurrencyProfileImageProps(
                objectIdUserProps = ObjectIdUserProps(
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                ),
                image = File(""),
                mimetype = "image/jpeg",
            )
        }
    }
}