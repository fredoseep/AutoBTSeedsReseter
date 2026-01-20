package com.fredoseep.mixin;

import me.voidxwalker.autoreset.Atum;
import net.minecraft.client.gui.DrawableHelper; // 👈 关键：目标是 DrawableHelper
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DrawableHelper.class)
public class MixinDrawableHelper {

    /**
     * 拦截 drawCenteredString 的第一个字符串参数 (text)。
     * 在方法执行的一开始 (HEAD) 就把文字换掉。
     */
    @ModifyVariable(
            method = "drawCenteredString(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
            at = @At("HEAD"),
            ordinal = 0, // 第一个 String 参数
            argsOnly = true
    )
    private String replaceSetSeedText(String text) {
        if (Atum.isRunning() && text != null) {
            if (text.equals("Set Seed")) {
                return "Generating a bt seed from random seeds";
            }
        }
        return text;
    }
}