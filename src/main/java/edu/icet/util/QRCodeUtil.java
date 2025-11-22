package edu.icet.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QRCodeUtil {

    /**
     * Generates a QR code image from a given text.
     *
     * @param text  The text to encode in the QR code.
     * @param width The width of the desired image.
     * @param height The height of the desired image.
     * @return A JavaFX Image object of the QR code.
     * @throws WriterException
     * @throws IOException
     */
    public static Image generateQRCode(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        // Write the BitMatrix to a byte array as a PNG
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        // Convert the byte array to a JavaFX Image
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pngOutputStream.toByteArray());
        return new Image(inputStream);
    }
}