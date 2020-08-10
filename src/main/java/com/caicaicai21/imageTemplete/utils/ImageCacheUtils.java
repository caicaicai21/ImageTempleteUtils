package com.caicaicai21.imageTemplete.utils;

import java.awt.image.BufferedImage;
import com.caicaicai21.imageTemplete.cache.WeakMapImageCache;

public class ImageCacheUtils {
	public static interface IImageCache {
		public boolean cacheImage(String key, BufferedImage image, long cacheTimeMillis);

		public BufferedImage getImage(String key);

		public boolean removeCache(String key);

		public boolean removeAll();
	}

	private static IImageCache imageCache = new WeakMapImageCache();

	public static boolean cacheImage(String key, BufferedImage image, long cacheTimeMillis) {
		if (!isCacheEnable() || key == null || key.trim().isEmpty() || image == null)
			return false;
		return imageCache.cacheImage(key, image, cacheTimeMillis);
	}

	public static BufferedImage getCacheImage(String key) {
		if (!isCacheEnable())
			return null;
		return imageCache.getImage(key);
	}

	public static boolean isCacheEnable() {
		Boolean enable = true;
		enable = enable == null ? false : enable;
		return enable;
	}

	public static void remove(String key) {
		imageCache.removeCache(key);
	}

	public static void removeAll() {
		imageCache.removeAll();
	}

	public static IImageCache getImageCache() {
		return imageCache;
	}

	public static void setImageCache(IImageCache imageCache) {
		ImageCacheUtils.imageCache = imageCache;
	}
}