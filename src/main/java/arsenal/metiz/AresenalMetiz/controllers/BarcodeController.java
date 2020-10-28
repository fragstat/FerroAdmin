package arsenal.metiz.AresenalMetiz.controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Controller
public class BarcodeController {

    public static BufferedImage generateEAN13BarcodeImage(String barcodeText) throws Exception {
        EAN13Writer barcodeWriter = new EAN13Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.EAN_13, 350, 100);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    @GetMapping(value = "/api/code/{id}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody
    byte[] getBarcode(Model model, @PathVariable String id) throws Exception {
        String code = "000000";
        var codes = code.toCharArray();
        System.out.println(codes);
        if (id.length() < 6) {
            for (int i = 0; i <= id.length() - 1; i++) {
                codes[codes.length - 1 - i] = id.charAt(id.length() - 1 - i);
            }
        }
        char[] ean13Code = {'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        System.out.println(codes);
        int sum = 0;
        for (char c : codes) {
            sum += (c - '0');
        }
        String i = String.valueOf(10 - ((sum * 3) % 10));
        System.out.println(ean13Code);
        ean13Code[12] = i.charAt(i.length() - 1);
        ean13Code[1] = codes[0];
        ean13Code[3] = codes[1];
        ean13Code[5] = codes[2];
        ean13Code[7] = codes[3];
        ean13Code[9] = codes[4];
        ean13Code[11] = codes[5];

        String s = new String(ean13Code);
        System.out.println(s);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(generateEAN13BarcodeImage(s), "png", baos);
        return baos.toByteArray();
    }
}
