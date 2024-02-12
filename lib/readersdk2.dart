import 'dart:async';
import 'package:built_value/standard_json_plugin.dart';
import 'package:flutter/services.dart';
import 'package:readersdk2/model/models.dart';
import 'package:readersdk2/model/serializers.dart';

class Readersdk2 {
  static final _standardSerializers =
      (serializers.toBuilder()..addPlugin(StandardJsonPlugin())).build();
  static late MethodChannel channel = MethodChannel('readersdk2');

  static const MethodChannel channelCallBack =
      MethodChannel('readerSDKFlutter');

  Future<dynamic> handleMethod(MethodCall call) async {
    switch (call.method) {
      case 'sendDataToFlutter':
        String data = call.arguments;
        break;
      default:
        break;
    }
  }

  Future<bool> get isAuthorizationInProgress async {
    try {
      bool isAuthorizationInProgress =
          await channel.invokeMethod('isAuthorizationInProgress');
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

  static Future<bool> currentEnv(String currentEnvironment,
      String selectedAccessToken, String selectedLocationId) async {
    try {
      var params = <String, dynamic>{
        'currentEnvironment': currentEnvironment,
        'selectedAccessToken': selectedAccessToken,
        'selectedLocationId': selectedLocationId,
      };
      var currentEnvironmentNativeObject =
          await channel.invokeMethod('currentAuthorisation', params);
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
      if (checkoutResultNativeObject != null) {
        return checkoutResultNativeObject;
      } else {
        throw Exception('Received null result from native');
      }
    } on PlatformException catch (ex) {
      throw ReaderSdk2Exception(
        ex.code,
        ex.message,
        ex.details['debugCode'],
        ex.details['debugMessage'],
      );
    }
  }

  static Future<void> get directAuthorised async {
    try {
      var directAuth = await channel.invokeMethod('directMockReaderUI');
      return directAuth;
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
