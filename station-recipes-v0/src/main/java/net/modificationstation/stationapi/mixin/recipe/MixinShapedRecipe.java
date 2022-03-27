package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.inventory.Crafting;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.ShapedRecipe;
import net.modificationstation.stationapi.api.recipe.StationRecipe;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tags.TagEntry;
import net.modificationstation.stationapi.api.tags.TagRegistry;
import net.modificationstation.stationapi.impl.recipe.tag.ShapedTagRecipeAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Random;

@Mixin(ShapedRecipe.class)
public class MixinShapedRecipe implements ShapedTagRecipeAccessor, StationRecipe {

    @Shadow private int width;
    @Shadow private int height;
    @Shadow private ItemInstance output;
    private Object[] taggedIngredients = new Object[]{};
    private static final Random RANDOM = new Random();

    @Override
    public void setTaggedIngredients(Object[] taggedIngredients) {
        this.taggedIngredients = taggedIngredients;
    }

    /**
     * @author calmilamsy
     * @reason Modified if statement
     */
    @Overwrite
    private boolean matches(Crafting arg, int i, int j, boolean flag) {
        for(int var5 = 0; var5 < 3; ++var5) {
            for(int var6 = 0; var6 < 3; ++var6) {
                int var7 = var5 - i;
                int var8 = var6 - j;
                Object var9 = null;
                if (var7 >= 0 && var8 >= 0 && var7 < this.width && var8 < this.height) {
                    if (flag) {
                        var9 = this.taggedIngredients[this.width - var7 - 1 + var8 * this.width];
                    } else {
                        var9 = this.taggedIngredients[var7 + var8 * this.width];
                    }
                }

                ItemInstance var10 = arg.getInventoryItemXY(var5, var6);
                if (var10 != null || var9 != null) {
                    if (var10 == null && var9 != null || var10 != null && var9 == null) {
                        return false;
                    }

                    if (var9 instanceof ItemInstance item) {
                        if (item.itemId != var10.itemId) {
                            return false;
                        }

                        if (item.getDamage() != -1 && item.getDamage() != var10.getDamage()) {
                            return false;
                        }
                    }
                    else if (!TagRegistry.INSTANCE.tagMatches((Identifier) var9, var10)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public ItemInstance[] getIngredients() {
        ItemInstance[] itemInstances = new ItemInstance[9];
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                int localId = (h * width) + w;
                int id = (h * 3) + w;

                Object entry = taggedIngredients[localId];

                if (entry instanceof Identifier identifier) {
                    List<TagEntry> tagEntries = TagRegistry.INSTANCE.get(identifier).orElseThrow(() -> new RuntimeException("Identifier ingredient \"" + identifier + "\" has no entry in the tag registry!"));
                    itemInstances[id] = tagEntries.get(RANDOM.nextInt(tagEntries.size())).displayItem;
                }
                else {
                    itemInstances[id] = (ItemInstance) entry;
                }
            }
        }

        return itemInstances;
    }

    @Override
    public ItemInstance[] getOutputs() {
        return new ItemInstance[]{output};
    }
}
