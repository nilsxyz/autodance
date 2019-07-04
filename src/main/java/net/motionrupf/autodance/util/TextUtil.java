package net.motionrupf.autodance.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public final class TextUtil {
    private static final String PREFIX = TextFormatting.GRAY + "[" + TextFormatting.RED +
            "AutoDance" + TextFormatting.GRAY + "] " + TextFormatting.RESET;

    public static ITextComponent formatComponent(String localization, Object... args) {
        return new TextComponentString(PREFIX).appendSibling(new TextComponentTranslation(localization, args));
    }

    public static String format(String localization, Object... args) {
        return I18n.format(localization, args);
    }
}
