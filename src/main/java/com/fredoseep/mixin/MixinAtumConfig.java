package com.fredoseep.mixin;

import me.voidxwalker.autoreset.AtumConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = AtumConfig.class, remap = false)
public class MixinAtumConfig {

    /**
     * 拦截 getDebugText 方法。
     * 这是 Atum 生成 F3 右侧文字的源头。
     */
    @Inject(method = "getDebugText", at = @At("RETURN"))
    private void modifyDebugText(CallbackInfoReturnable<List<String>> cir) {
        List<String> list = cir.getReturnValue();
        if (list == null) return;

        for (int i = 0; i < list.size(); i++) {
            String line = list.get(i);
            if (line.contains("Resetting a set seed") || line.contains("Resetting the seed")) {
                list.set(i, "Bt seed filtered from random seeds");
            }
        }
    }
}