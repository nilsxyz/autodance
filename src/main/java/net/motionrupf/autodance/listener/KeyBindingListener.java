package net.motionrupf.autodance.listener;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.motionrupf.autodance.AutodanceMod;
import net.motionrupf.autodance.data.KeyBindings;
import net.motionrupf.autodance.util.TextUtil;
import org.apache.logging.log4j.Logger;

public class KeyBindingListener {
    private final AutodanceMod mod;
    private final Logger logger;

    public KeyBindingListener(AutodanceMod mod, Logger logger) {
        this.mod = mod;
        this.logger = logger;
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        if(KeyBindings.TOGGLE_MOD.isPressed()) {
            mod.toggleAutodanceEnabled();
            Minecraft.getMinecraft().player.sendMessage(TextUtil.formatComponent("message.autodance.status",
                    mod.isAutodanceEnabled() ? TextUtil.format("message.autodance.status.enabled")
                            : TextUtil.format("message.autodance.status.disabled")));
        }

        if(KeyBindings.TOGGLE_AUTOJOIN.isPressed()) {
            mod.toggleAutojoinEnabled();
            Minecraft.getMinecraft().player.sendMessage(TextUtil.formatComponent("message.autodance.autojoin",
                    mod.isServerEnabled() ? TextUtil.format("message.autodance.autojoin.enabled")
                            : TextUtil.format("message.autodance.autojoin.disabled")));
        }
    }
}
