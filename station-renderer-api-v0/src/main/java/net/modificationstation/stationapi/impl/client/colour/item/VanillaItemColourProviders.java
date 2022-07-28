package net.modificationstation.stationapi.impl.client.colour.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.client.colour.block.BlockColors;
import net.modificationstation.stationapi.api.client.colour.item.ItemColors;
import net.modificationstation.stationapi.api.client.event.colour.item.ItemColorsRegisterEvent;
import net.modificationstation.stationapi.api.item.ItemConvertible;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class VanillaItemColourProviders {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerItemColors(ItemColorsRegisterEvent event) {
        BlockColors blockColors = event.blockColors;
        ItemColors itemColors = event.itemColors;
        itemColors.register((stack, tintIndex) -> {
            BlockState blockState = ((BlockStateHolder) BlockBase.BY_ID[stack.itemId]).getDefaultState();
            return blockColors.getColor(blockState, null, null, tintIndex);
        }, (ItemConvertible) BlockBase.GRASS, (ItemConvertible) BlockBase.TALLGRASS, (ItemConvertible) BlockBase.LEAVES);
    }
}