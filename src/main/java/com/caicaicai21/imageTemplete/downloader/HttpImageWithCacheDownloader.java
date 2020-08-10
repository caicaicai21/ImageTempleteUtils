package com.caicaicai21.imageTemplete.downloader;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

import com.caicaicai21.imageTemplete.utils.ImageCacheUtils;
import com.caicaicai21.imageTemplete.pojo.Picture.IOnlineImageDownloader;

public class HttpImageWithCacheDownloader implements IOnlineImageDownloader {
	private long cacheTimeMillis = 60 * 1000;

	public HttpImageWithCacheDownloader() {

	}

	public HttpImageWithCacheDownloader(long cacheTimeMillis) {
		this.cacheTimeMillis = cacheTimeMillis;
	}

	@Override
	public BufferedImage getOnlineImage(String url) {
		BufferedImage image = getCacheImage(url);
		if (image == null) {
			HttpURLConnection connection = null;
			try {
				connection = (HttpURLConnection) new URL(url).openConnection();
				connection.setConnectTimeout(5 * 1000);
				connection.setReadTimeout(10 * 1000);
				connection.setRequestMethod("GET");
				connection.setInstanceFollowRedirects(false);
				connection.connect();
				InputStream is = connection.getInputStream();
				image = ImageIO.read(is);
				is.close();
				if (image != null) {
					ImageCacheUtils.cacheImage(url, image, cacheTimeMillis);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (connection != null)
					connection.disconnect();
			}
		}
		return image;
	}

	private BufferedImage getCacheImage(String key) {
		return ImageCacheUtils.getCacheImage(key);
	}

	@Override
	public InputStream getImageInputStream(String url) {
		return null;
	}

	@Override
	public void downloadComplete() {

	}
}