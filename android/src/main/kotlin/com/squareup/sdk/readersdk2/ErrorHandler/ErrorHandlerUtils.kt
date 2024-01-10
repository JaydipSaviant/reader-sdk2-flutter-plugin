package com.squareup.sdk.readersdk2.ErrorHandler

import com.squareup.sdk.reader2.authorization.AuthorizeErrorCode
import com.squareup.sdk.reader2.core.ErrorCode
import java.util.HashMap

class ErrorHandlerUtils {
    companion object {
        // Define all the error codes and messages below
        // These error codes and messages **MUST** align with iOS error codes and dart error codes

        // Usage error
        const val USAGE_ERROR = "USAGE_ERROR"

        private val authorizeErrorMap: Map<AuthorizeErrorCode, String>
//        private val checkoutErrorMap: Map<CheckoutErrorCode, String>
//        private val readerSettingsErrorMap: Map<ReaderSettingsErrorCode, String>
//        private val storeCustomerCardErrorMap: Map<StoreCustomerCardErrorCode, String>

        init {
            // Build Expected Error mappings
            authorizeErrorMap = linkedMapOf()
            AuthorizeErrorCode.values().forEach { authorizeErrorCode ->
                // Search KEEP_IN_SYNC_AUTHORIZE_ERROR to update all places
                when (authorizeErrorCode) {
                    AuthorizeErrorCode.NO_NETWORK ->
                        authorizeErrorMap[AuthorizeErrorCode.NO_NETWORK] = "AUTHORIZE_NO_NETWORK"
                    AuthorizeErrorCode.USAGE_ERROR ->
                        // Usage error is handled separately
                        Unit
                    else ->
                        throw RuntimeException("Unexpected auth error code: ${authorizeErrorCode.name}")
                }
            }

           // checkoutErrorMap = linkedMapOf()
//            CheckoutErrorCode.values().forEach { checkoutErrorCode ->
//                // Search KEEP_IN_SYNC_CHECKOUT_ERROR to update all places
//                when (checkoutErrorCode) {
//                    CheckoutErrorCode.SDK_NOT_AUTHORIZED ->
//                        checkoutErrorMap[CheckoutErrorCode.SDK_NOT_AUTHORIZED] = "CHECKOUT_SDK_NOT_AUTHORIZED"
//                    CheckoutErrorCode.CANCELED ->
//                        checkoutErrorMap[CheckoutErrorCode.CANCELED] = "CHECKOUT_CANCELED"
//                    CheckoutErrorCode.USAGE_ERROR ->
//                        // Usage error is handled separately
//                        Unit
//                    else ->
//                        throw RuntimeException("Unexpected checkout error code: ${checkoutErrorCode.name}")
//                }
//            }

           // readerSettingsErrorMap = linkedMapOf()
//            ReaderSettingsErrorCode.values().forEach { readerSettingsErrorCode ->
//                // Search KEEP_IN_SYNC_READER_SETTINGS_ERROR to update all places
//                when (readerSettingsErrorCode) {
//                    ReaderSettingsErrorCode.SDK_NOT_AUTHORIZED ->
//                        readerSettingsErrorMap[ReaderSettingsErrorCode.SDK_NOT_AUTHORIZED] = "READER_SETTINGS_SDK_NOT_AUTHORIZED"
//                    ReaderSettingsErrorCode.USAGE_ERROR ->
//                        // Usage error is handled separately
//                        Unit
//                    else ->
//                        throw RuntimeException("Unexpected reader settings error code: ${readerSettingsErrorCode.name}")
//                }
//            }

          //  storeCustomerCardErrorMap = linkedMapOf()
//            StoreCustomerCardErrorCode.values().forEach { storeCustomerCardErrorCode ->
//                // Search KEEP_IN_SYNC_STORE_CUSTOMER_CARD_ERROR to update all places
//                when (storeCustomerCardErrorCode) {
//                    StoreCustomerCardErrorCode.CANCELED ->
//                        storeCustomerCardErrorMap[StoreCustomerCardErrorCode.CANCELED] = "STORE_CUSTOMER_CARD_CANCELED"
//                    StoreCustomerCardErrorCode.INVALID_CUSTOMER_ID ->
//                        storeCustomerCardErrorMap[StoreCustomerCardErrorCode.INVALID_CUSTOMER_ID] = "STORE_CUSTOMER_CARD_INVALID_CUSTOMER_ID"
//                    StoreCustomerCardErrorCode.SDK_NOT_AUTHORIZED ->
//                        storeCustomerCardErrorMap[StoreCustomerCardErrorCode.SDK_NOT_AUTHORIZED] = "STORE_CUSTOMER_CARD_SDK_NOT_AUTHORIZED"
//                    StoreCustomerCardErrorCode.NO_NETWORK ->
//                        storeCustomerCardErrorMap[StoreCustomerCardErrorCode.NO_NETWORK] = "STORE_CUSTOMER_CARD_NO_NETWORK"
//                    StoreCustomerCardErrorCode.USAGE_ERROR ->
//                        // Usage error is handled separately
//                        Unit
//                    else ->
//                        throw RuntimeException("Unexpected reader settings error code: ${storeCustomerCardErrorCode.name}")
//                }
//            }
        }

        fun getNativeModuleErrorMessage(nativeModuleErrorCode: String): String {
            return "Something went wrong. Please contact the developer of this application and provide them with this error code: $nativeModuleErrorCode"
        }

        fun getDebugErrorObject(debugCode: String, debugMessage: String): HashMap<String, String> {
            val errorData = hashMapOf<String, String>()
            errorData["debugCode"] = debugCode
            errorData["debugMessage"] = debugMessage
            return errorData
        }

        fun getErrorCode(nativeErrorCode: ErrorCode): String {
            return if (nativeErrorCode.isUsageError) {
                USAGE_ERROR
            } else {
                val errorCodeString: String = when (nativeErrorCode) {
                    is AuthorizeErrorCode -> authorizeErrorMap[nativeErrorCode] ?: throw RuntimeException("Unexpected error code: ${nativeErrorCode.toString()}")
//                    is CheckoutErrorCode -> checkoutErrorMap[nativeErrorCode] ?: throw RuntimeException("Unexpected error code: ${nativeErrorCode.toString()}")
//                    is ReaderSettingsErrorCode -> readerSettingsErrorMap[nativeErrorCode] ?: throw RuntimeException("Unexpected error code: ${nativeErrorCode.toString()}")
//                    is StoreCustomerCardErrorCode -> storeCustomerCardErrorMap[nativeErrorCode] ?: throw RuntimeException("Unexpected error code: ${nativeErrorCode.toString()}")
                    else -> throw RuntimeException("Unexpected error code: ${nativeErrorCode.toString()}")
                }
                errorCodeString
            }
        }
    }
}
