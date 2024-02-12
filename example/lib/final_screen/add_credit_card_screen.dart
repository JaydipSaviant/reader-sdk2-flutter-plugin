import 'package:flutter/material.dart';
import 'package:flutter_credit_card/flutter_credit_card.dart';
import 'package:readersdk2/model/models.dart';
import 'package:readersdk2/readersdk2.dart';
import 'package:readersdk2_example/const/static_string.dart';
import 'package:readersdk2_example/widgets/buttons.dart';
import 'package:built_collection/built_collection.dart';
import 'package:intl/intl.dart';

const _debug = !bool.fromEnvironment("dart.vm.product");

class AddCreditCardScreen extends StatefulWidget {
  final String input;
  const AddCreditCardScreen({super.key, required this.input});

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
                                  ..amount = int.parse(widget.input)
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
                                  switch (e.code) {
                                    case ErrorCode.checkoutErrorCanceled:
                                      // Handle canceled transaction here
                                      debugPrint('transaction canceled.');
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
        ));
  }

  _showTransactionDialog() {
    return showDialog(
        context: context,
        builder: (var context) => AlertDialog(
              title: Text(
                'Successfully Charged',
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
