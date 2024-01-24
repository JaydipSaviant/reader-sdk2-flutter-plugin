import 'package:flutter/material.dart';
import 'package:group_list_view/group_list_view.dart';
import 'package:readersdk2_example/const/static_string.dart';
import 'package:readersdk2_example/screen/checkout_screen.dart';
import 'package:readersdk2_example/widgets/buttons.dart';
import 'package:readersdk2_example/widgets/network_button.dart';

class MockReaderScreen extends StatefulWidget {
  const MockReaderScreen({super.key});

  @override
  State<MockReaderScreen> createState() => _MockReaderScreenState();
}

class _MockReaderScreenState extends State<MockReaderScreen> {
  List<String> items = [];
  String addString = "";
  String addSubString = "";

  Map<String, List> elements = {
    'MAGSTRIPE': ['Swipe', 'Remove Reader'],
    'CONTACTLESS AND CHIP': [
      "Insert Card",
      "Remove Card",
      "Tap Card",
      "Remove Reader",
      "Payments Details"
    ],
  };
  Map<String, List<IconData>> icon = {
    'MAGSTRIPE': [
      Icons.payment,
      Icons.do_disturb,
    ],
    'CONTACTLESS AND CHIP': [
      Icons.file_download_outlined,
      Icons.file_upload_outlined,
      Icons.rss_feed,
      Icons.do_disturb,
      Icons.tune_outlined
    ],
  };
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      floatingActionButtonLocation: FloatingActionButtonLocation.startFloat,
      floatingActionButton: NetworkButton(
        onTap: () {
          Navigator.push(context, MaterialPageRoute(
            builder: (context) {
              return const CheckoutScreen();
            },
          ));
        },
      ),
      appBar: AppBar(
        title: Text(StaticString.squreReaders),
      ),
      body: Column(
        children: [
          const SizedBox(
            height: 20,
          ),
          Visibility(
            visible: items.isNotEmpty,
            child: Column(
              children: [
                Padding(
                  padding: const EdgeInsets.only(bottom: 10),
                  child: SizedBox(
                    child: ListView.builder(
                      shrinkWrap: true,
                      itemCount: items.length,
                      itemBuilder: (context, index) {
                        return Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 10),
                          child: Card(
                            child: ListTile(
                              onTap: () {
                                itemlistBottomSheet(context);
                              },
                              subtitle: Text(items[index],
                                  style: const TextStyle(color: Colors.grey)),
                              trailing: const Icon(Icons.arrow_forward_ios),
                              title: Text(
                                items[index],
                                style: const TextStyle(color: Colors.black),
                              ),
                            ),
                          ),
                        );
                      },
                    ),
                  ),
                ),
              ],
            ),
          ),
          Align(
            alignment: Alignment.center,
            child: SQRaisedButton(
              width: MediaQuery.sizeOf(context).width * 0.90,
              text: StaticString.connectReader,
              onPressed: () {
                addCardBottomSheet(
                  context: context,
                  contactlessOnTap: () {
                    setState(() {
                      addString = StaticString.contactlessChip;
                      addSubString = StaticString.readybattery100;
                    });
                    additems();
                    Navigator.pop(context);
                  },
                  magstripeOnTap: () {
                    setState(() {
                      addString = StaticString.magstripeReader;
                      addSubString = StaticString.ready;
                    });
                    additems();
                    Navigator.pop(context);
                  },
                );
              },
            ),
          ),
        ],
      ),
    );
  }

  Future<dynamic> itemlistBottomSheet(BuildContext context) {
    return showModalBottomSheet(
      context: context,
      builder: (context) {
        return Container(
            color: Colors.white,
            child: GroupListView(
                sectionsCount: elements.keys.toList().length,
                countOfItemInSection: (int section) {
                  return elements.values.toList()[section].length;
                },
                itemBuilder: (BuildContext context, IndexPath index) {
                  return ListTile(
                    onTap: () {
                      navigatorScreen(
                          titleIndex: index.section, index: index.index);
                    },
                    leading:
                        Icon(icon.values.toList()[index.section][index.index]),
                    title: Text(
                      elements.values.toList()[index.section][index.index],
                      style: const TextStyle(color: Colors.black, fontSize: 18),
                    ),
                  );
                },
                groupHeaderBuilder: (BuildContext context, int section) {
                  return Padding(
                    padding:
                        const EdgeInsets.symmetric(horizontal: 15, vertical: 5),
                    child: Text(
                      elements.keys.toList()[section],
                      style: const TextStyle(
                          color: Color.fromARGB(255, 193, 172, 172),
                          fontSize: 18,
                          fontWeight: FontWeight.w600),
                    ),
                  );
                },
                separatorBuilder: (context, index) => const SizedBox(height: 5),
                sectionSeparatorBuilder: (context, section) => section == 0
                    ? const Padding(
                        padding: EdgeInsets.symmetric(horizontal: 10),
                        child: Divider(color: Colors.grey, thickness: 2),
                      )
                    : const SizedBox()
                // const SizedBox(height: 10),
                ));
      },
    );
  }

  additems() {
    setState(() {
      items.add(addString);
    });
  }

  navigatorScreen({required int titleIndex, required int index}) {
    if (titleIndex == 0 && index == 0) {
    } else if (titleIndex == 0 && index == 1) {
    } else if (titleIndex == 1 && index == 0) {
    } else if (titleIndex == 1 && index == 1) {
    } else if (titleIndex == 1 && index == 2) {
    } else if (titleIndex == 1 && index == 3) {
    } else {
      Navigator.push(context, MaterialPageRoute(
        builder: (context) {
          return const CheckoutScreen();
        },
      ));
    }
  }

  Future<dynamic> addCardBottomSheet(
      {required BuildContext context,
      required Function() magstripeOnTap,
      required Function() contactlessOnTap}) {
    return showModalBottomSheet(
      backgroundColor: Colors.white,
      context: context,
      builder: (context) {
        return Padding(
          padding: const EdgeInsets.all(8.0),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              ListTile(
                onTap: magstripeOnTap,
                leading: const Icon(Icons.payment),
                title: Text(
                  StaticString.magstripeReader,
                  style: const TextStyle(color: Colors.black),
                ),
              ),
              ListTile(
                onTap: contactlessOnTap,
                leading: const Icon(Icons.payment),
                title: Text(
                  StaticString.addContactlessReader,
                  style: const TextStyle(color: Colors.black),
                ),
              ),
              const SizedBox(
                height: 20,
              ),
            ],
          ),
        );
      },
    );
  }
}
