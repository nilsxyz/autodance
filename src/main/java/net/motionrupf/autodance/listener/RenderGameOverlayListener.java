package net.motionrupf.autodance.listener;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.motionrupf.autodance.AutodanceMod;
import org.apache.logging.log4j.Logger;

import java.awt.Color;

public class RenderGameOverlayListener {
    private AutodanceMod mod;
    private final Logger logger;

    public RenderGameOverlayListener(AutodanceMod mod, Logger logger) {
        this.mod = mod;
        this.logger = logger;
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        fontRenderer.drawString("Status of AutoDance: " + mod.isAutodanceEnabled(), 10, 10 , Color.WHITE.getRGB());

        if(mod.isAutodanceEnabled())  {
            int yOffset = 20;
            if(mod.getAimedColor() != null) {
                fontRenderer.drawString("Aimed color: " + mod.getAimedColor().getName(), 10, yOffset,
                        mod.getAimedColor().getColorValue());
                yOffset += 10;
            }

            if(mod.getAimedPos() != null) {
                fontRenderer.drawString(String.format("Aimed pos: x = %d; y = %d; z = %d",
                        mod.getAimedPos().getX(),
                        mod.getAimedPos().getY(),
                        mod.getAimedPos().getZ()), 10, yOffset, Color.WHITE.getRGB());
                yOffset += 10;
            }
        }
    }
}
