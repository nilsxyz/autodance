package net.motionrupf.autodance.listener;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerNameFormat {

    @SubscribeEvent
    public void playerFormat(PlayerEvent.NameFormat event) {
        if(event.getUsername().equals("Janrupf") || event.getUsername().equals("MotionLife")) {
            event.setDisplayname(event.getUsername() + " " + TextFormatting.RESET + TextFormatting.GRAY + "[" + TextFormatting.RED + TextFormatting.BOLD + "MAIN DEVELOPER" + TextFormatting.RESET + TextFormatting.GRAY + "]");
        }
    }
}
