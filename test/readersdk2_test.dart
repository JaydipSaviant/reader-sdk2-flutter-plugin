import 'package:flutter_test/flutter_test.dart';
import 'package:readersdk2/readersdk2.dart';
import 'package:readersdk2/readersdk2_platform_interface.dart';
import 'package:readersdk2/readersdk2_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockReadersdk2Platform
    with MockPlatformInterfaceMixin
    implements Readersdk2Platform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final Readersdk2Platform initialPlatform = Readersdk2Platform.instance;

  test('$MethodChannelReadersdk2 is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelReadersdk2>());
  });

  test('getPlatformVersion', () async {
    Readersdk2 readersdk2Plugin = Readersdk2();
    MockReadersdk2Platform fakePlatform = MockReadersdk2Platform();
    Readersdk2Platform.instance = fakePlatform;

    expect("",'42');
  });
}
