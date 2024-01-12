import 'package:flutter/material.dart';
import 'package:readersdk2/readersdk2.dart';
import 'package:readersdk2_example/screen/add_authorise_screen.dart';

void main() {
  runApp(const MyApp());
}

const _debug = !bool.fromEnvironment("dart.vm.product");

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final readersdk2Plugin = Readersdk2();

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        debugShowCheckedModeBanner: false,
        theme: _buildTheme(),
        title: 'ReaderSDK',
        home: const AddAuthorisedScreen()
        //const PermissionScreen(),
        );
  }
}

ThemeData _buildTheme() {
  var base = ThemeData.light();
  return base.copyWith(
    backgroundColor: const Color.fromRGBO(64, 135, 225, 1.0),
    canvasColor: const Color.fromARGB(0, 192, 158, 158),
    scaffoldBackgroundColor: const Color.fromRGBO(64, 135, 225, 1.0),
    buttonTheme: const ButtonThemeData(
      height: 64.0,
    ),
    hintColor: Colors.transparent,
    inputDecorationTheme: const InputDecorationTheme(
      labelStyle: TextStyle(
        color: Colors.white,
      ),
    ),
    textTheme: const TextTheme(
        button: TextStyle(
          fontSize: 20.0,
          fontWeight: FontWeight.w600,
          color: Colors.white,
        ),
        bodyText1: TextStyle(
          fontSize: 24.0,
          fontWeight: FontWeight.w600,
          color: Colors.white,
        )),
  );
}
