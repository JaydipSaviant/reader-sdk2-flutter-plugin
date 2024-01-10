// ignore_for_file: avoid_print
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:readersdk2_example/api_intregration/base_url.dart';

class Api {
  static Future authorizeAPI({required String clientId}) async {
    return http
        .get(Uri.parse(authorize))
        .then(
            (value) => value.statusCode == 302 ? jsonDecode(value.body) : null)
        .catchError((err) =>
            debugPrint("Error while getting all the time slots ====> $err"));
  }
}
