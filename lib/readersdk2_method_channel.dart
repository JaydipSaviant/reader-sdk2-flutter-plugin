import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'readersdk2_platform_interface.dart';

/// An implementation of [Readersdk2Platform] that uses method channels.
class MethodChannelReadersdk2 extends Readersdk2Platform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('readersdk2');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
