import 'dart:async';

import 'package:built_value/serializer.dart';
import 'package:built_value/standard_json_plugin.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:readersdk2/model/models.dart';
import 'package:readersdk2/model/serializers.dart';

class Readersdk2 {
  static final _standardSerializers =
      (serializers.toBuilder()..addPlugin(StandardJsonPlugin())).build();
  static const MethodChannel channel = MethodChannel('readersdk2');

  Future<bool> get isAuthorizationInProgress async {
    try {
      bool isAuthorizationInProgress =
          await channel.invokeMethod('isAuthorizationInProgress');
      debugPrint("isAuthorized 1 1 1 1 ==== $isAuthorizationInProgress");
      return isAuthorizationInProgress;
    } on PlatformException catch (ex) {
      throw ReaderSdk2Exception(ex.code, ex.message, ex.details['debugCode'],
          ex.details['debugMessage']);
    }
  }

  static Future<Location> get authorizedLocation async {
    try {
      var locationNativeObject =
          await channel.invokeMethod('authorizedLocation');
      return _standardSerializers.deserializeWith(
          Location.serializer, locationNativeObject)!;
    } on PlatformException catch (ex) {
      throw ReaderSdk2Exception(ex.code, ex.message, ex.details['debugCode'],
          ex.details['debugMessage']);
    }
  }

  static Future<Location> authorize(String authCode) async {
    try {
      var params = <String, dynamic>{
        'authCode': authCode,
      };
      var locationNativeObject =
          await channel.invokeMethod('authorize', params);
      return _standardSerializers.deserializeWith(
          Location.serializer, locationNativeObject)!;
    } on PlatformException catch (ex) {
      throw ReaderSdk2Exception(ex.code, ex.message, ex.details['debugCode'],
          ex.details['debugMessage']);
    }
  }

  static Future<bool> currentEnv(String currentEnvironment, String selectedAccessToken, String selectedLocationId) async {
    try {
      var params = <String, dynamic>{
        'currentEnvironment': currentEnvironment,
        'selectedAccessToken': selectedAccessToken,
        'selectedLocationId': selectedLocationId,
      };
      var currentEnvironmentNativeObject =
          await channel.invokeMethod('currentAuthorisation', params);
      debugPrint(
          "isAuthorized 1 1 1 1 69 ==== $currentEnvironmentNativeObject");
      return currentEnvironmentNativeObject;
    } on PlatformException catch (ex) {
      throw ReaderSdk2Exception(ex.code, ex.message, ex.details['debugCode'],
          ex.details['debugMessage']);
    }
  }

  static Future<String> startCheckout(CheckoutParameters checkoutParams) async {
    try {
      var params = <String, dynamic>{
        'checkoutParams': _standardSerializers.serializeWith(
            CheckoutParameters.serializer, checkoutParams),
      };
      var checkoutResultNativeObject =
          await channel.invokeMethod('startCheckout', params);
      debugPrint('Result from native parameter payment : $params');

      return checkoutResultNativeObject;
    } on PlatformException catch (ex) {
      throw ReaderSdk2Exception(ex.code, ex.message, ex.details['debugCode'],
          ex.details['debugMessage']);
    }
  }
}

class ReaderSdk2Exception implements Exception {
  static final _standardSerializers =
      (serializers.toBuilder()..addPlugin(StandardJsonPlugin())).build();

  final String _code;

  final String? message;

  final String debugCode;

  final String? debugMessage;

  ErrorCode? get code =>
      _standardSerializers.deserializeWith(ErrorCode.serializer, _code);

  ReaderSdk2Exception(
    this._code,
    this.message,
    this.debugCode,
    this.debugMessage,
  );

  @override
  String toString() =>
      'PlatformException($code, $message, $debugCode, $debugMessage)';
}
