import 'package:flutter/material.dart';
import 'package:readersdk2/model/model.dart';
import 'package:readersdk2/readersdk2.dart';
import 'package:readersdk2_example/const/static_string.dart';
import 'package:readersdk2_example/widgets/dialog_modal.dart';

const _debug = !bool.fromEnvironment("dart.vm.product");

class AddCardScreen extends StatefulWidget {
  const AddCardScreen({super.key});

  @override
  State<AddCardScreen> createState() => _AddCardScreenState();
}

class _AddCardScreenState extends State<AddCardScreen> {
    
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
          child: Column(
        children: [
          SizedBox(
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                IconButton(
                  onPressed: () {
                    Navigator.pop(context);
                  },
                  icon: const Icon(
                    Icons.cancel,
                    size: 40,
                    color: Colors.white,
                  ),
                ),
                Text(
                  StaticString.amount,
                  style: const TextStyle(
                      fontSize: 30, fontWeight: FontWeight.bold),
                ),
                Container()
              ],
            ),
          ),
          SizedBox(
            height: MediaQuery.of(context).size.height * 0.30,
          ),
          const Center(
            child: SizedBox(
              height: 150,
              width: 150,
              child: CircularProgressIndicator(
                color: Colors.white,
              ),
            ),
          ),
          const SizedBox(
            height: 50,
          ),
          Text(
            StaticString.doNotRemoveCard,
            style: const TextStyle(fontSize: 30, fontWeight: FontWeight.bold),
          ),
          const SizedBox(
            height: 20,
          ),
          Text(
            StaticString.prePayment,
            style: const TextStyle(fontSize: 20, fontWeight: FontWeight.w300),
          ),
        ],
      )),
    );
  }
}


