// ignore_for_file: use_build_context_synchronously, use_key_in_widget_constructors, library_private_types_in_public_api, unreachable_switch_case

import 'package:barcode_scan2/platform_wrapper.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:readersdk2/model/models.dart';
import 'package:readersdk2/readersdk2.dart';
import 'package:readersdk2_example/screen/checkout_screen.dart';
import 'package:readersdk2_example/widgets/buttons.dart';
import 'package:readersdk2_example/widgets/dialog_modal.dart';
import 'package:readersdk2_example/widgets/loading.dart';
import 'package:readersdk2_example/widgets/square_logo.dart';

const _debug = !bool.fromEnvironment("dart.vm.product");

/// AuthorizeScreen authorizes the reader sdk using a mobile auth
class AuthorizeScreen extends StatefulWidget {
  @override
  _AuthorizeScreenState createState() => _AuthorizeScreenState();
}

class _AuthorizeScreenState extends State<AuthorizeScreen> {
  bool _isLoading = false;

 @override
  void initState() {
    super.initState();
  }


  void authorizeQRCode(String authCode) async {
    try {
      setState(() {
        _isLoading = true;
      });
      await Readersdk2.authorize(authCode);
      debugPrint("auth code authorised = ${Readersdk2.authorize(authCode)}");
     Navigator.push(context, MaterialPageRoute(
            builder: (context) {
              return const CheckoutScreen();
            },
          ));
    } on ReaderSdk2Exception catch (e) {
      switch (e.code) {
        case ErrorCode.authorizeErrorNoNetwork:
          displayErrorModal(context, 'Please connect your device to network.');
          break;
        case ErrorCode.usageError:
          var errorMessage = e.message!;
          if (_debug) {
            errorMessage += '\n\nDebug Message: ${e.debugMessage}';
            print('${e.code}:${e.debugCode}:${e.debugMessage}');
          }
          displayErrorModal(context, errorMessage);
          break;
        case ErrorCode.authorizeErrorNoNetwork:
          // TODO: Handle this case.
          break;
        case ErrorCode.checkoutErrorCanceled:
          // TODO: Handle this case.
          break;
        case ErrorCode.checkoutErrorSdkNotAuthorized:
          // TODO: Handle this case.
          break;
        case ErrorCode.readerSettingsErrorSdkNotAuthorized:
          // TODO: Handle this case.
          break;
        case ErrorCode.storeCustomerErrorCanceled:
          // TODO: Handle this case.
          break;
        case ErrorCode.storeCustomerErrorInvalidCustomerId:
          // TODO: Handle this case.
          break;
        case ErrorCode.storeCustomerErrorNoNetwork:
          // TODO: Handle this case.
          break;
        case ErrorCode.storeCustomerErrorSdkNotAuthorized:
          // TODO: Handle this case.
          break;
        case ErrorCode.usageError:
          // TODO: Handle this case.
          break;
      }
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  Future<void> scan() async {
    try {
      var result = await BarcodeScanner.scan();
      authorizeQRCode(result.rawContent);
      debugPrint("authorise qr code == ${result.rawContent}");
    } on PlatformException catch (e) {
      if (e.code == BarcodeScanner.cameraAccessDenied) {
        displayErrorModal(context, 'Camera Access was not granted');
      } else {
        displayErrorModal(context, e.toString());
      }
    } on Exception catch (e) {
      displayErrorModal(context, e.toString());
    }
  }

  void scanQRCode() {
    scan();
  }

  void manuallyEnterCode() {
    Navigator.pushNamed(context, '/authorize/manual');
  }

  @override
  Widget build(BuildContext context) => Scaffold(
        body: _isLoading
            ? LoadingWidget()
            : Column(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                    SquareLogo(),
                    _Description(),
                    _Buttons(
                      scanQRCode: scanQRCode,
                      manuallyEnterCode: manuallyEnterCode,
                    ),
                  ]),
      );
}

class _Description extends StatelessWidget {
  @override
  Widget build(BuildContext context) => Container(
        margin: const EdgeInsets.symmetric(horizontal: 64.0),
        child: const Column(children: [
          Text(
            'Authorize Reader SDK.',
            textAlign: TextAlign.center,
          ),
          Text(
            'Generate an authorization code in the Reader SDK tab of the Developer Portal',
            textAlign: TextAlign.center,
            style: TextStyle(fontSize: 18.0, fontWeight: FontWeight.w300),
          ),
        ]),
      );
}

class _Buttons extends StatelessWidget {
  final VoidCallback manuallyEnterCode;
  final VoidCallback scanQRCode;

  const _Buttons({
    required this.scanQRCode,
    required this.manuallyEnterCode,
  });

  @override
  Widget build(BuildContext context) => Container(
          child: SQButtonContainer(buttons: [
        SQRaisedButton(
          text: 'Scan QR Code',
          onPressed: scanQRCode,
        ),
        SQOutlineButton(
            text: 'Manually Enter Code', onPressed: manuallyEnterCode),
      ]));
}
