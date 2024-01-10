import 'package:shared_preferences/shared_preferences.dart';

class SharedPref {
  SharedPref._privateConstructor();
  static final instance = SharedPref._privateConstructor();
  factory SharedPref() {
    return instance;
  }
  //signup response save
  // signUpResponseSave(value) async {
  //   final prefs = await SharedPreferences.getInstance();
  //   prefs.setString("SignupData", value);
  // }
  // /// Get signup response
  // getUserSignupResponse() async {
  //   final prefs = await SharedPreferences.getInstance();
  //   String userSignupResponse = prefs.getString("SignupData") ?? "";
  //   return userSignupResponse;
  // }
}
