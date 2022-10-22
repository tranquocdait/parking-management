package com.huyendieu.parking.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class QRCodeUtils {
    public static String generateQRCodeImage(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);

            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    public static String generateQRCodeImage(String text, int width) {
        try {
            return generateQRCodeImage(text, width, width);
        } catch (Exception e) {
            return null;
        }
    }

    public static String generateQRCodeImage(String text) {
        try {
            return generateQRCodeImage(text, 480);
        } catch (Exception e) {
            return null;
        }
    }

}
