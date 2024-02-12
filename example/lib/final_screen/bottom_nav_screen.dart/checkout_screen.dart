import 'package:flutter/material.dart';
import 'package:readersdk2/model/models.dart';
import 'package:readersdk2/readersdk2.dart';
import 'package:readersdk2_example/const/static_string.dart';
import 'package:readersdk2_example/widgets/buttons.dart';
import 'package:readersdk2_example/widgets/loading.dart';
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

  _showTransactionDialog() {
    // amount is in cents
    // var formattedAmount = NumberFormat.simpleCurrency(
    //         name: checkoutResult.totalMoney.currencyCode)
    //     .format(checkoutResult.totalMoney.amount / 100);

    return showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text(
            'Successfully Charged',
            style: TextStyle(
              color: Colors.black,
            ),
          ),
          content: const SingleChildScrollView(
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
              child: const Text('OK'),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
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
                margin: const EdgeInsets.only(top: 30.0),
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
                              _showTransactionDialog();
                            } on ReaderSdk2Exception catch (e) {
                              debugPrint("payment cancel = $e");
                              switch (e.code) {
                                case ErrorCode.checkoutErrorCanceled:
                                  // Handle canceled transaction here
                                  debugPrint('transaction canceled.');
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
                                    debugPrint(
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
