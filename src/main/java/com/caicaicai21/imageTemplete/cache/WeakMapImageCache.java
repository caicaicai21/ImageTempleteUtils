package com.caicaicai21.imageTemplete.cache;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import com.caicaicai21.imageTemplete.utils.ImageCacheUtils.IImageCache;

public class WeakMapImageCache implements IImageCache {
	private static Map<String, CacheImage> cache = Collections.synchronizedMap(new WeakHashMap<String, CacheImage>());

	@Override
	public boolean cacheImage(String key, BufferedImage image, long cacheTimeMillis) {
		if (key == null || key.trim().isEmpty() || image == null)
			return false;
		long cacheTime = System.currentTimeMillis() + cacheTimeMillis;
		if (cacheTimeMillis == -1)
			cacheTime = cacheTimeMillis;
		CacheImage cacheImage = new CacheImage(image, cacheTime);
		cache.put(key, cacheImage);
		return true;
	}

	@Override
	public BufferedImage getImage(String key) {
		CacheImage image = cache.get(key);
		if (image == null) {
			return null;
		} else if (image.isExpire()) {
			removeCache(key);
			return null;
		}
		return image.getBufferedImage();
	}

	@Override
	public boolean removeCache(String key) {
		cache.remove(key);
		return true;
	}

	@Override
	public boolean removeAll() {
		cache.clear();
		return true;
	}
}

class CacheImage {
	private BufferedImage bufferedImage;
	private long expireTime = System.currentTimeMillis();

	public CacheImage(BufferedImage bufferedImage, long expireTime) {
		this.bufferedImage = bufferedImage;
		this.expireTime = expireTime;
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	public boolean isExpire() {
		if (expireTime == -1)
			return false;
		return expireTime < System.currentTimeMillis();
	}
}