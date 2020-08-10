package com.caicaicai21.imageTemplete.pojo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import com.caicaicai21.imageTemplete.utils.ImageTempleteUtils.IDrawBackground;
import com.caicaicai21.imageTemplete.utils.ImageTempleteUtils.IDrawItem;

public class ImageTemplete implements IDrawBackground {
	public static final int DEFAULT_BACKGROUND_HEIGHT = 960;
	public static final int DEFAULT_BACKGROUND_WIDTH = 480;
	public static final Color DEFAULT_IMAGE_BACKGROUND_COLOR = Color.WHITE;

	private int imageHeight = -1;
	private int imageWidth = -1;

	private Picture backgroundPicture = new Picture(null);
	private ColorBlock backgroundColorBlock = new ColorBlock(null);

	private List<IDrawItem> drawItems;

	public ImageTemplete() {
	}

	public ImageTemplete(Color backgroundColor) {
		this(backgroundColor, DEFAULT_BACKGROUND_HEIGHT, DEFAULT_BACKGROUND_WIDTH);
	}

	public ImageTemplete(Color backgroundColor, int imageHeight, int imageWidth) {
		this.backgroundColorBlock.setColor(backgroundColor);
		this.imageHeight = imageHeight;
		this.imageWidth = imageWidth;
	}

	public ImageTemplete(String backgroundPath) {
		this(backgroundPath, -1, -1);
	}

	public ImageTemplete(String backgroundPath, int imageHeight, int imageWidth) {
		this.backgroundPicture.setImagePath(backgroundPath);
		this.imageHeight = imageHeight;
		this.imageWidth = imageWidth;
	}

	public ImageTemplete(File backgroundFile) {
		this(backgroundFile, -1, -1);
	}

	public ImageTemplete(File backgroundFile, int imageHeight, int imageWidth) {
		this.backgroundPicture.setImagePath(backgroundFile.getAbsolutePath());
		this.imageHeight = imageHeight;
		this.imageWidth = imageWidth;
	}

	public ImageTemplete setBackground(String backgroundPath) {
		this.backgroundPicture.setImagePath(backgroundPath);
		return this;
	}

	public ImageTemplete setBackgroundColor(Color backgroundColor) {
		this.backgroundColorBlock.setColor(backgroundColor);
		return this;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public ImageTemplete setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
		return this;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public ImageTemplete setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
		return this;
	}

	public ImageTemplete setBackgroundColorGraphType(int backgroundColorGraphType) {
		this.backgroundColorBlock.setGraphType(backgroundColorGraphType);
		return this;
	}

	public ImageTemplete setBackgroundColorDrawType(int backgroundColorDrawType) {
		this.backgroundColorBlock.setDrawType(backgroundColorDrawType);
		return this;
	}

	public ImageTemplete setBackgroundColorStroke(Stroke backgroundColorStroke) {
		this.backgroundColorBlock.setStroke(backgroundColorStroke);
		return this;
	}

	public ImageTemplete setBackgroundColorBlock(ColorBlock backgroundColorBlock) {
		this.backgroundColorBlock = backgroundColorBlock;
		return this;
	}

	public ImageTemplete setBackgroundPicture(Picture backgroundPicture) {
		this.backgroundPicture = backgroundPicture;
		return this;
	}

	public void setDrawItems(List<IDrawItem> drawItems) {
		this.drawItems = drawItems;
	}

	@Override
	public List<IDrawItem> getDrawItemList() {
		return this.drawItems;
	}

	@Override
	public BufferedImage drawBackground() {
		BufferedImage baseImage = null;
		Graphics2D graphics2d = null;
		int backgroundHeight = 0;
		int backgroundWidth = 0;

		if (backgroundPicture != null) {
			Image bImage = backgroundPicture.getOriginalImage();

			if (bImage != null) {
				backgroundHeight = this.getImageHeight() <= 0 ? bImage.getHeight(null) : this.getImageHeight();
				backgroundWidth = this.getImageWidth() <= 0 ? bImage.getWidth(null) : this.getImageWidth();

				baseImage = new BufferedImage(backgroundWidth, backgroundHeight, BufferedImage.TYPE_INT_RGB);
				graphics2d = baseImage.createGraphics();
				graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				graphics2d.drawImage(bImage, 0, 0, baseImage.getWidth(), baseImage.getHeight(), null);
			}
		}

		// 颜色背景
		if (baseImage == null) {
			backgroundHeight = this.getImageHeight() <= 0 ? DEFAULT_BACKGROUND_HEIGHT : this.getImageHeight();
			backgroundWidth = this.getImageWidth() <= 0 ? DEFAULT_BACKGROUND_WIDTH : this.getImageWidth();

			baseImage = new BufferedImage(backgroundWidth, backgroundHeight, BufferedImage.TYPE_INT_RGB);
			graphics2d = baseImage.createGraphics();

			// 透明背景
			baseImage = graphics2d.getDeviceConfiguration().createCompatibleImage(baseImage.getWidth(),
					baseImage.getHeight(), Transparency.TRANSLUCENT);
			graphics2d = baseImage.createGraphics();

			Color color = backgroundColorBlock != null ? backgroundColorBlock.getColor()
					: DEFAULT_IMAGE_BACKGROUND_COLOR;

			ColorBlock colorBlock = backgroundColorBlock;
			colorBlock.setColor(color);
			colorBlock.setWidth(baseImage.getWidth());
			colorBlock.setHeight(baseImage.getHeight());
			if (colorBlock.getFloorColor() == null)
				colorBlock.setFloorColor(Color.WHITE);

			colorBlock.drawItem(graphics2d, new Dimension(backgroundWidth, backgroundHeight));
		}

		if (graphics2d != null)
			graphics2d.dispose();

		return baseImage;
	}
}
