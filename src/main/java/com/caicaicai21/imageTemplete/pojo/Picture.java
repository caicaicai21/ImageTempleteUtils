package com.caicaicai21.imageTemplete.pojo;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.caicaicai21.imageTemplete.downloader.HttpImageDownloader;
import com.caicaicai21.imageTemplete.pojo.ColorBlock.GraphType;
import com.caicaicai21.imageTemplete.utils.ImageTempleteUtils;
import com.caicaicai21.imageTemplete.utils.ImageTempleteUtils.IDrawItem;

public class Picture implements IDrawItem {
	public static interface IOnlineImageDownloader {
		public BufferedImage getOnlineImage(String url);

		public InputStream getImageInputStream(String url);

		public void downloadComplete();
	}

	private String imagePath = null;
	private int height = -1;
	private int width = -1;
	private int positionX = 0;
	private int positionY = 0;
	private float alpha = 1f;
	private float zoom = 1f;
	private int orderIndex = 0;

	private float positionXP = 0f;
	private float positionYP = 0f;

	private boolean center = false;

	private IOnlineImageDownloader imageDownloader = new HttpImageDownloader();

	private int graphType = GraphType.TYPE_RECT;

	public Picture() {
	}

	public Picture(String imagePath) {
		this.imagePath = imagePath;
	}

	public Picture(String imagePath, int height, int width) {
		this(imagePath, height, width, 0, 0);
	}

	public Picture(String imagePath, int height, int width, int positionX, int positionY) {
		this.imagePath = imagePath;
		this.height = height;
		this.width = width;
		this.positionX = positionX;
		this.positionY = positionY;
	}

	public int getOrderIndex() {
		return orderIndex;
	}

	public Picture setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
		return this;
	}

	public int getPositionX() {
		return positionX;
	}

	public Picture setPositionX(int positionX) {
		this.positionX = positionX;
		return this;
	}

	public int getPositionY() {
		return positionY;
	}

	public Picture setPositionY(int positionY) {
		this.positionY = positionY;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public Picture setHeight(int height) {
		this.height = height;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public Picture setWidth(int width) {
		this.width = width;
		return this;
	}

	public String getImagePath() {
		return imagePath;
	}

	public Picture setImagePath(String imagePath) {
		this.imagePath = imagePath;
		return this;
	}

	public float getAlpha() {
		if (Float.compare(alpha, 1) >= 0)
			return 1;
		if (Float.compare(alpha, 0) <= 0)
			return 0;
		return alpha;
	}

	public Picture setAlpha(float alpha) {
		this.alpha = alpha;
		return this;
	}

	public boolean isOvalImage() {
		return graphType == GraphType.TYPE_OVAL;
	}

	public Picture setOvalImage() {
		this.graphType = GraphType.TYPE_OVAL;
		return this;
	}

	public float getZoom() {
		return zoom;
	}

	/**
	 * 以原图大小为准，比例缩放，如果已经设置了 height、width，则此设置无效
	 * 
	 * @param zoom
	 * @return
	 */
	public Picture setZoom(float zoom) {
		this.zoom = zoom;
		return this;
	}

	public IOnlineImageDownloader getImageDownloader() {
		return imageDownloader;
	}

	public Picture setImageDownloader(IOnlineImageDownloader imageDownloader) {
		this.imageDownloader = imageDownloader;
		return this;
	}

	public float getPositionXP() {
		return positionXP;
	}

	/**
	 * 相对背景图片宽度的百分比
	 */
	public Picture setPositionXP(float positionXP) {
		this.positionXP = positionXP;
		return this;
	}

	public float getPositionYP() {
		return positionYP;
	}

	/**
	 * 相对背景图片高度的百分比
	 */
	public Picture setPositionYP(float positionYP) {
		this.positionYP = positionYP;
		return this;
	}

	public boolean isCenter() {
		return center;
	}

	/**
	 * 对 positionXP、positionYP 有效，true 以图像中心定位，false 以图像左上角定位
	 * 
	 * @param center
	 * @return
	 */
	public Picture setCenter(boolean center) {
		this.center = center;
		return this;
	}

	@Override
	public void drawItem(Graphics2D graphics2d, Dimension backgroundDimension) {
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Image pImage = getOriginalImage();

		if (pImage == null)
			return;

		int width = this.getWidth() < 0 ? (int) (pImage.getWidth(null) * this.getZoom()) : this.getWidth();
		int height = this.getHeight() < 0 ? (int) (pImage.getHeight(null) * this.getZoom()) : this.getHeight();

		if (backgroundDimension != null) {
			int backgroundHeight = backgroundDimension.height;
			int backgroundWidth = backgroundDimension.width;
			if (this.getPositionXP() > 0 && this.getPositionX() == 0)
				this.setPositionX((int) (backgroundWidth * this.getPositionXP()));
			if (this.getPositionYP() > 0 && this.getPositionY() == 0)
				this.setPositionY((int) (backgroundHeight * this.getPositionYP()));
		}

		if (this.isCenter()) {
			this.setPositionX(this.getPositionX() - (this.getWidth() / 2));
			this.setPositionY(this.getPositionY() - (this.getHeight() / 2));
		}

		graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.getAlpha()));
		if (this.isOvalImage()) {
			Ellipse2D.Double shape = new Ellipse2D.Double(this.getPositionX(), this.getPositionY(), width, height);
			graphics2d.setClip(shape);
		}
		graphics2d.drawImage(pImage, this.getPositionX(), this.getPositionY(), width, height, null);

		if (this.isOvalImage()) {
			// 消除锯齿 - 白色背景比较有效
			graphics2d.setColor(new Color(182, 188, 204));
			graphics2d.drawOval(this.getPositionX(), this.getPositionY(), width, height);
		}
	}

	public BufferedImage getOriginalImage() {
		String path = this.getImagePath();
		if (isOnlineImage(path)) {
			return getOnlinePicture(path, this.getImageDownloader());
		} else if (path != null) {
			File pFile = new File(path);
			if (!pFile.exists() || pFile.isDirectory()) {
				System.err.println("image '" + path + "' not exists");
				return null;
			}
			return ImageTempleteUtils.readImageFromFile(pFile);
		}
		return null;
	}

	private BufferedImage getOnlinePicture(String url, IOnlineImageDownloader downloader) {
		BufferedImage image = null;
		if (downloader == null) {
			System.err.println("All OnlineImageDownloader is NULL!! Please check.");
			return null;
		}
		try {
			image = downloader.getOnlineImage(url);
			if (image == null) {
				InputStream is = downloader.getImageInputStream(url);
				if (is != null) {
					image = ImageIO.read(is);
					is.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (image == null)
			System.err.println("Image: <" + url
					+ "> download fail!\nPlease check the OnlineImageDownloader and image url is normal.");
		downloader.downloadComplete();
		return image;
	}

	/**
	 * 匹配 http、https、ftp、ftps
	 * 
	 * @param text
	 * @return
	 */
	private boolean isOnlineImage(String text) {
		if (text == null || text.trim().isEmpty())
			return false;
		return text.matches(
				"^((ht|f)tps?):\\/\\/[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-\\.,@?^=%&:\\/~\\+#]*[\\w\\-\\@?^=%&\\/~\\+#])?$");
	}

	@Override
	public int getOrder() {
		return this.orderIndex;
	}
}
