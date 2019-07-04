package net.motionrupf.autodance.data;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBindings {
    public static KeyBinding TOGGLE_MOD = new KeyBinding("keybind.autodance.toggle_mod", Keyboard.KEY_P,
            "keybind.autodance.category");

    public static KeyBinding TOGGLE_AUTOJOIN = new KeyBinding("keybind.autodance.toggle_server", Keyboard.KEY_O,
            "keybind.autodance.category");

    public static void register() {
        ClientRegistry.registerKeyBinding(TOGGLE_MOD);
        ClientRegistry.registerKeyBinding(TOGGLE_AUTOJOIN);
    }
}
