package net.modificationstation.stationapi.mixin.registry;

import net.minecraft.stat.Stat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Stat.class)
public class MixinStat {

//    @Redirect(method = "register()Lnet/minecraft/stat/Stat;", at = @At(value = "INVOKE", target = "Ljava/util/Map;containsKey(Ljava/lang/Object;)Z"))
//    private boolean register(Map<?, ?> map, Object key) {
//        if (key instanceof Integer) {
//            int ID = (int) key;
//            if (LevelRegistry.remapping) {
//                map.remove(ID);
//                Stats.stats.remove(map.get(ID));
//                return false;
//            }
//        }
//        return map.containsKey(key);
//    }
}
