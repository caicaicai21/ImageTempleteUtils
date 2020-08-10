package com.caicaicai21.imageTemplete.qr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.caicaicai21.imageTemplete.pojo.Word.IQrCodeFactory;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class ColorfulQrCodeFactory implements IQrCodeFactory {

	private Setting setting = new ColorQrCodeSetting();
	private BufferedImage logo = null;
	private float logoDimension = 0.3f;
	private Color logoBackgroundColor = Color.WHITE;
	private int logoBackgroundColorPadding = 14;

	public ColorfulQrCodeFactory() {
	}

	public ColorfulQrCodeFactory(Setting setting) {
		this.setting = setting;
	}

	public ColorfulQrCodeFactory(BufferedImage logo) {
		this.logo = logo;
	}

	public ColorfulQrCodeFactory(Setting setting, BufferedImage logo) {
		this.setting = setting;
		this.logo = logo;
	}

	@Override
	public BufferedImage getQrImage(String text, int height, int width) {
		if (text == null || text.trim().isEmpty())
			return null;

		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 0);
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

		QRCodeWriter writer = new QRCodeWriter();
		try {
			BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
			return drawColorfulQrCode(bitMatrix);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	private BufferedImage drawColorfulQrCode(BitMatrix bitMatrix) {
		if (bitMatrix == null)
			return null;
		BufferedImage image = null;
		if (setting != null) {
			if (setting instanceof PhotoQrCodeSetting) {
				image = drawQrCodeWithImage(bitMatrix, (PhotoQrCodeSetting) setting);
			} else if (setting instanceof ColorQrCodeSetting) {
				image = drawQrCodeWithColor(bitMatrix, (ColorQrCodeSetting) setting);
			}
		}
		if (image == null)
			image = MatrixToImageWriter.toBufferedImage(bitMatrix);
		if (logo != null) {
			Graphics2D g = image.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			int logoBackgroundWidth = (int) ((image.getWidth() + logoBackgroundColorPadding) * logoDimension);
			int logoBackgroundHeight = (int) ((image.getHeight() + logoBackgroundColorPadding) * logoDimension);
			g.setPaint(logoBackgroundColor);
			g.fillRoundRect((image.getWidth() - logoBackgroundWidth) / 2,
					(image.getHeight() - logoBackgroundHeight) / 2, logoBackgroundWidth, logoBackgroundHeight, 0, 0);

			int logoWidth = (int) (image.getWidth() * logoDimension);
			int logoHeight = (int) (image.getHeight() * logoDimension);
			g.drawImage(logo, (image.getWidth() - logoWidth) / 2, (image.getHeight() - logoHeight) / 2, logoWidth,
					logoHeight, null);
			g.dispose();
		}
		return image;
	}

	private BufferedImage drawQrCodeWithImage(BitMatrix bitMatrix, PhotoQrCodeSetting setting) {
		Integer width = bitMatrix.getWidth();
		Integer height = bitMatrix.getHeight();
		BufferedImage background = setting.getImage();
		if (background == null)
			return null;
		Integer bWidth = background.getWidth();
		Integer bHeight = background.getHeight();
		if (setting.isAutoResize()) {
			float scale = 1f;
			if (bWidth > bHeight) {
				scale = height.floatValue() / bHeight.floatValue();
			} else {
				scale = width.floatValue() / bWidth.floatValue();
			}
			bHeight = (int) (bHeight * scale) + 1;
			bWidth = (int) (bWidth * scale) + 1;
			BufferedImage scaleBackground = new BufferedImage(bWidth, bHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = scaleBackground.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawImage(background, 0, 0, bWidth, bHeight, null);
			g.dispose();
			background = scaleBackground;
		} else {
			if (width > background.getWidth()) {
				System.err.println("背景图片宽度小于二维码宽度，取消生成彩色二维码");
				return null;
			}
			if (height > background.getHeight()) {
				System.err.println("背景图片高度小于二维码高度，取消生成彩色二维码");
				return null;
			}
		}
		BufferedImage qrCode = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int offsetX = setting.getOffsetX();
		if (offsetX + width > background.getWidth())
			offsetX = background.getWidth() - width;
		int offsetY = setting.getOffsetY();
		if (offsetY + height > background.getHeight())
			offsetY = background.getHeight() - height;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (bitMatrix.get(x, y) == setting.isQrCodeIsImage()) {
					qrCode.setRGB(x, y, background.getRGB(x + offsetX, y + offsetY));
				} else {
					qrCode.setRGB(x, y, setting.getOthersColor().getRGB());
				}
			}
		}
		return qrCode;
	}

	private BufferedImage drawQrCodeWithColor(BitMatrix bitMatrix, ColorQrCodeSetting setting) {
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage qrCode = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (bitMatrix.get(x, y)) {
					qrCode.setRGB(x, y, setting.getQrCodeColor().getRGB());
				} else {
					qrCode.setRGB(x, y, setting.getBackgroundColor().getRGB());
				}
			}
		}
		return qrCode;
	}

	public ColorfulQrCodeFactory setSetting(Setting setting) {
		this.setting = setting;
		return this;
	}

	public ColorfulQrCodeFactory setLogo(BufferedImage logo) {
		this.logo = logo;
		return this;
	}

	public ColorfulQrCodeFactory setLogoDimension(float logoDimension) {
		this.logoDimension = logoDimension;
		return this;
	}

	public ColorfulQrCodeFactory setLogoBackgroundColor(Color logoBackgroundColor) {
		this.logoBackgroundColor = logoBackgroundColor;
		return this;
	}

	public ColorfulQrCodeFactory setLogoBackgroundColorPadding(int logoBackgroundColorPadding) {
		this.logoBackgroundColorPadding = logoBackgroundColorPadding;
		return this;
	}

	/**
	 * image：主图片<br>
	 * othersColor：图片以外的颜色，默认 Color.WHITE<br>
	 * autoResize：自动调整图片大小，默认 true<br>
	 * qrCodeIsImage：设置为 true，二维码黑色部分为图片，反之背景为图片，默认 true<br>
	 * 
	 * @author caicaicai21
	 *
	 */
	public static class PhotoQrCodeSetting extends Setting {
		private BufferedImage image;
		private Color othersColor = Color.WHITE;
		private boolean autoResize = true;
		private boolean qrCodeIsImage = true;

		private int offsetX = 0;
		private int offsetY = 0;

		public PhotoQrCodeSetting(BufferedImage image) {
			this.image = image;
		}

		public BufferedImage getImage() {
			return image;
		}

		public PhotoQrCodeSetting setImage(BufferedImage image) {
			this.image = image;
			return this;
		}

		public Color getOthersColor() {
			return othersColor;
		}

		public PhotoQrCodeSetting setOthersColor(Color othersColor) {
			if (othersColor != null)
				this.othersColor = othersColor;
			return this;
		}

		public boolean isAutoResize() {
			return autoResize;
		}

		public PhotoQrCodeSetting setAutoResize(boolean autoResize) {
			this.autoResize = autoResize;
			return this;
		}

		public boolean isQrCodeIsImage() {
			return qrCodeIsImage;
		}

		public PhotoQrCodeSetting setQrCodeIsImage(boolean qrCodeIsImage) {
			this.qrCodeIsImage = qrCodeIsImage;
			return this;
		}

		public int getOffsetX() {
			return offsetX;
		}

		public PhotoQrCodeSetting setOffsetX(int offsetX) {
			this.offsetX = offsetX;
			return this;
		}

		public int getOffsetY() {
			return offsetY;
		}

		public PhotoQrCodeSetting setOffsetY(int offsetY) {
			this.offsetY = offsetY;
			return this;
		}
	}

	/**
	 * backgroundColor: 背景颜色，默认 Color.WHITE<br>
	 * qrCodeColor：二维码颜色，默认 Color.BLACK
	 * 
	 * @author caicaicai21
	 *
	 */
	public static class ColorQrCodeSetting extends Setting {
		private Color backgroundColor = Color.WHITE;
		private Color qrCodeColor = Color.BLACK;

		public ColorQrCodeSetting() {
		}

		public ColorQrCodeSetting(Color backgroundColor, Color qrCodeColor) {
			this.backgroundColor = backgroundColor;
			this.qrCodeColor = qrCodeColor;
		}

		public Color getBackgroundColor() {
			return backgroundColor;
		}

		public ColorQrCodeSetting setBackgroundColor(Color backgroundColor) {
			if (backgroundColor != null)
				this.backgroundColor = backgroundColor;
			return this;
		}

		public Color getQrCodeColor() {
			return qrCodeColor;
		}

		public ColorQrCodeSetting setQrCodeColor(Color qrCodeColor) {
			if (qrCodeColor != null)
				this.qrCodeColor = qrCodeColor;
			return this;
		}
	}
}

abstract class Setting {

}