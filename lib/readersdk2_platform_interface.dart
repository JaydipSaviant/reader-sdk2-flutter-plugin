import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'readersdk2_method_channel.dart';

abstract class Readersdk2Platform extends PlatformInterface {
  /// Constructs a Readersdk2Platform.
  Readersdk2Platform() : super(token: _token);

  static final Object _token = Object();

  static Readersdk2Platform _instance = MethodChannelReadersdk2();

  /// The default instance of [Readersdk2Platform] to use.
  ///
  /// Defaults to [MethodChannelReadersdk2].
  static Readersdk2Platform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [Readersdk2Platform] when
  /// they register themselves.
  static set instance(Readersdk2Platform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
