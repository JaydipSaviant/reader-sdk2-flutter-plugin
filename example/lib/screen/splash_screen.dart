import 'dart:async';

import 'package:flutter/material.dart';
import 'package:readersdk2_example/api_intregration/shared_prf.dart';
import 'package:readersdk2_example/const/global_variable.dart';
import 'package:readersdk2_example/screen/add_authorise_screen.dart';
import 'package:readersdk2_example/screen/add_card_reader_screen.dart';

class SplashScreen extends StatefulWidget {
  const SplashScreen({super.key});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  bool isCompletedAuth = false;

  getAuth() async {
    isCompletedAuth = await SharedPref().getBool("isCompleteAuthorized");
    Timer(
        Duration(seconds: 5),
        () => Navigator.pushReplacement(
            context,
            MaterialPageRoute(
                builder: (context) => isCompletedAuth
                    ? const AddCardReaderScreen()
                    : const AddAuthorisedScreen())));
  }

  void initState() {
    super.initState();
    getAuth();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
          color: Colors.blue,
          child: FlutterLogo(size: MediaQuery.of(context).size.height)),
    );
  }
}



// /*
// Copyright 2022 Square Inc.

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// */

// import 'dart:async';

// import 'package:example/screen/authorize_screen.dart';
// import 'package:example/widgets/animated_square_logo.dart';
// import 'package:example/widgets/buttons.dart';
// import 'package:flutter/foundation.dart';
// import 'package:flutter/material.dart';
// import 'package:permission_handler/permission_handler.dart';

// /// A screen that shows an animated square logo and asks for square permissions
// class SplashScreen extends StatefulWidget {
//   @override
//   _SplashScreenState createState() => _SplashScreenState();
// }

// class _SplashScreenState extends State<SplashScreen> {
//   bool _hasAnimated = false;
//   bool _isCheckingPermissions = true;

//   @override
//   void initState() {
//     super.initState();
//   }

//   Future _checkStatusAndNavigate() async {
//     var hasPermissions = await _permissionsStatus
//         .then((statuses) => statuses.every((status) => status.isGranted));
//     if (!hasPermissions) {
//       setState(() {
//         _isCheckingPermissions = false;
//       });
//       return;
//     }

//     Navigator.pushReplacement(
//         context,
//         MaterialPageRoute(
//           builder: (context) => AuthorizeScreen(),
//         ));
//   }

//   @override
//   Widget build(BuildContext context) => Scaffold(
//         body: SafeArea(
//           child: !_hasAnimated || _isCheckingPermissions
//               ? ConstrainedBox(
//                   constraints: const BoxConstraints.expand(),
//                   child: AnimatedSquareLogo(onLogoAnimated: () {
//                     _checkStatusAndNavigate();
//                     setState(() {
//                       _hasAnimated = true;
//                     });
//                   }))
//               : _PermissionSettings(),
//         ),
//       );
// }

// class _PermissionSettings extends StatelessWidget {
//   @override
//   Widget build(BuildContext context) =>
//       Column(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
//         // SquareLogo(),
//         Container(
//           margin: const EdgeInsets.symmetric(horizontal: 32.0),
//           child: const Text(
//             'Grant Reader SDK the required permissions.',
//             textAlign: TextAlign.center,
//           ),
//         ),
//         _ButtonContainer()
//       ]);
// }

// class _ButtonContainer extends StatefulWidget {
//   @override
//   _ButtonContainerState createState() => _ButtonContainerState();
// }

// class _ButtonContainerState extends State<_ButtonContainer> {
//   bool _hasLocationAccess = false;
//   String _locationButtonText = 'Enable Location Access';

//   bool _hasMicrophoneAccess = false;
//   String _microphoneButtonText = 'Enable Microphone Access';

//   bool _hasBluetoothAccess = !_requireBluetoothPermission;
//   String _bluetoothButtonText = 'Enable Bluetooth Access';

//   @override
//   void initState() {
//     super.initState();
//     checkPermissionsAndNavigate();
//   }

//   void requestPermission(Permission permission) async {
//     switch (await permission.status) {
//       //Works in ios
//       case PermissionStatus.permanentlyDenied:
//         openAppSettings();
//         break;
//       default:
//         //This condition is to check 'Don't ask again' on android'
//         if (await permission.request().isPermanentlyDenied) {
//           openAppSettings();
//         }
//         break;
//     }

//     checkPermissionsAndNavigate();
//   }

//   void onRequestLocationPermission() {
//     requestPermission(Permission.locationWhenInUse);
//   }

//   void onRequestAudioPermission() {
//     requestPermission(Permission.microphone);
//   }

//   void onRequestBluetooth() async {
//     // iOS
//     if ((await _permissionsStatus)
//         .sublist(2)
//         .any((status) => status.isPermanentlyDenied)) {
//       openAppSettings();
//       return;
//     }

//     final statuses =
//         await [Permission.bluetoothScan, Permission.bluetoothConnect].request();

//     if (statuses.values.any((status) => status.isPermanentlyDenied)) {
//       openAppSettings();
//       return;
//     }

//     checkPermissionsAndNavigate();
//   }

//   void checkPermissionsAndNavigate() async {
//     var permissionsStatus = await _permissionsStatus;

//     // discard if wiget has been removed while waiting
//     if (!mounted) return;
//     updateMicrophoneStatus(permissionsStatus[1]);
//     updateLocationStatus(permissionsStatus[0]);
//     updateBluetoothStatus(permissionsStatus.sublist(2));
//     debugPrint(
//         "permissions:$_hasLocationAccess=$_hasMicrophoneAccess=$_hasBluetoothAccess");
//     if (_hasLocationAccess && _hasMicrophoneAccess && _hasBluetoothAccess) {
//       Navigator.pushReplacement(
//           context,
//           MaterialPageRoute(
//             builder: (context) => const AuthorizeScreen(),
//           ));
//     }
//   }

//   void updateLocationStatus(PermissionStatus status) {
//     setState(() {
//       _hasLocationAccess = status == PermissionStatus.granted;

//       switch (status) {
//         case PermissionStatus.granted:
//           _locationButtonText = 'Location Enabled';
//           break;
//         case PermissionStatus.permanentlyDenied:
//           _locationButtonText = 'Enable Location in Settings';
//           break;
//         case PermissionStatus.restricted:
//           _locationButtonText = 'Location permission is restricted';
//           break;
//         case PermissionStatus.denied:
//           _locationButtonText = 'Enable Location Access';
//           break;
//         case PermissionStatus.limited:
//           _microphoneButtonText = 'Location permission is limited';
//         case PermissionStatus.provisional:
//       }
//     });
//   }

//   void updateMicrophoneStatus(PermissionStatus status) {
//     setState(() {
//       _hasMicrophoneAccess = status == PermissionStatus.granted;

//       switch (status) {
//         case PermissionStatus.granted:
//           _microphoneButtonText = 'Microphone Enabled';
//           break;
//         case PermissionStatus.permanentlyDenied:
//           _microphoneButtonText = 'Enable Microphone in Settings';
//           break;
//         case PermissionStatus.restricted:
//           _microphoneButtonText = 'Microphone permission is restricted';
//           break;
//         case PermissionStatus.denied:
//           _microphoneButtonText = 'Enable Microphone Access';
//           break;
//         case PermissionStatus.limited:
//           _microphoneButtonText = 'Microphone permission is limited';
//         case PermissionStatus.provisional:
//       }
//     });
//   }

//   void updateBluetoothStatus(Iterable<PermissionStatus> statuses) {
//     debugPrint("datais b=>$statuses");
//     if (statuses.isEmpty) {
//       return;
//     }
//     setState(() {
//       _hasBluetoothAccess = statuses.every((status) => status.isGranted);

//       if (_hasBluetoothAccess) {
//         _bluetoothButtonText = 'Bluetooth Enabled';
//         return;
//       }

//       if (statuses.any((status) => status.isPermanentlyDenied)) {
//         _bluetoothButtonText = 'Enable Bluetooth in Settings';
//         return;
//       }

//       if (statuses.any((status) => status.isRestricted)) {
//         _bluetoothButtonText = 'Bluetooth permission is restricted';
//         return;
//       }

//       if (statuses.any((status) => status.isDenied)) {
//         _bluetoothButtonText = 'Enable Bluetooth Access';
//         return;
//       }

//       if (statuses.any((status) => status.isLimited)) {
//         _bluetoothButtonText = 'Bluetooth permission is limited';
//         return;
//       }
//     });
//   }

//   @override
//   Widget build(BuildContext context) => SQButtonContainer(buttons: [
//         SQOutlineButton(
//           text: _microphoneButtonText,
//           onPressed: _hasMicrophoneAccess ? null : onRequestAudioPermission,
//         ),
//         SQOutlineButton(
//             text: _locationButtonText,
//             onPressed: _hasLocationAccess ? null : onRequestLocationPermission),
//         if (_requireBluetoothPermission)
//           SQOutlineButton(
//             text: _bluetoothButtonText,
//             onPressed: _hasBluetoothAccess ? null : onRequestBluetooth,
//           ),
//       ]);
// }

// Future<List<PermissionStatus>> get _permissionsStatus => Future.wait([
//       Permission.locationWhenInUse.status,
//       Permission.microphone.status,
//       if (_requireBluetoothPermission) ...[
//         Permission.bluetoothConnect.status,
//         Permission.bluetoothScan.status,
//       ]
//     ]);

// bool _requireBluetoothPermission =
//     (TargetPlatform.android == defaultTargetPlatform);
