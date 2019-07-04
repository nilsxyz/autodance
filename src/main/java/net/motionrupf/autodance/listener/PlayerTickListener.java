package net.motionrupf.autodance.listener;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.motionrupf.autodance.AutodanceMod;
import net.motionrupf.autodance.states.IngameState;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class PlayerTickListener {
    private final AutodanceMod mod;
    private final Logger logger;

    private final IngameState ingameState;

    public PlayerTickListener(AutodanceMod mod, Logger logger) {
        this.mod = mod;
        this.logger = logger;

        Random random = new Random();
        ingameState = new IngameState(mod, random);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(ingameState.shouldUpdate(event)) {
            ingameState.update(Minecraft.getMinecraft().player);
        }
    }
}
