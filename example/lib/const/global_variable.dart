class GlobalSingleton {
  static final GlobalSingleton _singleton = GlobalSingleton.internal();

  factory GlobalSingleton() {
    return _singleton;
  }

  GlobalSingleton.internal();

  bool isCompleteAuthorized = false;
}
