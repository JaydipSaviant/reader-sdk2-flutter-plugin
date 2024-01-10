
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
part 'model.g.dart';

abstract class Money implements Built<Money, MoneyBuilder> {
  int get amount;

  String? get currencyCode;

  Money._();
  factory Money([updates(MoneyBuilder b) /*!*/]) = _$Money;
  static Serializer<Money> get serializer => _$moneySerializer;
}

abstract class Location implements Built<Location, LocationBuilder> {
  String get currencyCode;
  String get businessName;
  String get name;
  String get locationId;
  bool get isCardProcessingActivated;
  Money get maximumCardPaymentAmountMoney;
  Money get minimumCardPaymentAmountMoney;

  Location._();
  factory Location([updates(LocationBuilder b) /*!*/]) = _$Location;
  static Serializer<Location> get serializer => _$locationSerializer;
}

class ErrorCode extends EnumClass {
  static Serializer<ErrorCode> get serializer => _$errorCodeSerializer;

  @BuiltValueEnumConst(wireName: 'USAGE_ERROR')
  static const ErrorCode usageError = _$usageError;
  @BuiltValueEnumConst(wireName: 'AUTHORIZE_NO_NETWORK')
  static const ErrorCode authorizeErrorNoNetwork = _$authorizeErrorNoNetwork;
  @BuiltValueEnumConst(wireName: 'CHECKOUT_CANCELED')
  static const ErrorCode checkoutErrorCanceled = _$checkoutErrorCanceled;
  @BuiltValueEnumConst(wireName: 'CHECKOUT_SDK_NOT_AUTHORIZED')
  static const ErrorCode checkoutErrorSdkNotAuthorized =
      _$checkoutErrorSdkNotAuthorized;
  @BuiltValueEnumConst(wireName: 'READER_SETTINGS_SDK_NOT_AUTHORIZED')
  static const ErrorCode readerSettingsErrorSdkNotAuthorized =
      _$readerSettingsErrorSdkNotAuthorized;
  @BuiltValueEnumConst(wireName: 'STORE_CUSTOMER_CARD_CANCELED')
  static const ErrorCode storeCustomerErrorCanceled =
      _$storeCustomerErrorCanceled;
  @BuiltValueEnumConst(wireName: 'STORE_CUSTOMER_CARD_INVALID_CUSTOMER_ID')
  static const ErrorCode storeCustomerErrorInvalidCustomerId =
      _$storeCustomerErrorInvalidCustomerId;
  @BuiltValueEnumConst(wireName: 'STORE_CUSTOMER_CARD_SDK_NOT_AUTHORIZED')
  static const ErrorCode storeCustomerErrorSdkNotAuthorized =
      _$storeCustomerErrorSdkNotAuthorized;
  @BuiltValueEnumConst(wireName: 'STORE_CUSTOMER_CARD_NO_NETWORK')
  static const ErrorCode storeCustomerErrorNoNetwork =
      _$storeCustomerErrorNoNetwork;

  const ErrorCode._(String name) : super(name);

  static BuiltSet<ErrorCode> get values => _$errorCodeValues;
  static ErrorCode valueOf(String name) => _$errorCodeValueOf(name);
}