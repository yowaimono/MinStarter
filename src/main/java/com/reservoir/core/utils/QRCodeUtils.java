package com.reservoir.core.utils;//package com.reservoir.core.utils;
//
//import com.google.zxing.*;
//import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
//import com.google.zxing.client.j2se.MatrixToImageWriter;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.common.HybridBinarizer;
//import com.google.zxing.qrcode.QRCodeWriter;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.FileSystems;
//import java.nio.file.Path;
//import java.util.Base64;
//import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.Map;
//
//public class QRCodeUtils {
//
//    private static final String QR_CODE_IMAGE_FORMAT = "PNG";
//
//    /**
//     * 生成二维码图片并保存到指定路径
//     *
//     * @param text      要编码的文本
//     * @param width     图片宽度
//     * @param height    图片高度
//     * @param filePath  保存路径
//     * @throws WriterException 如果生成二维码失败
//     * @throws IOException     如果保存图片失败
//     */
//    public static void generateQRCodeImage(String text, int width, int height, String filePath)
//            throws WriterException, IOException {
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
//        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
//
//        Path path = FileSystems.getDefault().getPath(filePath);
//        MatrixToImageWriter.writeToPath(bitMatrix, QR_CODE_IMAGE_FORMAT, path);
//    }
//
//    /**
//     * 生成二维码图片并返回字节数组
//     *
//     * @param text   要编码的文本
//     * @param width  图片宽度
//     * @param height 图片高度
//     * @return 二维码图片的字节数组
//     * @throws WriterException 如果生成二维码失败
//     * @throws IOException     如果生成图片失败
//     */
//    public static byte[] generateQRCodeImage(String text, int width, int height)
//            throws WriterException, IOException {
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
//        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        MatrixToImageWriter.writeToStream(bitMatrix, QR_CODE_IMAGE_FORMAT, byteArrayOutputStream);
//        return byteArrayOutputStream.toByteArray();
//    }
//
//    /**
//     * 生成二维码图片并返回字节数组
//     *
//     * @param text   要编码的文本
//     * @return 二维码图片的字节数组
//     * @throws WriterException 如果生成二维码失败
//     * @throws IOException     如果生成图片失败
//     */
//    public static byte[] generateQRCodeImage(String text)
//            throws WriterException, IOException {
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//
//        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
//        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE,200,200,hints);
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        MatrixToImageWriter.writeToStream(bitMatrix, QR_CODE_IMAGE_FORMAT, byteArrayOutputStream);
//        return byteArrayOutputStream.toByteArray();
//    }
//
//    /**
//     * 解析二维码图片
//     *
//     * @param filePath 二维码图片路径
//     * @return 解析出的文本
//     * @throws IOException           如果读取图片失败
//     * @throws NotFoundException     如果解析二维码失败
//     */
//    public static String decodeQRCode(String filePath) throws IOException, NotFoundException {
//        BufferedImage bufferedImage = ImageIO.read(new File(filePath));
//        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
//        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
//
//        Map<DecodeHintType, Object> hints = new HashMap<>();
//        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
//
//        Result result = new MultiFormatReader().decode(bitmap, hints);
//        return result.getText();
//    }
//
//    /**
//     * 解析二维码图片字节数组
//     *
//     * @param imageBytes 二维码图片字节数组
//     * @return 解析出的文本
//     * @throws IOException           如果读取图片失败
//     * @throws NotFoundException     如果解析二维码失败
//     */
//    public static String decodeQRCode(byte[] imageBytes) throws IOException, NotFoundException {
//        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
//        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
//        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
//
//        Map<DecodeHintType, Object> hints = new HashMap<>();
//        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
//
//        Result result = new MultiFormatReader().decode(bitmap, hints);
//        return result.getText();
//    }
//}