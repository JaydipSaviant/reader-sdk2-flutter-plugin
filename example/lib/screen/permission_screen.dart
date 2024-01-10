// ignore_for_file: prefer_const_constructors, prefer_const_literals_to_create_immutables, use_build_context_synchronously

import 'package:flutter/material.dart';
import 'package:readersdk2_example/const/static_string.dart';
import 'package:readersdk2_example/screen/authorize_screen.dart';
import 'package:readersdk2_example/screen/mock_reader_screen.dart';
import 'package:readersdk2_example/widgets/buttons.dart';
import 'package:readersdk2_example/widgets/permisson_container.dart';
import 'package:permission_handler/permission_handler.dart';

class PermissionScreen extends StatefulWidget {
  const PermissionScreen({super.key});

  @override
  State<PermissionScreen> createState() => _PermissionScreenState();
}

class _PermissionScreenState extends State<PermissionScreen> {
  bool microPhonPermission = false;
  bool locationPermission = false;
  bool bluetoothPermission = false;
  bool storagePermission = false;

  @override
  void initState() {
    super.initState();
    requestPermissions();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          mainAxisSize: MainAxisSize.max,
          children: [
            Spacer(
              flex: 2,
            ),
            PermissionContainer(
              title: StaticString.microPhone,
              subtitle: StaticString.subtitleMicroPhone,
              isEnable: microPhonPermission,
              onPressed: microPhonPermission
                  ? null
                  : () {
                      selectPermission(permissionNumber: 1);
                    },
            ),
            PermissionContainer(
              title: StaticString.location,
              subtitle: StaticString.subtitleLocation,
              isEnable: locationPermission,
              onPressed: locationPermission
                  ? null
                  : () {
                      selectPermission(permissionNumber: 2);
                    },
            ),
            PermissionContainer(
              title: StaticString.bluetooth,
              subtitle: StaticString.subtitlebluetooth,
              isEnable: bluetoothPermission,
              onPressed: bluetoothPermission
                  ? null
                  : () {
                      selectPermission(permissionNumber: 3);
                    },
            ),
            PermissionContainer(
              title: StaticString.storage,
              subtitle: StaticString.subtitleStorage,
              isEnable: storagePermission,
              onPressed: storagePermission
                  ? null
                  : () async {
                      selectPermission(permissionNumber: 4);
                    },
            ),
            SizedBox(
              height: 50,
            ),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 10),
              child: SQRaisedButton(
                text: StaticString.startTaking,
                onPressed: () {
                  if (!microPhonPermission) {
                    ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text(StaticString.selectMicroPhone)));
                  } else if (!locationPermission) {
                    ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text(StaticString.selectLocation)));
                  } else {
                    Navigator.push(context, MaterialPageRoute(
                      builder: (context) {
                        //return AuthorizeScreen();
                        return MockReaderScreen();
                      },
                    ));
                  }
                },
              ),
            ),
            Spacer(),
            SizedBox(
              height: 50,
            ),
          ],
        ),
      ),
    );
  }

  Future<void> requestPermissions() async {
    final statusMicrophone = await Permission.microphone.request();
    final statusLocation = await Permission.location.request();
    final statusBluetooth = await Permission.bluetooth.request();
    final statusStorage = await Permission.storage.request();

    if (statusLocation.isGranted) {
      setState(() {
        locationPermission = true;
      });
    }
    if (statusMicrophone.isGranted) {
      setState(() {
        microPhonPermission = true;
      });
    }
    if (statusBluetooth.isGranted) {
      setState(() {
        bluetoothPermission = true;
      });
    }
    if (statusStorage.isGranted) {
      setState(() {
        storagePermission = true;
      });
    }
    // if (!statusStorage.isGranted ||
    //     !statusBluetooth.isGranted ||
    //     !statusLocation.isGranted ||
    //     !statusMicrophone.isGranted) {}
  }

  selectPermission({required int permissionNumber}) {
    setState(() {
      if (permissionNumber == 1) {
        microPhonPermission = !microPhonPermission;
      } else if (permissionNumber == 2) {
        locationPermission = !locationPermission;
      } else if (permissionNumber == 3) {
        bluetoothPermission = !bluetoothPermission;
      } else {
        storagePermission = !storagePermission;
      }
    });
  }
}
