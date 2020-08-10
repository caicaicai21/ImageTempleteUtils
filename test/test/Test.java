package test;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

import com.caicaicai21.imageTemplete.pojo.ColorBlock;
import com.caicaicai21.imageTemplete.pojo.ColorBlock.DrawType;
import com.caicaicai21.imageTemplete.pojo.ColorBlock.GraphType;
import com.caicaicai21.imageTemplete.pojo.ImageTemplete;
import com.caicaicai21.imageTemplete.pojo.Picture;
import com.caicaicai21.imageTemplete.pojo.Word;
import com.caicaicai21.imageTemplete.utils.ImageTempleteUtils;
import com.caicaicai21.imageTemplete.utils.ImageTempleteUtils.IDrawItem;

public class Test {

	public static void main(String[] args) throws Exception {
		ImageTemplete templete = new ImageTemplete();
		templete.setBackgroundColor(Color.DARK_GRAY);
		templete.setImageWidth(500);
		templete.setImageHeight(300);

		List<IDrawItem> drawItems = new LinkedList<IDrawItem>();
		ColorBlock textColorBlock = new ColorBlock();
		textColorBlock.setColor(Color.YELLOW);
		textColorBlock.setFloorColor(Color.LIGHT_GRAY);
		textColorBlock.setGraphType(GraphType.TYPE_ROUND_RECT);
		textColorBlock.setRoundRectArc(10);
		textColorBlock.setDrawType(DrawType.DRAW);

		drawItems.add(new Word("测试1\n \n测试").setColor(Color.YELLOW).setPositionX(0).setPositionY(0)
				.setBackgroundColorBlock(textColorBlock).setBackgroundPadding(20).setCenter(true).setPositionXP(.5f)
				.setPositionYP(.5f));
		drawItems.add(new Word("测试2").setColor(Color.GREEN).setFont(new Font("微软雅黑", Font.BOLD, 30)).setPositionX(130)
				.setPositionY(200));
		drawItems.add(new Word("测试3").setColor(Color.BLUE).setFont(new Font("微软雅黑", Font.ITALIC, 20)).setPositionX(120)
				.setPositionY(170).setAlpha(0.7f));
		drawItems.add(new Word("你好啊，哈哈哈😊").setPositionX(300).setPositionY(100).setToQrImage(100, 100).setAlpha(0.7f));

		drawItems.add(new Picture("C:\\Users\\caicaicai21\\Desktop\\1.png", 100, 100, 100, 100).setAlpha(0.9f));

		templete.setDrawItems(drawItems);
		ImageTempleteUtils.createImageAndSave(templete, "C:\\Users\\caica\\Desktop\\test.png", true);
	}
}
