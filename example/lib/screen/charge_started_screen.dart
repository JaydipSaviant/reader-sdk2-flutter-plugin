import 'package:flutter/material.dart';
import 'package:readersdk2/readersdk2.dart';
import 'package:readersdk2_example/const/static_string.dart';
import 'package:readersdk2_example/screen/add_credit_card_screen.dart';
import 'package:readersdk2_example/widgets/buttons.dart';
import 'package:readersdk2_example/widgets/square_logo.dart';

class ChargesStartedScreen extends StatefulWidget {
  final String input;
  const ChargesStartedScreen({super.key, required this.input});

  @override
  State<ChargesStartedScreen> createState() => _ChargesStartedScreenState();
}

class _ChargesStartedScreenState extends State<ChargesStartedScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
          child: Column(
        children: [
          Align(
            alignment: Alignment.centerLeft,
            child: IconButton(
              onPressed: () {
                Navigator.pop(context);
              },
              icon: const Icon(
                Icons.cancel,
                size: 40,
                color: Colors.white,
              ),
            ),
          ),
          const SizedBox(
            height: 30,
          ),
          Text(
            widget.input,
            style: const TextStyle(
              color: Colors.white,
              fontSize: 50,
              fontWeight: FontWeight.bold,
            ),
          ),
          SquareLogo(),
          SizedBox(
            height: 30,
          ),
          InkWell(
            onTap: ()async{
            // await Readersdk2.mockReaderUI;
            },
            child: Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(
                StaticString.tapMockReader,
                style: const TextStyle(
                  color: Colors.white,
                  fontSize: 30,
                ),
                textAlign: TextAlign.center,
              ),
            ),
          ),
          const SizedBox(
            height: 20,
          ),
          SQButtonContainer(buttons: [
            SQRaisedButton(
              text: StaticString.manualCardEntry,
              icon: Icons.arrow_forward_ios,
              onPressed: () {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) => const AddCreditCardScreen(),
                    ));
              },
            ),
          ])
        ],
      )),
    );
  }
}
