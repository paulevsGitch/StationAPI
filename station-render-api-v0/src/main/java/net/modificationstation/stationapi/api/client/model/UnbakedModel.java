package net.modificationstation.stationapi.api.client.model;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;

@Environment(EnvType.CLIENT)
public interface UnbakedModel {
   Collection<Identifier> getModelDependencies();

   Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences);

   @Nullable
   BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId);
}
