package net.motionrupf.autodance;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.motionrupf.autodance.data.KeyBindings;
import net.motionrupf.autodance.listener.KeyBindingListener;
import net.motionrupf.autodance.listener.PlayerNameFormat;
import net.motionrupf.autodance.listener.PlayerTickListener;
import net.motionrupf.autodance.listener.RenderGameOverlayListener;
import org.apache.logging.log4j.Logger;

@Mod(modid = "autodance")
public class AutodanceMod {
    private Logger logger;
    private boolean enabled;

    private EnumDyeColor aimedColor;
    private BlockPos aimedPos;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("Initializing AutoDance...");
        KeyBindings.register();
        logger.info("Registered key bindings...");
        MinecraftForge.EVENT_BUS.register(new KeyBindingListener(this, logger));
        MinecraftForge.EVENT_BUS.register(new PlayerTickListener(this, logger));
        MinecraftForge.EVENT_BUS.register(new RenderGameOverlayListener(this, logger));
        MinecraftForge.EVENT_BUS.register(new PlayerNameFormat());
        logger.info("AutoDance initialized!");
    }

    public void toggleEnabled() {
        enabled = !enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setAimedColor(EnumDyeColor aimedColor) {
        this.aimedColor = aimedColor;
    }

    public EnumDyeColor getAimedColor() {
        return aimedColor;
    }

    public void setAimedPos(BlockPos aimedPos) {
        this.aimedPos = aimedPos;
    }

    public BlockPos getAimedPos() {
        return aimedPos;
    }
}
