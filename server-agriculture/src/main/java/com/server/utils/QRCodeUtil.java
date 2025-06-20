package com.server.utils;

/**
 * @Author: zbb
 * @Date: 2025/6/25 16:34
 */


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class QRCodeUtil {

    /**
     * 生成QRCode并保存为图片文件
     *
     * @param text     需要转换为二维码的字符串
     * @throws IOException 生成文件时的异常
     * @throws WriterException 编码时的异常
     */
    public static String generateQRCode(String text) throws Exception {
        int width = 200;
        int height = 200;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF); // 黑/白
            }
        }

        // 将图像转换为Base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(bytes);

        return "data:image/png;base64," + base64Image; // 返回前端可直接使用的Base64图像
    }
//
//    public static void main(String[] args) {
//        try {
//            // 示例调用
//            //text 扫描二维码要显示的内容
//            String text = "Hello, World!";
//            String filePath = "QRCode.png";  // 输出文件名
//            generateQRCode(text, filePath, 200);  // 调用方法生成200x200大小的二维码
//        } catch (WriterException | IOException e) {
//            System.err.println("Error generating QRCode: " + e.getMessage());
//        }
//    }
}