import 'package:flutter/material.dart';
import 'package:readersdk2_example/final_screen/bottom_nav_screen.dart/add_card_reader_screen.dart';
import 'package:readersdk2_example/final_screen/bottom_nav_screen.dart/checkout_screen.dart';
import 'package:readersdk2_example/final_screen/bottom_nav_screen.dart/setting_screen.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  int selectedIndex = 0;

  final List<Widget> screens = [
    AddCardReaderScreen(),
    CheckoutScreen(),
    SettingScreen(),
  ];

  void _onItemTapped(int index) {
    setState(() {
      selectedIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: screens[selectedIndex],
      bottomNavigationBar: BottomNavigationBar(
        items: [
          BottomNavigationBarItem(
            icon: Icon(Icons.keyboard_alt_outlined),
            label: 'Keypad',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.square_outlined),
            label: 'Readers',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.settings),
            label: 'Settings',
          ),
        ],
        selectedItemColor: Colors.white,
        backgroundColor: Color.fromRGBO(57, 114, 178, 1.0),
        currentIndex: selectedIndex,
        onTap: _onItemTapped,
      ),
    );
  }
}
