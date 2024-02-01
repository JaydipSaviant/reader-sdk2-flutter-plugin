import 'package:shared_preferences/shared_preferences.dart';

class SharedPref {
  SharedPref._privateConstructor();
  static final instance = SharedPref._privateConstructor();
  factory SharedPref() {
    return instance;
  }

  Future<void> saveBool(String key, bool value) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setBool(key, value);
  }

  // Get a boolean value from shared preferences
  Future<bool> getBool(String key) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.getBool(key) ?? false;
  }

  Future<void> saveString(
      String currentEnv,
      String currentToken,
      String currentLoactionId,
      String applicationId,
      String Environment,
      String Token,
      String LocationId,String applicationID) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setString(currentEnv, Environment);
    prefs.setString(currentToken, Token);
    prefs.setString(currentLoactionId, LocationId);
    prefs.setString(applicationId, applicationID);
  }

  Future<Map<String, String>> getStrings(
      String currentEnv, String currentToken, String currentLoactionId, String applicationId) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();

    String value1 = prefs.getString(currentEnv) ?? "";
    String value2 = prefs.getString(currentToken) ?? "";
    String value3 = prefs.getString(currentLoactionId) ?? "";
    String value4 = prefs.getString(applicationId) ?? "";
    return {
      'currentEnv': value1,
      'currentToken': value2,
      'currentLoactionId': value3,
      'applicationId':value4
    };
  }
}


