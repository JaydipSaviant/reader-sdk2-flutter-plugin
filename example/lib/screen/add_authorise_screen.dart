// ignore_for_file: use_build_context_synchronously

import 'package:flutter/material.dart';
import 'package:readersdk2/readersdk2.dart';
import 'package:readersdk2_example/const/static_string.dart';
import 'package:readersdk2_example/screen/permission_screen.dart';
import 'package:readersdk2_example/widgets/buttons.dart';

class AddAuthorisedScreen extends StatefulWidget {
  const AddAuthorisedScreen({super.key});

  @override
  State<AddAuthorisedScreen> createState() => _AddAuthorisedScreenState();
}

class _AddAuthorisedScreenState extends State<AddAuthorisedScreen> {
  String selectedOption = "Production";
  var isCurrentEnv;

  // void checkAuthorisedAndNavigate() async {
  //   var isAuthorized = await Readersdk2.callNativeMethod;
  //   if (isAuthorized) {
  //     debugPrint("is authorised flutter = $isAuthorized");
  //     Navigator.push(context, MaterialPageRoute(
  //       builder: (context) {
  //         return const PermissionScreen();
  //       },
  //     ));
  //   } else {
  //     isCurrentEnv = await Readersdk2.currentEnv(selectedOption);
  //     if (isCurrentEnv) {
  //       var isAuthorized = await Readersdk2.callNativeMethod;
  //       if (isAuthorized) {
  //         debugPrint("is authorised flutter 33 = $isAuthorized");
  //         Navigator.push(context, MaterialPageRoute(
  //           builder: (context) {
  //             return const PermissionScreen();
  //           },
  //         ));
  //       }
  //     }
  //   }
  // }
  // @override
  // void initState() {
  //   Readersdk2.currentEnvirment(selectedOption);
  //   super.initState();
  // }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: <Widget>[
          ListTile(
            title: const Text(
              'Production',
              style: TextStyle(color: Colors.black),
            ),
            leading: Radio(
              value: "Production",
              groupValue: selectedOption,
              onChanged: (value) {
                setState(() {
                  selectedOption = value!;
                  // Readersdk2.currentEnvirment(selectedOption);
                });
              },
            ),
          ),
          ListTile(
            title: const Text(
              'Sandbox',
              style: TextStyle(color: Colors.black),
            ),
            leading: Radio(
              value: "Sandbox",
              groupValue: selectedOption,
              onChanged: (value) {
                setState(() {
                  selectedOption = value!;
                  // Readersdk2.currentEnvirment(selectedOption);
                });
              },
            ),
          ),
          SQButtonContainer(buttons: [
            SQRaisedButton(
              backgroundColor: Colors.blue,
              foregroundColor: Colors.white,
              text: StaticString.authoriseWithOauth.toUpperCase(),
              onPressed: () {
                //checkAuthorisedAndNavigate();
                Readersdk2.currentEnv(selectedOption).then((value) {
                  debugPrint("cureent enviorment = $value");
                  if (value) {
                    Navigator.push(context, MaterialPageRoute(
                      builder: (context) {
                        return const PermissionScreen();
                      },
                    ));
                  }
                });
              },
            ),
          ]),
        ],
      ),
    );
    ;
  }
}
