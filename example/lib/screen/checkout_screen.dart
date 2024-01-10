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
import 'package:readersdk2_example/const/static_string.dart';
import 'package:readersdk2_example/screen/charge_started_screen.dart';
import 'package:readersdk2_example/widgets/buttons.dart';
import 'package:readersdk2_example/widgets/loading.dart';
import 'package:readersdk2_example/widgets/network_button.dart';

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

  @override
  Widget build(BuildContext context) => Scaffold(
        floatingActionButtonLocation: FloatingActionButtonLocation.startFloat,
        floatingActionButton: NetworkButton(
          onTap: () {
            // Navigator.push(context, MaterialPageRoute(
            //   builder: (context) {
            //     return const CheckoutScreen();
            //   },
            // ));
          },
        ),
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
                          onPressed: () {
                             Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                          builder: (context) =>
                                           ChargesStartedScreen()));
                            
                            // Readersdk2().startCheckout({
                            //   'amount': StaticString.amount,
                              // 'param2': 'value2',
                              // //   }).then((value) {
                              //     Navigator.push(
                              //         context,
                              //         MaterialPageRoute(
                              //             builder: (context) =>
                              //              ChargesStartedScreen()));
                            //});
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
                      SQButtonContainer(buttons: [
                        SQRaisedButton(
                          text: StaticString.logOut,
                          onPressed: () {},
                        ),
                      ]),
                    ]),
              ),
      );
}
