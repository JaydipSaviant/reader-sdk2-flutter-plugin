import 'package:flutter/material.dart';
import 'package:readersdk2_example/const/static_string.dart';
import 'package:readersdk2_example/widgets/buttons.dart';

class ReaderConnectionScreen extends StatefulWidget {
  const ReaderConnectionScreen({super.key});

  @override
  State<ReaderConnectionScreen> createState() => _ReaderConnectionScreenState();
}

class _ReaderConnectionScreenState extends State<ReaderConnectionScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(StaticString.squreReaders),
      ),
      body: Column(
        children: [
          SQButtonContainer(buttons: [
            SQRaisedButton(
              text: StaticString.connectReader,
              onPressed: () {
                showDialog(
                  context: context,
                  builder: (context) => AlertDialog(
                    content: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        const SizedBox(
                          height: 40,
                        ),
                        Image.asset("assets/readers_pairing_production.png"),
                        const SizedBox(
                          height: 20,
                        ),
                        Text(
                          StaticString.connectChip,
                          textAlign: TextAlign.center,
                          style: const TextStyle(
                            color: Colors.black,
                            fontSize: 18,
                          ),
                        ),
                        const SizedBox(
                          height: 40,
                        ),
                      ],
                    ),
                  ),
                );
              },
            ),
          ]),
        ],
      ),
    );
  }
}
