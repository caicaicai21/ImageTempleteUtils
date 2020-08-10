package com.caicaicai21.imageTemplete.pojo;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.caicaicai21.imageTemplete.qr.GoogleQrCodeFactory;
import com.caicaicai21.imageTemplete.utils.ImageTempleteUtils.IDrawItem;

public class Word implements IDrawItem {
	public static final String REGEX_EMOJI = "(?:[\uD83C\uDF00-\uD83D\uDDFF]|[\uD83E\uDD00-\uD83E\uDDFF]|[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEFF]|[\u2600-\u26FF]\uFE0F?|[\u2700-\u27BF]\uFE0F?|\u24C2\uFE0F?|[\uD83C\uDDE6-\uD83C\uDDFF]{1,2}|[\uD83C\uDD70\uD83C\uDD71\uD83C\uDD7E\uD83C\uDD7F\uD83C\uDD8E\uD83C\uDD91-\uD83C\uDD9A]\uFE0F?|[\u0023\u002A\u0030-\u0039]\uFE0F?\u20E3|[\u2194-\u2199\u21A9-\u21AA]\uFE0F?|[\u2B05-\u2B07\u2B1B\u2B1C\u2B50\u2B55]\uFE0F?|[\u2934\u2935]\uFE0F?|[\u3030\u303D]\uFE0F?|[\u3297\u3299]\uFE0F?|[\uD83C\uDE01\uD83C\uDE02\uD83C\uDE1A\uD83C\uDE2F\uD83C\uDE32-\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE51]\uFE0F?|[\u203C\u2049]\uFE0F?|[\u25AA\u25AB\u25B6\u25C0\u25FB-\u25FE]\uFE0F?|[\u00A9\u00AE]\uFE0F?|[\u2122\u2139]\uFE0F?|\uD83C\uDC04\uFE0F?|\uD83C\uDCCF\uFE0F?|[\u231A\u231B\u2328\u23CF\u23E9-\u23F3\u23F8-\u23FA]\uFE0F?)";
	public static final Font DEFAULT_FONT = new Font("Default", Font.PLAIN, 20);
	public static final Color DEFAULT_FONT_COLOR = Color.BLACK;

	public static interface IQrCodeFactory {
		public BufferedImage getQrImage(String text, int height, int width);
	}

	private String text = "";
	private Color color = null;
	private Font font = null;
	private int positionX = 0;
	private int positionY = 0;
	private float alpha = 1f;
	private int orderIndex = 0;
	private int singleLineWidth = 0;
	private int maxLineCount = 0;
	private int firstLineBlank = 0;

	private float positionXP = 0f;
	private float positionYP = 0f;
	private float singleLineWidthP = 0f;

	private boolean center = false;

	private boolean toQrImage = false;
	private int qrImageHeight = 20;
	private int qrImageWidth = 20;
	private IQrCodeFactory qrCodeFactory = new GoogleQrCodeFactory();

	private ColorBlock backgroundColorBlock = null;
	private int backgroundPadding = 0;

	public Word() {
	}

	public Word(String text) {
		this.text = text;
	}

	public Word(String text, int positionX, int positionY) {
		this(text, null, null, positionX, positionY);
	}

	public Word(String text, Color color, int positionX, int positionY) {
		this(text, color, null, positionX, positionY);
	}

	public Word(String text, Font font, int positionX, int positionY) {
		this(text, null, font, positionX, positionY);
	}

	public Word(String text, Color color, Font font, int positionX, int positionY) {
		this.text = text;
		this.color = color;
		this.font = font;
		this.positionX = positionX;
		this.positionY = positionY;
	}

	public String getText() {
		if (firstLineBlank > 0) {
			String blankStr = "";
			for (int i = 0; i < firstLineBlank; i++) {
				blankStr += "\\s";
			}
			return blankStr + text;
		}
		return text;
	}

	public Word setText(String text) {
		this.text = text;
		return this;
	}

	public Color getColor() {
		return color;
	}

	public Word setColor(Color color) {
		this.color = color;
		return this;
	}

	public Font getFont() {
		return font;
	}

	public Word setFont(Font font) {
		this.font = font;
		return this;
	}

	public int getPositionX() {
		return positionX;
	}

	public Word setPositionX(int positionX) {
		this.positionX = positionX;
		return this;
	}

	public int getPositionY() {
		return positionY;
	}

	public Word setPositionY(int positionY) {
		this.positionY = positionY;
		return this;
	}

	public int getOrderIndex() {
		return orderIndex;
	}

	public Word setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
		return this;
	}

	public boolean isToQrImage() {
		return toQrImage;
	}

	public Word setToQrImage(int qrImageHeight, int qrImageWidth) {
		this.toQrImage = true;
		this.qrImageHeight = qrImageHeight;
		this.qrImageWidth = qrImageWidth;
		return this;
	}

	public Word setSingleLineWidth(int singleLineWidth) {
		this.singleLineWidth = singleLineWidth;
		return this;
	}

	public int getSingleLineWidth() {
		return singleLineWidth;
	}

	public int getQrImageHeight() {
		return qrImageHeight;
	}

	public int getQrImageWidth() {
		return qrImageWidth;
	}

	public float getAlpha() {
		if (Float.compare(alpha, 1) >= 0)
			return 1;
		if (Float.compare(alpha, 0) <= 0)
			return 0;
		return alpha;
	}

	public Word setAlpha(float alpha) {
		this.alpha = alpha;
		return this;
	}

	public IQrCodeFactory getQrCodeFactory() {
		return qrCodeFactory;
	}

	public Word setQrCodeFactory(IQrCodeFactory qrCodeFactory) {
		this.qrCodeFactory = qrCodeFactory;
		return this;
	}

	public int getMaxLineCount() {
		return maxLineCount;
	}

	public Word setMaxLineCount(int maxLineCount) {
		this.maxLineCount = maxLineCount;
		return this;
	}

	public int getFirstLineBlank() {
		return firstLineBlank;
	}

	public Word setFirstLineBlank(int firstLineBlank) {
		this.firstLineBlank = firstLineBlank;
		return this;
	}

	public float getPositionXP() {
		return positionXP;
	}

	/**
	 * 相对背景图片宽度
	 */
	public Word setPositionXP(float positionXP) {
		this.positionXP = positionXP;
		return this;
	}

	public float getPositionYP() {
		return positionYP;
	}

	/**
	 * 相对背景图片高度
	 */
	public Word setPositionYP(float positionYP) {
		this.positionYP = positionYP;
		return this;
	}

	public float getSingleLineWidthP() {
		return singleLineWidthP;
	}

	/**
	 * 相对背景图片宽度
	 */
	public Word setSingleLineWidthP(float singleLineWidthP) {
		this.singleLineWidthP = singleLineWidthP;
		return this;
	}

	public ColorBlock getBackgroundColorBlock() {
		return backgroundColorBlock;
	}

	public Word setBackgroundColorBlock(ColorBlock backgroundColorBlock) {
		this.backgroundColorBlock = backgroundColorBlock;
		return this;
	}

	public int getBackgroundPadding() {
		return backgroundPadding;
	}

	public Word setBackgroundPadding(int backgroundPadding) {
		this.backgroundPadding = backgroundPadding;
		return this;
	}

	public boolean isCenter() {
		return center;
	}

	public Word setCenter(boolean center) {
		this.center = center;
		return this;
	}

	@Override
	public void drawItem(Graphics2D graphics2d, Dimension backgroundDimension) {
		if (this.getText() == null || this.getText().isEmpty())
			return;

		if (backgroundDimension != null) {
			int backgroundHeight = backgroundDimension.height;
			int backgroundWidth = backgroundDimension.width;
			if (this.getPositionXP() > 0 && this.getPositionX() == 0)
				this.setPositionX((int) (backgroundWidth * this.getPositionXP()));
			if (this.getPositionYP() > 0 && this.getPositionY() == 0)
				this.setPositionY((int) (backgroundHeight * this.getPositionYP()));
			if (this.getSingleLineWidthP() > 0 && this.getSingleLineWidth() == 0)
				this.setSingleLineWidth((int) (backgroundWidth * this.getSingleLineWidthP()));
		}

		drawWord(graphics2d);
	}

	private void drawWord(Graphics2D graphics2d) {
		resetGraphics2d(graphics2d);
		if (this.isToQrImage()) {
			IQrCodeFactory currentQrCodeFactory = this.getQrCodeFactory();
			if (currentQrCodeFactory != null) {
				Image qrImage = currentQrCodeFactory.getQrImage(this.getText(), this.getQrImageHeight(),
						this.getQrImageWidth());
				if (qrImage == null)
					return;
				graphics2d.drawImage(qrImage, this.getPositionX(), this.getPositionY(), qrImage.getWidth(null),
						qrImage.getHeight(null), null);
			} else {
				System.err.println("Qr code factory is NULL !!");
				return;
			}
		} else {
			// 去除 emoji
			this.setText(this.getText().replaceAll(REGEX_EMOJI, ""));

			calculateDrawStringWithAutoWrap(graphics2d);
		}
	}

	private void calculateDrawStringWithAutoWrap(Graphics2D graphics2d) {
		String text = this.getText();
		int singleLineWidth = this.getSingleLineWidth();
		int maxLineCount = this.getMaxLineCount();
		int x = this.getPositionX();
		int y = this.getPositionY();

		FontMetrics metrics = graphics2d.getFontMetrics(graphics2d.getFont());

		// 计算文字偏移量，让绘制定位点在文字左上角
		int stringHeight = metrics.getHeight();
		int stringOffsetY = stringHeight - metrics.getDescent();
		y += stringOffsetY;

		int currentHeight = 0;
		int currentWidth = 0;

		List<DrawText> texts = new LinkedList<DrawText>();
		DrawText drawText = null;

		int wordsWidth = metrics.stringWidth(text);
		if (!text.contains("\n") && (singleLineWidth <= 0 || wordsWidth <= singleLineWidth)) {
			currentHeight = stringHeight;
			currentWidth = wordsWidth;

			drawText = new DrawText(text, x, y);
			texts.add(drawText);
		} else {
			char[] ch = text.toCharArray();
			int nowLine = 0;
			int yOffset = metrics.getHeight();

			StringBuffer sb = new StringBuffer();
			boolean isDone = false;
			int nowWidth = 0;
			for (int i = 0; i < ch.length; i++) {
				sb.append(ch[i]);
				nowWidth = metrics.stringWidth(sb.toString());
				if (ch[i] == '\n' || (singleLineWidth > 0 && nowWidth >= singleLineWidth) || i + 1 == ch.length) {
					if (nowWidth > currentWidth)
						currentWidth = nowWidth;
					if (maxLineCount > 0 && nowLine + 1 == maxLineCount && i + 1 < ch.length) {
						sb.delete(sb.length() - 2, sb.length()).append("...");
						isDone = true;
					}

					drawText = new DrawText(sb.toString(), x, y + (nowLine * yOffset));
					texts.add(drawText);

					if (sb.toString().trim().isEmpty()) {
						y -= yOffset / 2;
						currentHeight += yOffset / 2;
					} else {
						currentHeight += yOffset;
					}

					sb.delete(0, sb.length());
					if (isDone)
						break;
					nowLine++;
				}
			}
		}
		if (this.getBackgroundColorBlock() != null) {
			drawBackground(graphics2d, this.getPositionX(), this.getPositionY(), currentWidth, currentHeight);
			resetGraphics2d(graphics2d);
		}
		drawText(graphics2d, texts, currentWidth, currentHeight);
	}

	private void resetGraphics2d(Graphics2D graphics2d) {
		if (this.isToQrImage()) {
			graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		} else {
			graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
			graphics2d.setPaint(this.getColor() != null ? this.getColor() : DEFAULT_FONT_COLOR);
			graphics2d.setFont(this.getFont() != null ? this.getFont() : DEFAULT_FONT);
		}
		graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.getAlpha()));
	}

	private void drawText(Graphics2D graphics2d, List<DrawText> texts, int currentWidth, int currentHeight) {
		if (texts == null || texts.isEmpty())
			return;
		Iterator<DrawText> it = texts.iterator();
		while (it.hasNext()) {
			DrawText drawText = it.next();
			int x = drawText.getX();
			int y = drawText.getY();
			if (this.isCenter()) {
				x -= currentWidth / 2;
				y -= currentHeight / 2;
			}
			graphics2d.drawString(drawText.getText(), x, y);
		}
	}

	private void drawBackground(Graphics2D graphics2d, int x, int y, int currentWidth, int currentHeight) {
		ColorBlock colorBlock = this.getBackgroundColorBlock();

		x -= this.backgroundPadding;
		y -= this.backgroundPadding;
		if (this.isCenter()) {
			x -= currentWidth / 2;
			y -= currentHeight / 2;
		}
		currentWidth += this.backgroundPadding * 2;
		currentHeight += this.backgroundPadding * 2;

		colorBlock.setPositionX(x);
		colorBlock.setPositionY(y);
		colorBlock.setWidth(currentWidth);
		colorBlock.setHeight(currentHeight);
		colorBlock.setCenter(false);

		colorBlock.drawItem(graphics2d, null);
	}

	@Override
	public int getOrder() {
		return this.orderIndex;
	}
}

class DrawText {
	private int x = 0;
	private int y = 0;
	private String text;

	public DrawText() {
	}

	public DrawText(String text, int x, int y) {
		this.x = x;
		this.y = y;
		this.text = text;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}