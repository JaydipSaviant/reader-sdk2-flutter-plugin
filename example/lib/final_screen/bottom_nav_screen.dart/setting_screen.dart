import 'package:flutter/material.dart';
import 'package:readersdk2_example/api_intregration/shared_prf.dart';
import 'package:readersdk2_example/const/static_string.dart';
import 'package:readersdk2_example/widgets/buttons.dart';

class SettingScreen extends StatefulWidget {
  const SettingScreen({super.key});

  @override
  State<SettingScreen> createState() => _SettingScreenState();
}

class _SettingScreenState extends State<SettingScreen> {
  String currentEnv = "";
  String currentToken = "";
  String currentLocationId = "";
  String applicationId = "";
  SharedPref sharedPref = SharedPref();

  // Map<String, String>? values;
  TextStyle style = const TextStyle(
      color: Colors.white, fontWeight: FontWeight.w600, fontSize: 18);

  TextStyle subStyle = const TextStyle(
      color: Colors.white60, fontWeight: FontWeight.w400, fontSize: 14);

  getStringData() async {
    Map<String, String> values = await sharedPref.getStrings(
      "currentEnv",
      "currentToken",
      "currentLoactionId",
      "applicationId"
    );
    setState(() {
      currentEnv = values['currentEnv'] ?? "";
      currentToken = values['currentToken'] ?? "";
      currentLocationId = values['currentLoactionId'] ?? "";
      applicationId = values['applicationId'] ?? "";
    });
  }

  @override
  void initState() {
    getStringData();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Setting"),
        centerTitle: true,
      ),
      body: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          ListTile(
            title: Text(StaticString.locations, style: style),
            subtitle: Text(currentEnv, style: subStyle),
          ),
          ListTile(
            title: Text(StaticString.locationId, style: style),
            subtitle: Text(currentLocationId, style: subStyle),
          ),
          ListTile(
            title: Text(StaticString.sdkVersion, style: style),
            subtitle: Text("2.0.0-alpha18", style: subStyle),
          ),
          ListTile(
            title: Text(StaticString.environment, style: style),
            subtitle: Text(currentEnv, style: subStyle),
          ),
          ListTile(
            title: Text(StaticString.applicationID, style: style),
            subtitle: Text(applicationId, style: subStyle),
          ),
          const SizedBox(
            height: 20,
          ),
          SQButtonContainer(buttons: [
            SQRaisedButton(
              text: StaticString.logOut,
              onPressed: () {},
            ),
          ]),
        ],
      ),
    );
  }
}
