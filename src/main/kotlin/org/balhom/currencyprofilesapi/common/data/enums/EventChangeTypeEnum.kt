package org.balhom.currencyprofilesapi.common.data.enums

enum class EventChangeTypeEnum {
    CREATE, UPDATE, DELETE;

    companion object {
        fun fromAction(action: String): EventChangeTypeEnum =
            when (action) {
                "C" -> CREATE
                "U" -> UPDATE
                "D" -> DELETE
                else -> throw UnsupportedOperationException()
            }
    }
}
