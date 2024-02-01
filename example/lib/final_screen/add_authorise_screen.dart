// ignore_for_file: use_build_context_synchronously

import 'package:flutter/material.dart';
import 'package:readersdk2/readersdk2.dart';
import 'package:readersdk2_example/api_intregration/shared_prf.dart';
import 'package:readersdk2_example/const/static_string.dart';
import 'package:readersdk2_example/final_screen/permission_screen.dart';
import 'package:readersdk2_example/widgets/buttons.dart';
import 'package:readersdk2_example/widgets/network_button.dart';

class AddAuthorisedScreen extends StatefulWidget {
  const AddAuthorisedScreen({super.key});

  @override
  State<AddAuthorisedScreen> createState() => _AddAuthorisedScreenState();
}

class _AddAuthorisedScreenState extends State<AddAuthorisedScreen> {
  String selectedOption = "Production";
  String selectedAccessToken =
      "EAAAFNbbmssq_Adi_nZhJXZ1n5Sg0So5eBeYLxAvJ0pfvMX1A_OFtlwxPti1T3xW";
  String selectedLocationId = "LBBSYN1QKHJSY";
  String applicationID = "sq0idp-Pr-sJRq3sev_zacgGj2H1Q";
  //var isCurrentEnv;

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
                  selectedAccessToken =
                      "EAAAFNbbmssq_Adi_nZhJXZ1n5Sg0So5eBeYLxAvJ0pfvMX1A_OFtlwxPti1T3xW";
                  selectedLocationId = "LBBSYN1QKHJSY";
                  applicationID = "sq0idp-Pr-sJRq3sev_zacgGj2H1Q";
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
                  selectedAccessToken =
                      "EAAAEJe7nhcIDV2cXO1edJafX0ZNFQk42lqxVZjYn1kc3Tg7lN-P32GlFey5OepV";
                  selectedLocationId = "LWTCANRWNHMF0";
                  applicationID = "sandbox-sq0idb-7QT2EriOdn1Gz8jw7e2KSw";
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
                Readersdk2.currentEnv(
                        selectedOption, selectedAccessToken, selectedLocationId)
                    .then((value) {
                  debugPrint("cureent enviorment = $value");
                  if (value) {
                    SharedPref().saveString(
                        "currentEnv",
                        "currentToken",
                        "currentLoactionId",
                        "applicationId",
                        selectedOption,
                        selectedAccessToken,
                        selectedLocationId,
                        applicationID);
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
