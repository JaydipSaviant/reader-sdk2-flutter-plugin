// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'model.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

const ErrorCode _$usageError = const ErrorCode._('usageError');
const ErrorCode _$authorizeErrorNoNetwork =
    const ErrorCode._('authorizeErrorNoNetwork');
const ErrorCode _$checkoutErrorCanceled =
    const ErrorCode._('checkoutErrorCanceled');
const ErrorCode _$checkoutErrorSdkNotAuthorized =
    const ErrorCode._('checkoutErrorSdkNotAuthorized');
const ErrorCode _$readerSettingsErrorSdkNotAuthorized =
    const ErrorCode._('readerSettingsErrorSdkNotAuthorized');
const ErrorCode _$storeCustomerErrorCanceled =
    const ErrorCode._('storeCustomerErrorCanceled');
const ErrorCode _$storeCustomerErrorInvalidCustomerId =
    const ErrorCode._('storeCustomerErrorInvalidCustomerId');
const ErrorCode _$storeCustomerErrorSdkNotAuthorized =
    const ErrorCode._('storeCustomerErrorSdkNotAuthorized');
const ErrorCode _$storeCustomerErrorNoNetwork =
    const ErrorCode._('storeCustomerErrorNoNetwork');

ErrorCode _$errorCodeValueOf(String name) {
  switch (name) {
    case 'usageError':
      return _$usageError;
    case 'authorizeErrorNoNetwork':
      return _$authorizeErrorNoNetwork;
    case 'checkoutErrorCanceled':
      return _$checkoutErrorCanceled;
    case 'checkoutErrorSdkNotAuthorized':
      return _$checkoutErrorSdkNotAuthorized;
    case 'readerSettingsErrorSdkNotAuthorized':
      return _$readerSettingsErrorSdkNotAuthorized;
    case 'storeCustomerErrorCanceled':
      return _$storeCustomerErrorCanceled;
    case 'storeCustomerErrorInvalidCustomerId':
      return _$storeCustomerErrorInvalidCustomerId;
    case 'storeCustomerErrorSdkNotAuthorized':
      return _$storeCustomerErrorSdkNotAuthorized;
    case 'storeCustomerErrorNoNetwork':
      return _$storeCustomerErrorNoNetwork;
    default:
      throw new ArgumentError(name);
  }
}

final BuiltSet<ErrorCode> _$errorCodeValues =
    new BuiltSet<ErrorCode>(const <ErrorCode>[
  _$usageError,
  _$authorizeErrorNoNetwork,
  _$checkoutErrorCanceled,
  _$checkoutErrorSdkNotAuthorized,
  _$readerSettingsErrorSdkNotAuthorized,
  _$storeCustomerErrorCanceled,
  _$storeCustomerErrorInvalidCustomerId,
  _$storeCustomerErrorSdkNotAuthorized,
  _$storeCustomerErrorNoNetwork,
]);

Serializer<Money> _$moneySerializer = new _$MoneySerializer();
Serializer<Location> _$locationSerializer = new _$LocationSerializer();
Serializer<ErrorCode> _$errorCodeSerializer = new _$ErrorCodeSerializer();

class _$MoneySerializer implements StructuredSerializer<Money> {
  @override
  final Iterable<Type> types = const [Money, _$Money];
  @override
  final String wireName = 'Money';

  @override
  Iterable<Object?> serialize(Serializers serializers, Money object,
      {FullType specifiedType = FullType.unspecified}) {
    final result = <Object?>[
      'amount',
      serializers.serialize(object.amount, specifiedType: const FullType(int)),
    ];
    Object? value;
    value = object.currencyCode;
    if (value != null) {
      result
        ..add('currencyCode')
        ..add(serializers.serialize(value,
            specifiedType: const FullType(String)));
    }
    return result;
  }

  @override
  Money deserialize(Serializers serializers, Iterable<Object?> serialized,
      {FullType specifiedType = FullType.unspecified}) {
    final result = new MoneyBuilder();

    final iterator = serialized.iterator;
    while (iterator.moveNext()) {
      final key = iterator.current! as String;
      iterator.moveNext();
      final Object? value = iterator.current;
      switch (key) {
        case 'amount':
          result.amount = serializers.deserialize(value,
              specifiedType: const FullType(int))! as int;
          break;
        case 'currencyCode':
          result.currencyCode = serializers.deserialize(value,
              specifiedType: const FullType(String)) as String?;
          break;
      }
    }

    return result.build();
  }
}

class _$LocationSerializer implements StructuredSerializer<Location> {
  @override
  final Iterable<Type> types = const [Location, _$Location];
  @override
  final String wireName = 'Location';

  @override
  Iterable<Object?> serialize(Serializers serializers, Location object,
      {FullType specifiedType = FullType.unspecified}) {
    final result = <Object?>[
      'currencyCode',
      serializers.serialize(object.currencyCode,
          specifiedType: const FullType(String)),
      'businessName',
      serializers.serialize(object.businessName,
          specifiedType: const FullType(String)),
      'name',
      serializers.serialize(object.name, specifiedType: const FullType(String)),
      'locationId',
      serializers.serialize(object.locationId,
          specifiedType: const FullType(String)),
      'isCardProcessingActivated',
      serializers.serialize(object.isCardProcessingActivated,
          specifiedType: const FullType(bool)),
      'maximumCardPaymentAmountMoney',
      serializers.serialize(object.maximumCardPaymentAmountMoney,
          specifiedType: const FullType(Money)),
      'minimumCardPaymentAmountMoney',
      serializers.serialize(object.minimumCardPaymentAmountMoney,
          specifiedType: const FullType(Money)),
    ];

    return result;
  }

  @override
  Location deserialize(Serializers serializers, Iterable<Object?> serialized,
      {FullType specifiedType = FullType.unspecified}) {
    final result = new LocationBuilder();

    final iterator = serialized.iterator;
    while (iterator.moveNext()) {
      final key = iterator.current! as String;
      iterator.moveNext();
      final Object? value = iterator.current;
      switch (key) {
        case 'currencyCode':
          result.currencyCode = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
        case 'businessName':
          result.businessName = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
        case 'name':
          result.name = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
        case 'locationId':
          result.locationId = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
        case 'isCardProcessingActivated':
          result.isCardProcessingActivated = serializers.deserialize(value,
              specifiedType: const FullType(bool))! as bool;
          break;
        case 'maximumCardPaymentAmountMoney':
          result.maximumCardPaymentAmountMoney.replace(serializers.deserialize(
              value,
              specifiedType: const FullType(Money))! as Money);
          break;
        case 'minimumCardPaymentAmountMoney':
          result.minimumCardPaymentAmountMoney.replace(serializers.deserialize(
              value,
              specifiedType: const FullType(Money))! as Money);
          break;
      }
    }

    return result.build();
  }
}

class _$ErrorCodeSerializer implements PrimitiveSerializer<ErrorCode> {
  static const Map<String, Object> _toWire = const <String, Object>{
    'usageError': 'USAGE_ERROR',
    'authorizeErrorNoNetwork': 'AUTHORIZE_NO_NETWORK',
    'checkoutErrorCanceled': 'CHECKOUT_CANCELED',
    'checkoutErrorSdkNotAuthorized': 'CHECKOUT_SDK_NOT_AUTHORIZED',
    'readerSettingsErrorSdkNotAuthorized': 'READER_SETTINGS_SDK_NOT_AUTHORIZED',
    'storeCustomerErrorCanceled': 'STORE_CUSTOMER_CARD_CANCELED',
    'storeCustomerErrorInvalidCustomerId':
        'STORE_CUSTOMER_CARD_INVALID_CUSTOMER_ID',
    'storeCustomerErrorSdkNotAuthorized':
        'STORE_CUSTOMER_CARD_SDK_NOT_AUTHORIZED',
    'storeCustomerErrorNoNetwork': 'STORE_CUSTOMER_CARD_NO_NETWORK',
  };
  static const Map<Object, String> _fromWire = const <Object, String>{
    'USAGE_ERROR': 'usageError',
    'AUTHORIZE_NO_NETWORK': 'authorizeErrorNoNetwork',
    'CHECKOUT_CANCELED': 'checkoutErrorCanceled',
    'CHECKOUT_SDK_NOT_AUTHORIZED': 'checkoutErrorSdkNotAuthorized',
    'READER_SETTINGS_SDK_NOT_AUTHORIZED': 'readerSettingsErrorSdkNotAuthorized',
    'STORE_CUSTOMER_CARD_CANCELED': 'storeCustomerErrorCanceled',
    'STORE_CUSTOMER_CARD_INVALID_CUSTOMER_ID':
        'storeCustomerErrorInvalidCustomerId',
    'STORE_CUSTOMER_CARD_SDK_NOT_AUTHORIZED':
        'storeCustomerErrorSdkNotAuthorized',
    'STORE_CUSTOMER_CARD_NO_NETWORK': 'storeCustomerErrorNoNetwork',
  };

  @override
  final Iterable<Type> types = const <Type>[ErrorCode];
  @override
  final String wireName = 'ErrorCode';

  @override
  Object serialize(Serializers serializers, ErrorCode object,
          {FullType specifiedType = FullType.unspecified}) =>
      _toWire[object.name] ?? object.name;

  @override
  ErrorCode deserialize(Serializers serializers, Object serialized,
          {FullType specifiedType = FullType.unspecified}) =>
      ErrorCode.valueOf(
          _fromWire[serialized] ?? (serialized is String ? serialized : ''));
}

class _$Money extends Money {
  @override
  final int amount;
  @override
  final String? currencyCode;

  factory _$Money([void Function(MoneyBuilder)? updates]) =>
      (new MoneyBuilder()..update(updates))._build();

  _$Money._({required this.amount, this.currencyCode}) : super._() {
    BuiltValueNullFieldError.checkNotNull(amount, r'Money', 'amount');
  }

  @override
  Money rebuild(void Function(MoneyBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  MoneyBuilder toBuilder() => new MoneyBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is Money &&
        amount == other.amount &&
        currencyCode == other.currencyCode;
  }

  @override
  int get hashCode {
    var _$hash = 0;
    _$hash = $jc(_$hash, amount.hashCode);
    _$hash = $jc(_$hash, currencyCode.hashCode);
    _$hash = $jf(_$hash);
    return _$hash;
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'Money')
          ..add('amount', amount)
          ..add('currencyCode', currencyCode))
        .toString();
  }
}

class MoneyBuilder implements Builder<Money, MoneyBuilder> {
  _$Money? _$v;

  int? _amount;
  int? get amount => _$this._amount;
  set amount(int? amount) => _$this._amount = amount;

  String? _currencyCode;
  String? get currencyCode => _$this._currencyCode;
  set currencyCode(String? currencyCode) => _$this._currencyCode = currencyCode;

  MoneyBuilder();

  MoneyBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _amount = $v.amount;
      _currencyCode = $v.currencyCode;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(Money other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$Money;
  }

  @override
  void update(void Function(MoneyBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  Money build() => _build();

  _$Money _build() {
    final _$result = _$v ??
        new _$Money._(
            amount: BuiltValueNullFieldError.checkNotNull(
                amount, r'Money', 'amount'),
            currencyCode: currencyCode);
    replace(_$result);
    return _$result;
  }
}

class _$Location extends Location {
  @override
  final String currencyCode;
  @override
  final String businessName;
  @override
  final String name;
  @override
  final String locationId;
  @override
  final bool isCardProcessingActivated;
  @override
  final Money maximumCardPaymentAmountMoney;
  @override
  final Money minimumCardPaymentAmountMoney;

  factory _$Location([void Function(LocationBuilder)? updates]) =>
      (new LocationBuilder()..update(updates))._build();

  _$Location._(
      {required this.currencyCode,
      required this.businessName,
      required this.name,
      required this.locationId,
      required this.isCardProcessingActivated,
      required this.maximumCardPaymentAmountMoney,
      required this.minimumCardPaymentAmountMoney})
      : super._() {
    BuiltValueNullFieldError.checkNotNull(
        currencyCode, r'Location', 'currencyCode');
    BuiltValueNullFieldError.checkNotNull(
        businessName, r'Location', 'businessName');
    BuiltValueNullFieldError.checkNotNull(name, r'Location', 'name');
    BuiltValueNullFieldError.checkNotNull(
        locationId, r'Location', 'locationId');
    BuiltValueNullFieldError.checkNotNull(
        isCardProcessingActivated, r'Location', 'isCardProcessingActivated');
    BuiltValueNullFieldError.checkNotNull(maximumCardPaymentAmountMoney,
        r'Location', 'maximumCardPaymentAmountMoney');
    BuiltValueNullFieldError.checkNotNull(minimumCardPaymentAmountMoney,
        r'Location', 'minimumCardPaymentAmountMoney');
  }

  @override
  Location rebuild(void Function(LocationBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  LocationBuilder toBuilder() => new LocationBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is Location &&
        currencyCode == other.currencyCode &&
        businessName == other.businessName &&
        name == other.name &&
        locationId == other.locationId &&
        isCardProcessingActivated == other.isCardProcessingActivated &&
        maximumCardPaymentAmountMoney == other.maximumCardPaymentAmountMoney &&
        minimumCardPaymentAmountMoney == other.minimumCardPaymentAmountMoney;
  }

  @override
  int get hashCode {
    var _$hash = 0;
    _$hash = $jc(_$hash, currencyCode.hashCode);
    _$hash = $jc(_$hash, businessName.hashCode);
    _$hash = $jc(_$hash, name.hashCode);
    _$hash = $jc(_$hash, locationId.hashCode);
    _$hash = $jc(_$hash, isCardProcessingActivated.hashCode);
    _$hash = $jc(_$hash, maximumCardPaymentAmountMoney.hashCode);
    _$hash = $jc(_$hash, minimumCardPaymentAmountMoney.hashCode);
    _$hash = $jf(_$hash);
    return _$hash;
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'Location')
          ..add('currencyCode', currencyCode)
          ..add('businessName', businessName)
          ..add('name', name)
          ..add('locationId', locationId)
          ..add('isCardProcessingActivated', isCardProcessingActivated)
          ..add('maximumCardPaymentAmountMoney', maximumCardPaymentAmountMoney)
          ..add('minimumCardPaymentAmountMoney', minimumCardPaymentAmountMoney))
        .toString();
  }
}

class LocationBuilder implements Builder<Location, LocationBuilder> {
  _$Location? _$v;

  String? _currencyCode;
  String? get currencyCode => _$this._currencyCode;
  set currencyCode(String? currencyCode) => _$this._currencyCode = currencyCode;

  String? _businessName;
  String? get businessName => _$this._businessName;
  set businessName(String? businessName) => _$this._businessName = businessName;

  String? _name;
  String? get name => _$this._name;
  set name(String? name) => _$this._name = name;

  String? _locationId;
  String? get locationId => _$this._locationId;
  set locationId(String? locationId) => _$this._locationId = locationId;

  bool? _isCardProcessingActivated;
  bool? get isCardProcessingActivated => _$this._isCardProcessingActivated;
  set isCardProcessingActivated(bool? isCardProcessingActivated) =>
      _$this._isCardProcessingActivated = isCardProcessingActivated;

  MoneyBuilder? _maximumCardPaymentAmountMoney;
  MoneyBuilder get maximumCardPaymentAmountMoney =>
      _$this._maximumCardPaymentAmountMoney ??= new MoneyBuilder();
  set maximumCardPaymentAmountMoney(
          MoneyBuilder? maximumCardPaymentAmountMoney) =>
      _$this._maximumCardPaymentAmountMoney = maximumCardPaymentAmountMoney;

  MoneyBuilder? _minimumCardPaymentAmountMoney;
  MoneyBuilder get minimumCardPaymentAmountMoney =>
      _$this._minimumCardPaymentAmountMoney ??= new MoneyBuilder();
  set minimumCardPaymentAmountMoney(
          MoneyBuilder? minimumCardPaymentAmountMoney) =>
      _$this._minimumCardPaymentAmountMoney = minimumCardPaymentAmountMoney;

  LocationBuilder();

  LocationBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _currencyCode = $v.currencyCode;
      _businessName = $v.businessName;
      _name = $v.name;
      _locationId = $v.locationId;
      _isCardProcessingActivated = $v.isCardProcessingActivated;
      _maximumCardPaymentAmountMoney =
          $v.maximumCardPaymentAmountMoney.toBuilder();
      _minimumCardPaymentAmountMoney =
          $v.minimumCardPaymentAmountMoney.toBuilder();
      _$v = null;
    }
    return this;
  }

  @override
  void replace(Location other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$Location;
  }

  @override
  void update(void Function(LocationBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  Location build() => _build();

  _$Location _build() {
    _$Location _$result;
    try {
      _$result = _$v ??
          new _$Location._(
              currencyCode: BuiltValueNullFieldError.checkNotNull(
                  currencyCode, r'Location', 'currencyCode'),
              businessName: BuiltValueNullFieldError.checkNotNull(
                  businessName, r'Location', 'businessName'),
              name: BuiltValueNullFieldError.checkNotNull(
                  name, r'Location', 'name'),
              locationId: BuiltValueNullFieldError.checkNotNull(
                  locationId, r'Location', 'locationId'),
              isCardProcessingActivated: BuiltValueNullFieldError.checkNotNull(
                  isCardProcessingActivated,
                  r'Location',
                  'isCardProcessingActivated'),
              maximumCardPaymentAmountMoney:
                  maximumCardPaymentAmountMoney.build(),
              minimumCardPaymentAmountMoney:
                  minimumCardPaymentAmountMoney.build());
    } catch (_) {
      late String _$failedField;
      try {
        _$failedField = 'maximumCardPaymentAmountMoney';
        maximumCardPaymentAmountMoney.build();
        _$failedField = 'minimumCardPaymentAmountMoney';
        minimumCardPaymentAmountMoney.build();
      } catch (e) {
        throw new BuiltValueNestedFieldError(
            r'Location', _$failedField, e.toString());
      }
      rethrow;
    }
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: deprecated_member_use_from_same_package,type=lint

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Money _$MoneyFromJson(Map<String, dynamic> json) => Money();

Map<String, dynamic> _$MoneyToJson(Money instance) => <String, dynamic>{};

Location _$LocationFromJson(Map<String, dynamic> json) => Location();

Map<String, dynamic> _$LocationToJson(Location instance) => <String, dynamic>{};
