#ifndef FLUTTER_PLUGIN_READERSDK2_PLUGIN_H_
#define FLUTTER_PLUGIN_READERSDK2_PLUGIN_H_

#include <flutter/method_channel.h>
#include <flutter/plugin_registrar_windows.h>

#include <memory>

namespace readersdk2 {

class Readersdk2Plugin : public flutter::Plugin {
 public:
  static void RegisterWithRegistrar(flutter::PluginRegistrarWindows *registrar);

  Readersdk2Plugin();

  virtual ~Readersdk2Plugin();

  // Disallow copy and assign.
  Readersdk2Plugin(const Readersdk2Plugin&) = delete;
  Readersdk2Plugin& operator=(const Readersdk2Plugin&) = delete;

  // Called when a method is called on this plugin's channel from Dart.
  void HandleMethodCall(
      const flutter::MethodCall<flutter::EncodableValue> &method_call,
      std::unique_ptr<flutter::MethodResult<flutter::EncodableValue>> result);
};

}  // namespace readersdk2

#endif  // FLUTTER_PLUGIN_READERSDK2_PLUGIN_H_
