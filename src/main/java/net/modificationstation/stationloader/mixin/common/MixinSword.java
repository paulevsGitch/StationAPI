package net.modificationstation.stationloader.mixin.common;

import net.minecraft.item.tool.Sword;
import net.minecraft.item.tool.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Sword.class)
public class MixinSword implements net.modificationstation.stationloader.api.common.item.tool.Sword {

    @Inject(method = "<init>(ILnet/minecraft/item/tool/ToolMaterial;)V", at = @At("RETURN"))
    private void captureToolMaterial(int i, ToolMaterial arg, CallbackInfo ci) {
        toolMaterial = arg;
    }

    private ToolMaterial toolMaterial;

    @Override
    public int getToolLevel() {
        return toolMaterial.getMiningLevel();
    }
}
