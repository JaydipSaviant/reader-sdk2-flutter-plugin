package com.squareup.sdk.readersdk2.converter

import com.squareup.sdk.reader2.authorization.AuthorizedLocation
import java.util.HashMap

class LocationConverter {
  //  private val moneyConverter: MoneyConverter = MoneyConverter()

    fun toMapObject(location: AuthorizedLocation): HashMap<String, Any> {
        val mapToReturn = HashMap<String, Any>()
        mapToReturn["locationId"] = location.locationId
        mapToReturn["name"] = location.name
        mapToReturn["businessName"] = location.businessName
        mapToReturn["isCardProcessingActivated"] = location.cardProcessingActivated
//        mapToReturn["minimumCardPaymentAmountMoney"] = moneyConverter.toMapObject(location.minimumCardPaymentAmountMoney)
//        mapToReturn["maximumCardPaymentAmountMoney"] = moneyConverter.toMapObject(location.maximumCardPaymentAmountMoney)
        mapToReturn["currencyCode"] = location.currencyCode.name
        mapToReturn["merchantId"] = location.merchantId

        return mapToReturn
    }
}
