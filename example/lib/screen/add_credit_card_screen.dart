import 'package:flutter/material.dart';
import 'package:flutter_credit_card/flutter_credit_card.dart';
import 'package:readersdk2/model/models.dart';
import 'package:readersdk2/readersdk2.dart';
import 'package:readersdk2_example/const/static_string.dart';
import 'package:readersdk2_example/widgets/buttons.dart';
import 'package:built_collection/built_collection.dart';

const _debug = !bool.fromEnvironment("dart.vm.product");

class AddCreditCardScreen extends StatefulWidget {
  const AddCreditCardScreen({super.key});

  @override
  State<AddCreditCardScreen> createState() => _AddCreditCardScreenState();
}

class _AddCreditCardScreenState extends State<AddCreditCardScreen> {
  bool cardNumbers = false;
  String cardNumber = '';
  String expiryDate = '';
  String cardHolderName = '';
  String cvvCode = '';
  bool isCvvFocused = false;
  final GlobalKey<FormState> formKey = GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          leading: IconButton(
            icon: const Icon(Icons.arrow_back, color: Colors.white),
            onPressed: () => Navigator.of(context).pop(),
          ),
          title: Text(StaticString.squareReaders),
        ),
        body: InkWell(
          splashColor: Colors.transparent,
          onTap: () {
            FocusManager.instance.primaryFocus?.unfocus();
          },
          child: Column(
            children: [
              CreditCardWidget(
                cardNumber: cardNumber,
                expiryDate: expiryDate,
                cardHolderName: cardHolderName,
                cvvCode: cvvCode,
                showBackView: false,
                onCreditCardWidgetChange: (CreditCardBrand) {},
              ),
              Expanded(
                child: SingleChildScrollView(
                  child: Column(
                    children: [
                      CreditCardForm(
                        formKey: formKey,
                        obscureCvv: true,
                        obscureNumber: true,
                        cardNumber: cardNumber,
                        cvvCode: cvvCode,
                        isHolderNameVisible: true,
                        isCardNumberVisible: true,
                        isExpiryDateVisible: true,
                        cardHolderName: cardHolderName,
                        expiryDate: expiryDate,
                        inputConfiguration: const InputConfiguration(
                          cardNumberDecoration: InputDecoration(
                            labelText: 'Number',
                            hintText: 'XXXX XXXX XXXX XXXX',
                          ),
                          expiryDateDecoration: InputDecoration(
                            labelText: 'Expired Date',
                            hintText: 'XX/XX',
                          ),
                          cvvCodeDecoration: InputDecoration(
                            labelText: 'CVV',
                            hintText: 'XXX',
                          ),
                          cardHolderDecoration: InputDecoration(
                            labelText: 'Card Holder',
                          ),
                        ),
                        onCreditCardModelChange: onCreditCardModelChange,
                      ),
                      const SizedBox(height: 20),
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 16),
                        child: SQButtonContainer(buttons: [
                          SQRaisedButton(
                            text: StaticString.pay,
                            onPressed: () async {
                              if (formKey.currentState!.validate()) {
                                var builder = CheckoutParametersBuilder();
                                builder.amountMoney = MoneyBuilder()
                                  ..amount = 100
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
                                  print("102 line --- $checkoutResult");
                                  //_showTransactionDialog(checkoutResult);
                                } on ReaderSdk2Exception catch (e) {
                                  debugPrint("payment cancel = $e");
                                  switch (e.code) {
                                    case ErrorCode.checkoutErrorCanceled:
                                      // Handle canceled transaction here
                                      print('transaction canceled.');
                                      break;
                                    case ErrorCode
                                          .checkoutErrorSdkNotAuthorized:
                                      // Handle sdk not authorized
                                      Navigator.pushReplacementNamed(
                                          context, '/');
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
                              }
                            },
                          ),
                        ]),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
        )
        //   body:  
        // InkWell(
        //   splashColor: Colors.transparent,
        //   onTap: () {
        //     FocusManager.instance.primaryFocus?.unfocus();
        //   },
        //   child: Column(
        //     crossAxisAlignment: CrossAxisAlignment.center,
        //     mainAxisAlignment: MainAxisAlignment.center,
        //     children: [
        //       Padding(
        //         padding: const EdgeInsets.symmetric(horizontal: 15),
        //         child: TextFormField(
        //           controller: cardNumberController,
        //           cursorColor: Colors.white,
        //           inputFormatters: [
        //             FilteringTextInputFormatter.digitsOnly,
        //             CardNumberFormatter(),
        //           ],
        //           textInputAction: TextInputAction.done,
        //           keyboardType: TextInputType.number,
        //           obscureText: !cardNumber,
        //           decoration: InputDecoration(
        //             prefixIcon: Padding(
        //               padding: const EdgeInsets.all(8.0),
        //               child: Image.network(
        //                 'https://upload.wikimedia.org/wikipedia/commons/thumb/2/2a/Mastercard-logo.svg/800px-Mastercard-logo.svg.png',
        //                 height: 30,
        //                 width: 30,
        //               ),
        //             ),
        //             suffixIcon: Padding(
        //                 padding: const EdgeInsets.all(8.0),
        //                 child: IconButton(
        //                   onPressed: () {
        //                     setState(() {
        //                       cardNumber = !cardNumber;
        //                     });
        //                   },
        //                   icon: const Icon(Icons.lock),
        //                   color: Colors.white,
        //                 )),
        //             enabledBorder: OutlineInputBorder(
        //               borderRadius: BorderRadius.all(Radius.circular(4)),
        //               borderSide: BorderSide(width: 2, color: Colors.white38),
        //             ),
        //             focusedBorder: OutlineInputBorder(
        //               borderRadius: BorderRadius.all(Radius.circular(4)),
        //               borderSide: BorderSide(width: 1, color: Colors.white),
        //             ),
        //             hintText: 'XXXX XXXX XXXX XXXX',
        //             labelText: 'Card Number',
        //           ),
        //           maxLength: 19,
        //           onChanged: (value) {},
        //         ),
        //       ),
        //       Container(
        //         margin: const EdgeInsets.symmetric(horizontal: 15, vertical: 20),
        //         child: Row(
        //           mainAxisAlignment: MainAxisAlignment.spaceBetween,
        //           children: [
        //             Flexible(
        //               child: TextFormField(
        //                 controller: dateController,
        //                 cursorColor: Colors.white,
        //                 inputFormatters: [
        //                   FilteringTextInputFormatter.digitsOnly,
        //                   DateFormatter(),
        //                 ],
        //                 textInputAction: TextInputAction.next,
        //                 keyboardType: TextInputType.datetime,
        //                 decoration: const InputDecoration(
        //                     enabledBorder: OutlineInputBorder(
        //                       borderRadius: BorderRadius.all(Radius.circular(4)),
        //                       borderSide:
        //                           BorderSide(width: 2, color: Colors.white38),
        //                     ),
        //                     focusedBorder: OutlineInputBorder(
        //                       borderRadius: BorderRadius.all(Radius.circular(4)),
        //                       borderSide:
        //                           BorderSide(width: 1, color: Colors.white),
        //                     ),
        //                     hintText: 'Card Expiry',
        //                     labelText: 'MM/YY'),
        //                 maxLength: 5,
        //               ),
        //             ),
        //             const SizedBox(width: 20),
        //             Flexible(
        //               child: TextFormField(
        //                 controller: cvvController,
        //                 cursorColor: Colors.white,
        //                 textInputAction: TextInputAction.next,
        //                 keyboardType: TextInputType.number,
        //                 obscureText: true,
        //                 decoration: const InputDecoration(
        //                   enabledBorder: OutlineInputBorder(
        //                     borderRadius: BorderRadius.all(Radius.circular(4)),
        //                     borderSide:
        //                         BorderSide(width: 2, color: Colors.white38),
        //                   ),
        //                   focusedBorder: OutlineInputBorder(
        //                     borderRadius: BorderRadius.all(Radius.circular(4)),
        //                     borderSide: BorderSide(width: 1, color: Colors.white),
        //                   ),
        //                   labelText: 'CVV',
        //                   hintText: 'CVV Number',
        //                 ),
        //                 maxLength: 3,
        //               ),
        //             ),
        //           ],
        //         ),
        //       ),
        //       SQButtonContainer(buttons: [
        //         SQRaisedButton(
        //           text: StaticString.pay,
        //           onPressed: () async {
        //             if (cardNumberController.text.isNotEmpty &&
        //                 dateController.text.isNotEmpty &&
        //                 cvvController.text.isNotEmpty) {
        //               var builder = CheckoutParametersBuilder();
        //               builder.amountMoney = MoneyBuilder()
        //                 ..amount = 100
        //                 ..currencyCode = 'USD'; // currencyCode is optional
        //               // Optional for all following configuration
        //               builder.skipReceipt = false;
        //               builder.collectSignature = true;
        //               builder.allowSplitTender = false;
        //               builder.delayCapture = false;
        //               builder.note = 'Hello 💳 💰 World!';
        //               builder.additionalPaymentTypes = ListBuilder([
        //                 AdditionalPaymentType.cash,
        //                 AdditionalPaymentType.manualCardEntry,
        //                 AdditionalPaymentType.other
        //               ]);
        //               builder.tipSettings = TipSettingsBuilder()
        //                 ..showCustomTipField = true
        //                 ..showSeparateTipScreen = false
        //                 ..tipPercentages = ListBuilder([15, 20, 30]);

        //               CheckoutParameters checkoutParameters = builder.build();
        //               try {
        //                 var checkoutResult =
        //                     await Readersdk2.startCheckout(checkoutParameters);
        //                 print("102 line --- $checkoutResult");
        //                 //_showTransactionDialog(checkoutResult);
        //               } on ReaderSdk2Exception catch (e) {
        //                 debugPrint("payment cancel = $e");
        //                 switch (e.code) {
        //                   case ErrorCode.checkoutErrorCanceled:
        //                     // Handle canceled transaction here
        //                     print('transaction canceled.');
        //                     break;
        //                   case ErrorCode.checkoutErrorSdkNotAuthorized:
        //                     // Handle sdk not authorized
        //                     Navigator.pushReplacementNamed(context, '/');
        //                     break;
        //                   default:
        //                     var errorMessage = e.message!;
        //                     if (_debug) {
        //                       errorMessage +=
        //                           '\n\nDebug Message: ${e.debugMessage}';
        //                       print('${e.code}:${e.debugCode}:${e.debugMessage}');
        //                     }
        //                   // displayErrorModal(context, errorMessage);
        //                 }
        //               }
        //               // Navigator.push(
        //               //     context,
        //               //     MaterialPageRoute(
        //               //       builder: (context) => const AddCardScreen(),
        //               //     ));
        //             }
        //           },
        //         ),
        //       ]),
        //     ],
        //   ),
        // ),
        );
  }

  void onCreditCardModelChange(CreditCardModel creditCardModel) {
    setState(() {
      cardNumber = creditCardModel.cardNumber;
      expiryDate = creditCardModel.expiryDate;
      cardHolderName = creditCardModel.cardHolderName;
      cvvCode = creditCardModel.cvvCode;
      isCvvFocused = creditCardModel.isCvvFocused;
    });
  }
}
