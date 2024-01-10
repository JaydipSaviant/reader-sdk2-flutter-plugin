import 'package:flutter/material.dart';

class NetworkButton extends StatelessWidget {
  final Function()? onTap;
  const NetworkButton({
    super.key,
    this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20),
      child: InkWell(
        onTap: onTap,
        child: Image.asset(
          "assets/actionbutton.png",
          fit: BoxFit.fill,
          height: 80,
        ),
      ),
    );
  }
}
