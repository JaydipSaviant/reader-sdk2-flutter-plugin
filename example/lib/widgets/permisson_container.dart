import 'package:flutter/material.dart';

class PermissionContainer extends StatelessWidget {
  final String title;
  final String subtitle;
  final bool isEnable;
  final void Function()? onPressed;
  const PermissionContainer({
    super.key,
    required this.title,
    required this.subtitle,
    required this.isEnable,
    this.onPressed,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 10),
      child: Card(
        child: ListTile(
          onTap: onPressed,
          contentPadding:
              const EdgeInsets.symmetric(vertical: 10, horizontal: 10),
          title: Padding(
            padding: const EdgeInsets.symmetric(vertical: 10),
            child: Text(
              title,
              style: const TextStyle(fontSize: 20, color: Colors.black),
            ),
          ),
          subtitle: Text(
            subtitle,
            style: const TextStyle(fontSize: 12, color: Colors.grey),
          ),
          trailing: Icon(
            isEnable ? Icons.check_circle : Icons.circle_outlined,
            color: Colors.green,
            size: 30,
          ),
        ),
      ),
    );
  }
}
