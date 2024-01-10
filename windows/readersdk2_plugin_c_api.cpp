#include "include/readersdk2/readersdk2_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "readersdk2_plugin.h"

void Readersdk2PluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  readersdk2::Readersdk2Plugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}
