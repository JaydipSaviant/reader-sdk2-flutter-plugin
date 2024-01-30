import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:readersdk2/readersdk2.dart';
import 'package:readersdk2_example/const/static_string.dart';
import 'package:readersdk2_example/screen/charge_started_screen.dart';
import 'package:readersdk2_example/widgets/buttons.dart';
import 'package:readersdk2_example/widgets/network_button.dart';

class AddCardReaderScreen extends StatefulWidget {
  const AddCardReaderScreen({super.key});

  @override
  State<AddCardReaderScreen> createState() => _AddCardReaderScreenState();
}

class _AddCardReaderScreenState extends State<AddCardReaderScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      // floatingActionButtonLocation: FloatingActionButtonLocation.startTop,
      // floatingActionButton: NetworkButton(
      //   onTap: () async {
      //     debugPrint("authorizationInit ui button click");
      //     await Readersdk2.mockReaderUI;
      //   },
      // ),
      body: Column(
        children: [NumericKeyboard()],
      ),
    );
  }
}

class NumericKeyboard extends StatefulWidget {
  @override
  _NumericKeyboardState createState() => _NumericKeyboardState();
}

class _NumericKeyboardState extends State<NumericKeyboard> {
  String input = '';

  void onDigitPressed(String digit) {
    setState(() {
      input += digit;
    });
  }

  void onDecimalPressed() {
    if (!input.contains('.')) {
      setState(() {
        input += '.';
      });
    }
  }

  void onClearPressed() {
    setState(() {
      if (input.isNotEmpty) {
        input = input.substring(0, input.length - 1);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        SizedBox(height: 60),
        Text(
          input,
          style: TextStyle(fontSize: 24, color: Colors.black),
        ),
        SizedBox(height: 30),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            buildDigitButton('1'),
            buildDigitButton('2'),
            buildDigitButton('3'),
          ],
        ),
        SizedBox(height: 30),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            buildDigitButton('4'),
            buildDigitButton('5'),
            buildDigitButton('6'),
          ],
        ),
        SizedBox(height: 30),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            buildDigitButton('7'),
            buildDigitButton('8'),
            buildDigitButton('9'),
          ],
        ),
        SizedBox(height: 30),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            buildDigitButton('0'),
            buildDecimalButton(),
            buildClearButton(),
          ],
        ),
        SizedBox(
          height: 60,
        ),
        Padding(
          padding: const EdgeInsets.only(top: 20),
          child: SQButtonContainer(buttons: [
            SQRaisedButton(
              text: StaticString.charges,
              onPressed: () {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) =>
                            ChargesStartedScreen(input: input)));
              },
            ),
          ]),
        ),
      ],
    );
  }

  Widget buildDigitButton(String digit) {
    return ElevatedButton(
      onPressed: () => onDigitPressed(digit),
      child: Padding(
        padding: const EdgeInsets.all(30.0),
        child: Text(digit),
      ),
      style:
          ElevatedButton.styleFrom(primary: Color.fromRGBO(57, 114, 178, 1.0)),
    );
  }

  Widget buildDecimalButton() {
    return ElevatedButton(
      onPressed: onDecimalPressed,
      child: Padding(
        padding: const EdgeInsets.all(30.0),
        child: Text('.'),
      ),
      style:
          ElevatedButton.styleFrom(primary: Color.fromRGBO(57, 114, 178, 1.0)),
    );
  }

  Widget buildClearButton() {
    return ElevatedButton(
      onPressed: onClearPressed,
      child: Padding(
        padding: const EdgeInsets.all(30.0),
        child: Text('Clear'),
      ),
      style: ElevatedButton.styleFrom(primary: Colors.red),
    );
  }
}
