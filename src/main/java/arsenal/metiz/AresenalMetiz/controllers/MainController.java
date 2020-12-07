package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.assets.Database;
import arsenal.metiz.AresenalMetiz.repo.ArchivedRequestsRepo;
import arsenal.metiz.AresenalMetiz.repo.OrdersRepository;
import arsenal.metiz.AresenalMetiz.repo.RequestRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

@Controller
public class MainController {

    private final RequestRepository requestRepository;
    private final ArchivedRequestsRepo archivedRequestsRepo;
    private final OrdersRepository ordersRepository;

    @Autowired
    public MainController(RequestRepository requestRepository, ArchivedRequestsRepo archivedRequestsRepo,
                          OrdersRepository ordersRepository) {
        this.requestRepository = requestRepository;
        this.archivedRequestsRepo = archivedRequestsRepo;
        this.ordersRepository = ordersRepository;
    }

    @GetMapping("/admin")
    public String admin(Authentication authentication, Model model) {
        String user = authentication.getName();
        String greeting;
        String name;
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (user.equals("natalia")) {
            hour += 2;
            name = "Наталья";
        }
        if (hour >= 6 && hour < 12) {
            greeting = "Доброе утро, ";
        } else if (hour >= 12 && hour < 18) {
            greeting = "Добрый день, ";
        } else if (hour >= 18 && hour <= 22) {
            greeting = "Добрый вечер, ";
        } else {
            greeting = "Доброй ночи, ";
        }
        name = switch (user) {
            case "sergey" -> "Сергей";
            case "ksenia" -> "Ксения";
            case "dir" -> "Евгений";
            case "danil" -> "Данил";
            case "ekat" -> "Екатерина";
            default -> "";
        };
        model.addAttribute("greeting", greeting + name);
        return "admin";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login2";
    }

    @GetMapping("/")
    public String ferroMain(Model model){
        return "index";
    }

    @GetMapping("/products")
    public String ferroProd(Model model){
        return "products";
    }

    @PostMapping("/c")
    public String ferroCode(@RequestParam String code, Model model){
        if(code == null){
            return "index";
        }
        Database database = Database.getInstance();
        try {
            Map<String,Object> map = database.getDocumentAsMap(code);

            if (map != null && map.get("id").equals("Неверный код")) {
                model.addAttribute("id", "Неверный код");
                model.addAttribute("mark", "-");
                model.addAttribute("part", "-");
                model.addAttribute("plav","-");
                model.addAttribute("weight", "-");
                model.addAttribute("diameter", "-");
                model.addAttribute("customer", "-");
                model.addAttribute("src", "https://pk-izhsintez.ru/upload/iblock/ba6/ba6495b4efc9e3d9ae6d7fd2a36b4874.png");
            }else {
                assert map != null;
                model.addAttribute("id", map.get("id"));
                model.addAttribute("mark", map.get("mark"));
                model.addAttribute("part", map.get("part"));
                model.addAttribute("plav",map.get("plav"));
                model.addAttribute("weight", map.get("weight"));
                model.addAttribute("diameter", map.get("diameter"));
                model.addAttribute("customer", map.get("customer"));
                model.addAttribute("src", "https://i.yapx.ru/GU705t.png");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "index";
        }

        return "result";
    }

    public static BitMatrix generateQRBarcodeImage(String barcodeText, int size) throws Exception {
        QRCodeWriter code128Writer = new QRCodeWriter();
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        return code128Writer.encode(barcodeText, BarcodeFormat.QR_CODE, 200,
                200, hintMap);
    }

    @SneakyThrows
    @GetMapping(value = "/admin/qr",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody
    byte[] downloadQr(Model model) throws IOException {

        ByteArrayOutputStream baos = null;
        for (int j = 0; j < 20000; j++) {
            String uri = "C:\\qr\\";
            StringBuilder qr = new StringBuilder();
            char[] al = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();
            for (int i = 0; i <= 16; i++) {
                qr.append(al[(int) (Math.random() * al.length)]);
            }

            BufferedImage imageBuff = MatrixToImageWriter.toBufferedImage(generateQRBarcodeImage(qr.toString(), 200));
            imageBuff = BarcodeController.process(imageBuff, qr.toString());
            baos = new ByteArrayOutputStream();
            ImageIO.write(imageBuff, "png", baos);

            FileOutputStream fos = new FileOutputStream(new File(uri + qr.toString() + ".png"));
            fos.write(baos.toByteArray());
            fos.flush();
        }
        return baos.toByteArray();
    }

}
