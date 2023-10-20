package net.modificationstation.stationapi.impl.client.texture;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.texture.NativeImage;
import net.modificationstation.stationapi.api.client.texture.NativeImage.Format;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas.Sprite;
import net.modificationstation.stationapi.api.registry.Identifier;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class BufferedTextures {
	private static final Map<Identifier, NativeImage> TEXTURES = new Reference2ObjectOpenHashMap<>();
	private static final Map<Identifier, Sprite> SPRITES = new Reference2ObjectOpenHashMap<>();
	
	public static void addTexture(Identifier id, BufferedImage img, Sprite sprite) {
		int w = img.getWidth();
		int h = img.getHeight();
		int[] pixels = new int[w * h];
		img.getRGB(0, 0, w, h, pixels, 0, w);
		ByteBuffer buffer = ByteBuffer.allocateDirect(pixels.length << 2).order(ByteOrder.BIG_ENDIAN);
		
		for (int pixel : pixels) {
			int rgba = Integer.rotateLeft(pixel, 8);
			buffer.putInt(rgba);
		}
		
		buffer.position(0);
		NativeImage nativeImage = new NativeImage(Format.RGBA, w, h, true, buffer);
		
		TEXTURES.put(id, nativeImage);
		SPRITES.put(id, sprite);
	}
	
	public static NativeImage getTexture(Identifier id) {
		System.out.println("Get " + id);
		return TEXTURES.get(id);
	}
	
	public static net.modificationstation.stationapi.api.client.texture.Sprite getSprite(Identifier id) {
		System.out.println("Sprite: " + id);
		Sprite sprite = SPRITES.get(id);
		return sprite == null ? null : sprite.getSprite();
	}
}
