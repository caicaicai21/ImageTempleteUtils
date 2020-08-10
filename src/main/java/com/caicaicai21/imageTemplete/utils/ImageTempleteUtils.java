package com.caicaicai21.imageTemplete.utils;

import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

public class ImageTempleteUtils {
	public static final String DEFAULT_IMAGE_ENCODE = "PNG";
	public static final String DEFAULT_BASE64_IMAGE_DATA = "data:image/png;base64,";

	public static interface IDrawBackground {
		public BufferedImage drawBackground();

		public List<IDrawItem> getDrawItemList();
	}

	public static interface IDrawItem {
		public int getOrder();

		public void drawItem(Graphics2D graphics2d, Dimension backgroundDimension);
	}

	public static void createImageAndSave(IDrawBackground background, String savePath) throws Exception {
		createImageAndSave(background, savePath, false);
	}

	public static void createImageAndSave(IDrawBackground background, String savePath, boolean forceReplace)
			throws Exception {
		saveToFile(createImage(background), new File(savePath), forceReplace);
	}

	public static String createBase64Image(IDrawBackground background) throws Exception {
		return toBase64Image(createImage(background));
	}

	public static BufferedImage createImage(IDrawBackground background) throws Exception {
		if (background == null)
			throw new NullPointerException("IDrawBackground is null.");
		BufferedImage baseImage = background.drawBackground();
		if (baseImage == null)
			throw new NullPointerException("Create background fail, background is null.");
		Graphics2D graphics2d = baseImage.createGraphics();

		List<IDrawItem> drawItems = background.getDrawItemList();
		if (drawItems != null && !drawItems.isEmpty()) {
			Dimension backgroundDimension = new Dimension(baseImage.getWidth(), baseImage.getHeight());
			Composite defaultCom = graphics2d.getComposite();
			Shape defaultShape = graphics2d.getClip();

			if (drawItems.size() > 1) {
				Collections.sort(drawItems, new Comparator<IDrawItem>() {
					@Override
					public int compare(IDrawItem o1, IDrawItem o2) {
						return Integer.compare(o1.getOrder(), o2.getOrder());
					}
				});
			}

			Iterator<IDrawItem> it = drawItems.iterator();
			while (it.hasNext()) {
				IDrawItem iDrawItem = it.next();
				iDrawItem.drawItem(graphics2d, backgroundDimension);

				resetGraphics(graphics2d);
				graphics2d.setComposite(defaultCom);
				graphics2d.setClip(defaultShape);
			}
		}

		graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics2d.dispose();
		return baseImage;
	}

	private static void resetGraphics(Graphics2D graphics2d) {
		if (graphics2d == null)
			return;
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		graphics2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	}

	public static BufferedImage readImageFromFile(File file) {
		try {
			return ImageIO.read(file);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void saveToFile(BufferedImage image, File destFile) throws Exception {
		saveToFile(image, destFile, false);
	}

	public static void saveToFile(BufferedImage image, File destFile, boolean forceReplace) throws Exception {
		if (image == null || destFile == null)
			return;
		if (destFile.exists()) {
			if (!forceReplace)
				throw new Exception("file exists!");
			if (destFile.isDirectory())
				throw new Exception("target file path is a directory!");
		}
		ImageIO.write(image, getExtName(destFile.getName()), destFile);
	}

	private static String getExtName(String fileName) {
		int index = fileName.lastIndexOf('.');
		if (index != -1 && (index + 1) < fileName.length()) {
			return fileName.substring(index + 1);
		} else {
			return DEFAULT_IMAGE_ENCODE;
		}
	}

	public static String toBase64Image(BufferedImage image) {
		if (image == null)
			return null;
		String base64Img = Base64.getEncoder().encodeToString(toByteArray(image));
		base64Img = base64Img.replaceAll("\n", "").replaceAll("\r", "");
		return DEFAULT_BASE64_IMAGE_DATA + base64Img;
	}

	public static byte[] toByteArray(BufferedImage image) {
		if (image == null)
			return null;
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(image, DEFAULT_IMAGE_ENCODE, outputStream);
			return outputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BufferedImage toBufferedImage(byte[] byteArray) {
		if (byteArray == null)
			return null;
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
			return ImageIO.read(inputStream);
		} catch (Exception e) {
			e.getSuppressed();
		}
		return null;
	}

	public static int getImageFileHeight(File file) {
		Dimension dimension = getImageFileDimension(file);
		if (dimension == null)
			return 0;
		return dimension.height;
	}

	public static int getImageFileWidth(File file) {
		Dimension dimension = getImageFileDimension(file);
		if (dimension == null)
			return 0;
		return dimension.width;
	}

	public static Dimension getImageFileDimension(File file) {
		if (file == null || !file.exists() || !file.canRead())
			return null;
		Dimension dimension = null;
		Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(getExtName(file.getName()));
		while (it.hasNext()) {
			ImageReader reader = it.next();
			ImageInputStream stream = null;
			try {
				stream = new FileImageInputStream(file);
				reader.setInput(stream);
				int width = reader.getWidth(reader.getMinIndex());
				int height = reader.getHeight(reader.getMinIndex());
				dimension = new Dimension(width, height);
				break;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				reader.dispose();
			}
		}
		return dimension;
	}
}
