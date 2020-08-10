package com.caicaicai21.imageTemplete.pojo;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;

import com.caicaicai21.imageTemplete.utils.ImageTempleteUtils.IDrawItem;

public class ColorBlock implements IDrawItem {
	public static final Color DEFAULT_COLOR_BLOCK = Color.WHITE;
	public static final Stroke DEFAULT_STROKE = new BasicStroke(5F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

	public interface DrawType {
		/**
		 * 绘制
		 */
		public static final int DRAW = 0;

		/**
		 * 填充
		 */
		public static final int FILL = 1;
	}

	public interface GraphType {
		/**
		 * 矩形
		 */
		public static final int TYPE_RECT = 0;

		/**
		 * 圆弧/扇形
		 */
		public static final int TYPE_ARC = 1;

		/**
		 * 圆/椭圆
		 */
		public static final int TYPE_OVAL = 2;

		/**
		 * 圆角矩形
		 */
		public static final int TYPE_ROUND_RECT = 3;
	}

	private Color color;
	private Color floorColor;
	private int height = -1;
	private int width = -1;
	private int positionX = 0;
	private int positionY = 0;
	private float alpha = 1f;
	private int orderIndex = 0;

	private float positionXP = 0f;
	private float positionYP = 0f;

	private boolean center = false;

	private int graphType = GraphType.TYPE_RECT;
	private int drawType = DrawType.FILL;

	private Stroke stroke = null;

	private int roundRectArcWidth = 0;
	private int roundRectArcHeight = 0;

	private int startAngle = 0;
	private int arcAngle = 180;

	public ColorBlock() {
	}

	public ColorBlock(Color color) {
		this.color = color;
	}

	public ColorBlock(Color color, int height, int width) {
		this.color = color;
		this.height = height;
		this.width = width;
	}

	public ColorBlock(Color color, int height, int width, int positionX, int positionY, float alpha) {
		this.color = color;
		this.height = height;
		this.width = width;
		this.positionX = positionX;
		this.positionY = positionY;
		this.alpha = alpha;
	}

	public Color getColor() {
		return color;
	}

	public ColorBlock setColor(Color color) {
		this.color = color;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public ColorBlock setHeight(int height) {
		this.height = height;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public ColorBlock setWidth(int width) {
		this.width = width;
		return this;
	}

	public int getPositionX() {
		return positionX;
	}

	public ColorBlock setPositionX(int positionX) {
		this.positionX = positionX;
		return this;
	}

	public int getPositionY() {
		return positionY;
	}

	public ColorBlock setPositionY(int positionY) {
		this.positionY = positionY;
		return this;
	}

	public float getAlpha() {
		return alpha;
	}

	public ColorBlock setAlpha(float alpha) {
		this.alpha = alpha;
		return this;
	}

	public int getOrderIndex() {
		return orderIndex;
	}

	public ColorBlock setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
		return this;
	}

	public int getDrawType() {
		return drawType;
	}

	public ColorBlock setDrawType(int drawType) {
		this.drawType = drawType;
		return this;
	}

	public int getGraphType() {
		return graphType;
	}

	public ColorBlock setGraphType(int graphType) {
		this.graphType = graphType;
		return this;
	}

	public int getStartAngle() {
		return startAngle;
	}

	/**
	 * GraphType 为 TYPE_ARC 时有效<br>
	 * 扇形开始绘制度数
	 * 
	 * @param startAngle
	 */
	public ColorBlock setStartAngle(int startAngle) {
		this.startAngle = startAngle;
		return this;
	}

	public int getArcAngle() {
		return arcAngle;
	}

	/**
	 * GraphType 为 TYPE_ARC 时有效<br>
	 * 扇形绘制度数<br>
	 * *长宽一致的情况下，180为半圆，360为圆形
	 * 
	 * @param arcAngle
	 */
	public ColorBlock setArcAngle(int arcAngle) {
		this.arcAngle = arcAngle;
		return this;
	}

	public Stroke getStroke() {
		return stroke;
	}

	public ColorBlock setStroke(Stroke stroke) {
		this.stroke = stroke;
		return this;
	}

	public int getRoundRectArcWidth() {
		return roundRectArcWidth;
	}

	/**
	 * GraphType 为 TYPE_ROUND_RECT 时有效<br>
	 * 圆角宽度
	 * 
	 * @param arcAngle
	 */
	public ColorBlock setRoundRectArcWidth(int roundRectArcWidth) {
		this.roundRectArcWidth = roundRectArcWidth;
		return this;
	}

	public int getRoundRectArcHeight() {
		return roundRectArcHeight;
	}

	/**
	 * GraphType 为 TYPE_ROUND_RECT 时有效<br>
	 * 圆角高度
	 * 
	 * @param arcAngle
	 */
	public ColorBlock setRoundRectArcHeight(int roundRectArcHeight) {
		this.roundRectArcHeight = roundRectArcHeight;
		return this;
	}

	/**
	 * GraphType 为 TYPE_ROUND_RECT 时有效<br>
	 * 圆角高/宽度
	 * 
	 * @param arcAngle
	 */
	public ColorBlock setRoundRectArc(int roundRectArc) {
		this.setRoundRectArcHeight(roundRectArc);
		this.setRoundRectArcWidth(roundRectArc);
		return this;
	}

	public Color getFloorColor() {
		return floorColor;
	}

	/**
	 * DrawType 为 DRAW 时有效<br>
	 * 底层颜色
	 * 
	 * @param floorColor
	 * @return
	 */
	public ColorBlock setFloorColor(Color floorColor) {
		this.floorColor = floorColor;
		return this;
	}

	public float getPositionXP() {
		return positionXP;
	}

	/**
	 * 相对背景图片宽度的百分比
	 */
	public ColorBlock setPositionXP(float positionXP) {
		this.positionXP = positionXP;
		return this;
	}

	public float getPositionYP() {
		return positionYP;
	}

	/**
	 * 相对背景图片高度的百分比
	 */
	public ColorBlock setPositionYP(float positionYP) {
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
	public ColorBlock setCenter(boolean center) {
		this.center = center;
		return this;
	}

	@Override
	public void drawItem(Graphics2D graphics2d, Dimension backgroundDimension) {
		if (this.getHeight() <= 0 || this.getWidth() <= 0)
			return;

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

		drawColorBlock(graphics2d);
	}

	private void drawColorBlock(Graphics2D graphics2d) {
		if (this.getStroke() != null)
			graphics2d.setStroke(this.getStroke());
		else
			graphics2d.setStroke(DEFAULT_STROKE);

		Color drawColor = this.getColor() != null ? this.getColor() : DEFAULT_COLOR_BLOCK;
		Color floorColor = this.getFloorColor();

		graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.getAlpha()));
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);

		int offset = 1;

		boolean isTypeDraw = DrawType.DRAW == this.getDrawType();

		switch (this.getGraphType()) {
		case GraphType.TYPE_ARC:
			if (floorColor != null && isTypeDraw) {
				graphics2d.setPaint(floorColor);
				graphics2d.fillArc(this.getPositionX(), this.getPositionY(), this.getWidth(), this.getHeight(),
						this.getStartAngle(), this.getArcAngle());
			}
			graphics2d.setPaint(drawColor);
			if (isTypeDraw)
				graphics2d.drawArc(this.getPositionX(), this.getPositionY(), this.getWidth() - offset,
						this.getHeight() - offset, this.getStartAngle(), this.getArcAngle());
			else
				graphics2d.fillArc(this.getPositionX(), this.getPositionY(), this.getWidth() - offset,
						this.getHeight() - offset, this.getStartAngle(), this.getArcAngle());
			break;
		case GraphType.TYPE_OVAL:
			if (floorColor != null && isTypeDraw) {
				graphics2d.setPaint(floorColor);
				graphics2d.fillOval(this.getPositionX(), this.getPositionY(), this.getWidth(), this.getHeight());
			}
			graphics2d.setPaint(drawColor);
			if (isTypeDraw)
				graphics2d.drawOval(this.getPositionX(), this.getPositionY(), this.getWidth() - offset,
						this.getHeight() - offset);
			else
				graphics2d.fillOval(this.getPositionX(), this.getPositionY(), this.getWidth() - offset,
						this.getHeight() - offset);
			break;
		case GraphType.TYPE_ROUND_RECT:
			if (floorColor != null && isTypeDraw) {
				graphics2d.setPaint(floorColor);
				graphics2d.fillRoundRect(this.getPositionX(), this.getPositionY(), this.getWidth(), this.getHeight(),
						this.getRoundRectArcWidth(), this.getRoundRectArcHeight());
			}
			graphics2d.setPaint(drawColor);
			if (isTypeDraw)
				graphics2d.drawRoundRect(this.getPositionX(), this.getPositionY(), this.getWidth() - offset,
						this.getHeight() - offset, this.getRoundRectArcWidth(), this.getRoundRectArcHeight());
			else
				graphics2d.fillRoundRect(this.getPositionX(), this.getPositionY(), this.getWidth() - offset,
						this.getHeight() - offset, this.getRoundRectArcWidth(), this.getRoundRectArcHeight());
			break;
		default:
		case GraphType.TYPE_RECT:
			if (floorColor != null && isTypeDraw) {
				graphics2d.setPaint(floorColor);
				graphics2d.fillRect(this.getPositionX(), this.getPositionY(), this.getWidth(), this.getHeight());
			}
			graphics2d.setPaint(drawColor);
			if (isTypeDraw)
				graphics2d.drawRect(this.getPositionX(), this.getPositionY(), this.getWidth() - offset,
						this.getHeight() - offset);
			else
				graphics2d.fillRect(this.getPositionX(), this.getPositionY(), this.getWidth() - offset,
						this.getHeight() - offset);
			break;
		}
	}

	@Override
	public int getOrder() {
		return this.orderIndex;
	}
}
