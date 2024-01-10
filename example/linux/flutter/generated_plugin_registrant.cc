//
//  Generated file. Do not edit.
//

// clang-format off

#include "generated_plugin_registrant.h"

#include <readersdk2/readersdk2_plugin.h>

void fl_register_plugins(FlPluginRegistry* registry) {
  g_autoptr(FlPluginRegistrar) readersdk2_registrar =
      fl_plugin_registry_get_registrar_for_plugin(registry, "Readersdk2Plugin");
  readersdk2_plugin_register_with_registrar(readersdk2_registrar);
}
