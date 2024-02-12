import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:readersdk2/readersdk2.dart';
import 'package:readersdk2_example/api_intregration/shared_prf.dart';
import 'package:readersdk2_example/final_screen/home_screen.dart';
import 'package:readersdk2_example/final_screen/splash_screen.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'const/global_variable.dart';

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
    GlobalSingleton();
    SharedPref();
    try {
      Readersdk2.channel.setMethodCallHandler((call) async {
        if (call.arguments != null) {
          GlobalSingleton().onlinePayment = call.arguments;
          Navigator.push(
              context,
              MaterialPageRoute(
                builder: (context) => const HomePage(),
              ));
        }
        //  showHtmlPopup(context, call.arguments);

        //   await openPaymentPopup(call.arguments);
      });
    } catch (e) {
      debugPrint("Error setting method call handler: $e");
    }
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    // openPaymentPopup(String htmlContent){
    //   showHtmlPopup(context, htmlContent);
    // }
    return MaterialApp(
        debugShowCheckedModeBanner: false,
        theme: _buildTheme(),
        title: 'ReaderSDK',
        home: const SplashScreen()
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

class HtmlPopupView extends StatelessWidget {
  final String htmlContent;

  HtmlPopupView({required this.htmlContent});

  @override
  Widget build(BuildContext context) {
    return Dialog(
      child: Container(
        width: MediaQuery.of(context).size.width * 0.8,
        height: MediaQuery.of(context).size.height * 0.8,
        child: Column(
          children: [
            Expanded(
              child: WebView(
                initialUrl: 'about:blank',
                onWebViewCreated: (WebViewController webViewController) {
                  webViewController.loadUrl(
                    Uri.dataFromString(
                      htmlContent,
                      mimeType: 'text/html',
                      encoding: Encoding.getByName('utf-8')!,
                    ).toString(),
                  );
                },
              ),
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.pop(context);
              },
              child: const Text('Close'),
            ),
          ],
        ),
      ),
    );
  }
}

// Usage
void showHtmlPopup(BuildContext context, String htmlContent) {
  showDialog(
    context: context,
    builder: (BuildContext context) {
      return AlertDialog(
        title: const Text('HTML Popup'),
        content: HtmlPopupView(htmlContent: htmlContent),
        actions: <Widget>[
          ElevatedButton(
            onPressed: () {
              Navigator.of(context).pop();
            },
            child: const Text('Close'),
          ),
        ],
      );
      //HtmlPopupView(htmlContent: htmlContent);
    },
  );
}

// Call this method where you want to show the HTML popup
// Assuming you have the HTML content in a variable named 'htmlContent'
