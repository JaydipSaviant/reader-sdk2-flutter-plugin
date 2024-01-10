import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:readersdk2_example/const/static_string.dart';
import 'package:readersdk2_example/screen/add_card_screen.dart';
import 'package:readersdk2_example/widgets/buttons.dart';
import 'package:readersdk2_example/widgets/card_formatter.dart';
import 'package:readersdk2_example/widgets/date_formatter.dart';

class AddCreditCardScreen extends StatefulWidget {
  const AddCreditCardScreen({super.key});

  @override
  State<AddCreditCardScreen> createState() => _AddCreditCardScreenState();
}

class _AddCreditCardScreenState extends State<AddCreditCardScreen> {
  bool cardNumber = false;
  TextEditingController cardNumberController = TextEditingController();
  TextEditingController dateController = TextEditingController();
  TextEditingController cvvController = TextEditingController();
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          icon: Icon(Icons.arrow_back, color: Colors.white),
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
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 15),
              child: TextFormField(
                controller: cardNumberController,
                cursorColor: Colors.white,
                inputFormatters: [
                  FilteringTextInputFormatter.digitsOnly,
                  CardNumberFormatter(),
                ],
                textInputAction: TextInputAction.done,
                keyboardType: TextInputType.number,
                obscureText: !cardNumber,
                decoration: InputDecoration(
                  prefixIcon: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Image.network(
                      'https://upload.wikimedia.org/wikipedia/commons/thumb/2/2a/Mastercard-logo.svg/800px-Mastercard-logo.svg.png',
                      height: 30,
                      width: 30,
                    ),
                  ),
                  suffixIcon: Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: IconButton(
                        onPressed: () {
                          setState(() {
                            cardNumber = !cardNumber;
                          });
                        },
                        icon: const Icon(Icons.lock),
                        color: Colors.white,
                      )),
                  enabledBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.all(Radius.circular(4)),
                    borderSide: BorderSide(width: 2, color: Colors.white38),
                  ),
                  focusedBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.all(Radius.circular(4)),
                    borderSide: BorderSide(width: 1, color: Colors.white),
                  ),
                  hintText: 'XXXX XXXX XXXX XXXX',
                  labelText: 'Card Number',
                ),
                maxLength: 19,
                onChanged: (value) {},
              ),
            ),
            Container(
              margin: const EdgeInsets.symmetric(horizontal: 15, vertical: 20),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Flexible(
                    child: TextFormField(
                      controller: dateController,
                      cursorColor: Colors.white,
                      inputFormatters: [
                        FilteringTextInputFormatter.digitsOnly,
                        DateFormatter(),
                      ],
                      textInputAction: TextInputAction.next,
                      keyboardType: TextInputType.datetime,
                      decoration: const InputDecoration(
                          enabledBorder: OutlineInputBorder(
                            borderRadius: BorderRadius.all(Radius.circular(4)),
                            borderSide:
                                BorderSide(width: 2, color: Colors.white38),
                          ),
                          focusedBorder: OutlineInputBorder(
                            borderRadius: BorderRadius.all(Radius.circular(4)),
                            borderSide:
                                BorderSide(width: 1, color: Colors.white),
                          ),
                          hintText: 'Card Expiry',
                          labelText: 'MM/YY'),
                      maxLength: 5,
                    ),
                  ),
                  const SizedBox(width: 20),
                  Flexible(
                    child: TextFormField(
                      controller: cvvController,
                      cursorColor: Colors.white,
                      textInputAction: TextInputAction.next,
                      keyboardType: TextInputType.number,
                      obscureText: true,
                      decoration: const InputDecoration(
                        enabledBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.all(Radius.circular(4)),
                          borderSide:
                              BorderSide(width: 2, color: Colors.white38),
                        ),
                        focusedBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.all(Radius.circular(4)),
                          borderSide: BorderSide(width: 1, color: Colors.white),
                        ),
                        labelText: 'CVV',
                        hintText: 'CVV Number',
                      ),
                      maxLength: 3,
                    ),
                  ),
                ],
              ),
            ),
            SQButtonContainer(buttons: [
              SQRaisedButton(
                text: StaticString.pay,
                onPressed: () {
                  if (cardNumberController.text.isNotEmpty &&
                      dateController.text.isNotEmpty &&
                      cvvController.text.isNotEmpty) {
                    Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => const AddCardScreen(),
                        ));
                  }
                },
              ),
            ]),
          ],
        ),
      ),
    );
  }
}