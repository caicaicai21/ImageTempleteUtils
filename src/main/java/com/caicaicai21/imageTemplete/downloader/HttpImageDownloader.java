package com.caicaicai21.imageTemplete.downloader;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

import com.caicaicai21.imageTemplete.pojo.Picture.IOnlineImageDownloader;

public class HttpImageDownloader implements IOnlineImageDownloader {
	@Override
	public BufferedImage getOnlineImage(String url) {
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setConnectTimeout(5 * 1000);
			connection.setReadTimeout(10 * 1000);
			connection.setRequestMethod("GET");
			connection.setInstanceFollowRedirects(false);
			connection.connect();
			InputStream is = connection.getInputStream();
			BufferedImage image = ImageIO.read(is);
			is.close();
			return image;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.disconnect();
		}
		return null;
	}

	@Override
	public InputStream getImageInputStream(String url) {
		return null;
	}

	@Override
	public void downloadComplete() {

	}
}
