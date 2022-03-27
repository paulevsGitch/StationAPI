package net.modificationstation.stationapi.api.client.render.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.RenderContext;
import net.modificationstation.stationapi.api.client.render.model.json.ModelOverrideList;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.util.collection.WeightedPicker;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class WeightedBakedModel implements BakedModel {
   private final int totalWeight;
   private final List<WeightedBakedModel.Entry> models;
   private final BakedModel defaultModel;
   private final boolean isVanilla;

   public WeightedBakedModel(List<WeightedBakedModel.Entry> models) {
      this.models = models;
      this.totalWeight = WeightedPicker.getWeightSum(models);
      this.defaultModel = models.get(0).model;
      boolean isVanilla = true;
      for (Entry model : models)
         if (!model.model.isVanillaAdapter()) {
            isVanilla = false;
            break;
         }
      this.isVanilla = isVanilla;
   }

   @Override
   public boolean isVanillaAdapter() {
      return isVanilla;
   }

   @Override
   public void emitBlockQuads(BlockView blockView, BlockState state, TilePos pos, Supplier<Random> randomSupplier, RenderContext context) {
      Entry entry = WeightedPicker.getAt(this.models, Math.abs((int) randomSupplier.get().nextLong()) % this.totalWeight);
      if (entry != null)
         entry.model.emitBlockQuads(blockView, state, pos, randomSupplier, context);
   }

   @Override
   public void emitItemQuads(ItemInstance stack, Supplier<Random> randomSupplier, RenderContext context) {
      Entry entry = WeightedPicker.getAt(this.models, Math.abs((int) randomSupplier.get().nextLong()) % this.totalWeight);
      if (entry != null)
         entry.model.emitItemQuads(stack, randomSupplier, context);
   }

   @Override
   public ImmutableList<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
      return Objects.requireNonNull(WeightedPicker.getAt(this.models, Math.abs((int) random.nextLong()) % this.totalWeight)).model.getQuads(state, face, random);
   }

   public boolean useAmbientOcclusion() {
      return this.defaultModel.useAmbientOcclusion();
   }

   public boolean hasDepth() {
      return this.defaultModel.hasDepth();
   }

   public boolean isSideLit() {
      return this.defaultModel.isSideLit();
   }

   public boolean isBuiltin() {
      return this.defaultModel.isBuiltin();
   }

   public Sprite getSprite() {
      return this.defaultModel.getSprite();
   }

   public ModelTransformation getTransformation() {
      return this.defaultModel.getTransformation();
   }

   public ModelOverrideList getOverrides() {
      return this.defaultModel.getOverrides();
   }

   @Environment(EnvType.CLIENT)
   static class Entry extends WeightedPicker.Entry {
      protected final BakedModel model;

      public Entry(BakedModel model, int weight) {
         super(weight);
         this.model = model;
      }
   }

   @Environment(EnvType.CLIENT)
   public static class Builder {
      private final List<WeightedBakedModel.Entry> models = Lists.newArrayList();

      public WeightedBakedModel.Builder add(@Nullable BakedModel model, int weight) {
         if (model != null) {
            this.models.add(new WeightedBakedModel.Entry(model, weight));
         }

         return this;
      }

      @Nullable
      public BakedModel getFirst() {
         if (this.models.isEmpty()) {
            return null;
         } else {
            return this.models.size() == 1 ? this.models.get(0).model : new WeightedBakedModel(this.models);
         }
      }
   }
}
