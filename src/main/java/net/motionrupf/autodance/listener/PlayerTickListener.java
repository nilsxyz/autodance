package net.motionrupf.autodance.listener;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.motionrupf.autodance.AutodanceMod;
import net.motionrupf.autodance.states.IngameDanceState;
import net.motionrupf.autodance.states.IngameJoinState;
import net.motionrupf.autodance.states.LobbyState;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class PlayerTickListener {
    private final AutodanceMod mod;
    private final Logger logger;

    private final IngameDanceState ingameDanceState;
    private final IngameJoinState ingameJoinState;
    private final LobbyState lobbyState;

    public PlayerTickListener(AutodanceMod mod, Logger logger) {
        this.mod = mod;
        this.logger = logger;

        Random random = new Random();
        ingameDanceState = new IngameDanceState(mod, random);
        ingameJoinState = new IngameJoinState(mod);
        lobbyState = new LobbyState(mod);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(ingameJoinState.shouldUpdate(event)) {
            ingameJoinState.update(Minecraft.getMinecraft().player);
        }

        if(ingameDanceState.shouldUpdate(event)) {
            ingameDanceState.update(Minecraft.getMinecraft().player);
            return;
        }

        if(lobbyState.shouldUpdate(event)) {
            lobbyState.update(Minecraft.getMinecraft().player);
        }
    }
}
