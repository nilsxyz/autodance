package net.motionrupf.autodance.states;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.motionrupf.autodance.AutodanceMod;
import net.motionrupf.autodance.util.ScoreboardUtil;
import net.motionrupf.autodance.util.TextUtil;

public class LobbyState {
    private enum State {
        NONE,
        WAITING_FOR_NAVIGATOR,
        HOLD,
    }

    private final AutodanceMod mod;

    private State state;
    private long waitStartTime = -1;

    public LobbyState(AutodanceMod mod) {
        this.mod = mod;
        state = State.NONE;
    }

    public boolean shouldUpdate(TickEvent.PlayerTickEvent event) {
        return mod.isAutojoinEnabled() && event.player == Minecraft.getMinecraft().player &&
                ScoreboardUtil.testSidebarTitle(Minecraft.getMinecraft().player.getWorldScoreboard(),
                        s -> TextUtil.getWithoutFormatting(s).equalsIgnoreCase("TheHive"));
    }

    public void update(EntityPlayerSP player) {
        switch(state) {
            case NONE: {
                for(int i = 0; i < 9; i++) {
                    ItemStack stack = player.inventory.getStackInSlot(i);
                    if(TextUtil.getWithoutFormatting(stack.getDisplayName())
                            .equalsIgnoreCase("Select Game")) {
                        player.connection.sendPacket(new CPacketHeldItemChange(i));
                        player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        state = State.WAITING_FOR_NAVIGATOR;
                    }
                }

                break;
            }

            case WAITING_FOR_NAVIGATOR: {
                if(waitStartTime == -1) {
                    waitStartTime = System.currentTimeMillis();
                } else if(System.currentTimeMillis() - waitStartTime > 5000) {
                    state = State.NONE;
                    break;
                }

                Container container = player.openContainer;
                if(container != null) {
                    for(Slot slot : container.inventorySlots) {
                        if(slot.getHasStack() && TextUtil.getWithoutFormatting(slot.getStack().getDisplayName())
                                .equalsIgnoreCase("BlockParty")) {
                            player.connection.sendPacket(new CPacketClickWindow(
                                    container.windowId,
                                    slot.getSlotIndex(),
                                    1,
                                    ClickType.PICKUP,
                                    slot.getStack(),
                                    (short) 0
                            ));
                            state = State.HOLD;
                            waitStartTime = System.currentTimeMillis();
                        }
                    }
                }
                break;
            }

            case HOLD: {
                if(System.currentTimeMillis() - waitStartTime > 5000) {
                    waitStartTime = -1;
                    state = State.NONE;
                }
                break;
            }
        }
    }
}
