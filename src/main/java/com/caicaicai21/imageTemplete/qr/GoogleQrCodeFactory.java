package com.caicaicai21.imageTemplete.qr;

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

public class GoogleQrCodeFactory implements IQrCodeFactory {

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
			return MatrixToImageWriter.toBufferedImage(bitMatrix);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}
}
