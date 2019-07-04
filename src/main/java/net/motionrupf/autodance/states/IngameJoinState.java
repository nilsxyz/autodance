package net.motionrupf.autodance.states;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.motionrupf.autodance.AutodanceMod;
import net.motionrupf.autodance.util.ScoreboardUtil;
import net.motionrupf.autodance.util.TextUtil;

public class IngameJoinState {
    private final AutodanceMod mod;

    private boolean joiningNext;

    public IngameJoinState(AutodanceMod mod) {
        this.mod = mod;
    }

    public boolean shouldUpdate(TickEvent.PlayerTickEvent event) {
        return mod.isAutojoinEnabled() && event.player == Minecraft.getMinecraft().player &&
                ScoreboardUtil.testSidebarTitle(event.player.getWorldScoreboard(),
                        s -> TextUtil.getWithoutFormatting(s).equalsIgnoreCase("BlockParty"));
    }

    public void update(EntityPlayerSP player) {
        ItemStack stack = player.inventory.getStackInSlot(1);
        if(TextUtil.getWithoutFormatting(stack.getDisplayName()).equalsIgnoreCase("Join New Game")) {
            joinNext(player, 1);
            return;
        }

        stack = player.inventory.getStackInSlot(4);
        if(TextUtil.getWithoutFormatting(stack.getDisplayName()).equalsIgnoreCase("Join New Game")) {
            joinNext(player, 4);
            return;
        }

        joiningNext = false;
    }

    private void joinNext(EntityPlayerSP player, int slotId) {
        if(joiningNext) {
            return;
        }

        player.connection.sendPacket(new CPacketHeldItemChange(slotId));
        player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
    }
}
