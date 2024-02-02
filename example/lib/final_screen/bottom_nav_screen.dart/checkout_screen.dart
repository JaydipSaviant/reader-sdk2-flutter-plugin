// ignore_for_file: prefer_const_constructors, prefer_const_constructors_in_immutables, library_private_types_in_public_api, unused_element, prefer_final_fields

/*
Copyright 2022 Square Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:readersdk2/model/models.dart';
import 'package:readersdk2/readersdk2.dart';
import 'package:readersdk2_example/const/static_string.dart';
import 'package:readersdk2_example/final_screen/bottom_nav_screen.dart/add_card_reader_screen.dart';
import 'package:readersdk2_example/screen/charge_started_screen.dart';
import 'package:readersdk2_example/widgets/buttons.dart';
import 'package:readersdk2_example/widgets/loading.dart';
import 'package:readersdk2_example/widgets/network_button.dart';
import 'package:built_collection/built_collection.dart';

const _debug = !bool.fromEnvironment("dart.vm.product");

/// CheckoutScreen shows the checkout button and reader settings
class CheckoutScreen extends StatefulWidget {
  const CheckoutScreen({super.key});

  @override
  _CheckoutScreenState createState() => _CheckoutScreenState();
}

class _CheckoutScreenState extends State<CheckoutScreen> {
  final TextEditingController textEditingController = TextEditingController();
  bool _isLoading = false;

  _showTransactionDialog(CheckoutResult checkoutResult) {
    // amount is in cents
    var formattedAmount = NumberFormat.simpleCurrency(
            name: checkoutResult.totalMoney.currencyCode)
        .format(checkoutResult.totalMoney.amount / 100);

    showDialog(
        context: context,
        builder: (var context) => AlertDialog(
              title: Text(
                '$formattedAmount Successfully Charged',
                style: TextStyle(
                  color: Colors.black,
                ),
              ),
              content: SingleChildScrollView(
                child: ListBody(
                  children: <Widget>[
                    Text(
                      'See the debugger console for transaction details. You can refund transactions from your Square Dashboard.',
                      style: TextStyle(
                        color: Colors.black,
                      ),
                    ),
                  ],
                ),
              ),
              actions: <Widget>[
                TextButton(
                  child: Text('OK'),
                  onPressed: () {
                    Navigator.of(context).pop();
                  },
                ),
              ],
            ));
  }

  @override
  Widget build(BuildContext context) => Scaffold(
        // floatingActionButtonLocation: FloatingActionButtonLocation.startFloat,
        // floatingActionButton: NetworkButton(
        //   onTap: () {
        //     // Navigator.push(context, MaterialPageRoute(
        //     //   builder: (context) {
        //     //     return const CheckoutScreen();
        //     //   },
        //     // ));
        //   },
        // ),
        appBar: AppBar(
          title: Text(StaticString.squreReaders),
        ),
        body: _isLoading
            ? LoadingWidget()
            : Container(
                margin: EdgeInsets.only(top: 30.0),
                child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      SQButtonContainer(buttons: [
                        SQRaisedButton(
                          backgroundColor: Colors.white,
                          foregroundColor: Colors.black,
                          text: StaticString.charge,
                          onPressed: () async {
                            var builder = CheckoutParametersBuilder();
                            builder.amountMoney = MoneyBuilder()
                              ..amount = 1
                              ..currencyCode =
                                  'USD'; // currencyCode is optional
                            // Optional for all following configuration
                            builder.skipReceipt = false;
                            builder.collectSignature = true;
                            builder.allowSplitTender = false;
                            builder.delayCapture = false;
                            builder.note = 'Hello 💳 💰 World!';
                            builder.additionalPaymentTypes = ListBuilder([
                              AdditionalPaymentType.cash,
                              AdditionalPaymentType.manualCardEntry,
                              AdditionalPaymentType.other
                            ]);
                            builder.tipSettings = TipSettingsBuilder()
                              ..showCustomTipField = true
                              ..showSeparateTipScreen = false
                              ..tipPercentages = ListBuilder([15, 20, 30]);
                            CheckoutParameters checkoutParameters =
                                builder.build();
                            try {
                              var checkoutResult =
                                  await Readersdk2.startCheckout(
                                      checkoutParameters);
                              await Readersdk2.paymentResult;
                              debugPrint(
                                  "paymentttt == ${Readersdk2.paymentResult}");

                              //         .then((value) {
                              //   debugPrint(
                              //       "value payment == $value");
                              // })
                              //  ;

                              // _showTransactionDialog(checkoutResult);
                            } on ReaderSdk2Exception catch (e) {
                              debugPrint("payment cancel = $e");
                              switch (e.code) {
                                case ErrorCode.checkoutErrorCanceled:
                                  // Handle canceled transaction here
                                  print('transaction canceled.');
                                  break;
                                case ErrorCode.checkoutErrorSdkNotAuthorized:
                                  // Handle sdk not authorized
                                  Navigator.pushReplacementNamed(context, '/');
                                  break;
                                default:
                                  var errorMessage = e.message!;
                                  if (_debug) {
                                    errorMessage +=
                                        '\n\nDebug Message: ${e.debugMessage}';
                                    print(
                                        '${e.code}:${e.debugCode}:${e.debugMessage}');
                                  }
                              }
                            }
                          },
                        ),
                      ]),
                      SQButtonContainer(buttons: [
                        SQRaisedButton(
                          text: StaticString.squareReaders,
                          onPressed: () {
                            // Navigator.push(
                            //     context,
                            //     MaterialPageRoute(
                            //         builder: (context) =>
                            //             ReaderConnectionScreen()));
                          },
                        ),
                      ]),
                    ]),
              ),
      );
}
