package arsenal.metiz.AresenalMetiz.controllers;

import arsenal.metiz.AresenalMetiz.WordParsing;
import arsenal.metiz.AresenalMetiz.assets.Database;
import arsenal.metiz.AresenalMetiz.models.AutoRegisteredCertificate;
import arsenal.metiz.AresenalMetiz.models.DBCertificate;
import arsenal.metiz.AresenalMetiz.models.Mark;
import arsenal.metiz.AresenalMetiz.models.ParsedCertificate;
import arsenal.metiz.AresenalMetiz.repo.AutoRegisteredCertificatesRepository;
import arsenal.metiz.AresenalMetiz.repo.MarksRepository;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
public class WordParsingController {

    public static final String UPLOAD_FOLDER = "C:\\Users\\Администратор\\Desktop\\certificates\\";
    public static final String REGISTERED = "C:\\Users\\Администратор\\Desktop\\registered\\";
    public static final File ROOT_DIR = new File(UPLOAD_FOLDER);
    private static final String UPLOADED__FOLDER = "C:\\Users\\Администратор\\Desktop\\files\\";
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    MarksRepository marksRepository;
    @Autowired
    AutoRegisteredCertificatesRepository autoRegisteredCertificatesRepository;

//    @GetMapping("/parse/{file}")
//    public String parse(@PathVariable("file") String file, Model model) {
//        File fileW = new File("C:\\Users\\Администратор\\Desktop\\files\\" + file);
//        ParsedCertificate ps = new ParsedCertificate();
//        HashMap<String, String> h1 = WordParsing.parseTable(fileW);
//        HashMap<String, String> h2 = WordParsing.parseParagraph(fileW);
//        ps.setId(h2.get("id"));
//        ps.setCustomer(h2.get("customer"));
//        ps.setMark(h1.get("mark"));
//        ps.setPart(h1.get("part"));
//        ps.setPlav(h1.get("plav"));
//        ps.setWeight(h1.get("weight"));
//        ps.setDiameter(h1.get("diameter"));
//        model.addAttribute("out", ps);
//        model.addAttribute("img", WordParsing.parseImages(fileW));
//        return "output";
//    }

    @GetMapping("/admin/upload/check")
    public String checkUpload() {
        return "checkAfterUpload";
    }

    @PostMapping("/admin/upload/check/save")
    public String saveCertificate(Model model, @RequestParam String qr, @RequestParam String id, @RequestParam
            String mark, @RequestParam String part, @RequestParam String plav,
                                  @RequestParam String diameter, @RequestParam String customer,
                                  @RequestParam String weight) throws IOException {

        Date date = new Date();
        DBCertificate certificate = new DBCertificate(id, mark, part, plav, diameter, weight, customer,
                "WEB", dateFormat.format(date), timeFormat.format(date));
        Database db = Database.getInstance();
        marksRepository.save(new Mark(mark));
        db.Upload(certificate, qr);
        return "redirect:/admin/upload";
    }

    @GetMapping("/admin/upload")
    public String upload(Model model) {
        return "upload";
    }

    @GetMapping("/admin/upload/random")
    public String uploadRandom(Model model, RedirectAttributes redirectAttributes) throws Exception {
        File[] files = ROOT_DIR.listFiles();
        int c = 0;
        while (c < files.length) {
            File f = files[c];
            if (!f.getName().contains(".docx")) {
                Document doc = new Document(f.getAbsolutePath());
                doc.save(UPLOAD_FOLDER + f.getName() + "x", SaveFormat.DOCX);
                File file = new File(UPLOAD_FOLDER + f.getName() + "x");
                boolean valid = WordParsing.parse(file);
                if (valid) {
                    ParsedCertificate ps = WordParsing.parseFile(f);
                    Optional<Mark> iterable = marksRepository.findById(ps.getMark());
                    if (iterable.isEmpty()) {
                        model.addAttribute("ps", ps);
                        redirectAttributes.addFlashAttribute("ps", ps);
                        f.renameTo(new File(REGISTERED + f.getName()));
                        return "redirect:/admin/upload/check";
                    } else {
                        Database.getInstance().Upload(ps.getDBCertificate(), ps.getQr());
                        System.out.println("Сертификат зарегестрирован автоматически!!");
                        autoRegisteredCertificatesRepository.save(new AutoRegisteredCertificate(ps.getId()));
                        f.renameTo(new File(REGISTERED + f.getName()));
                    }
                }
            } else {
                boolean valid = WordParsing.parse(f);
                if (valid) {
                    ParsedCertificate ps = WordParsing.parseFile(f);
                    Optional<Mark> iterable = marksRepository.findById(ps.getMark());
                    if (iterable.isEmpty()) {
                        model.addAttribute("ps", ps);
                        redirectAttributes.addFlashAttribute("ps", ps);
                        f.renameTo(new File(REGISTERED + f.getName()));
                        return "redirect:/admin/upload/check";
                    } else {
                        Database.getInstance().Upload(ps.getDBCertificate(), ps.getQr());
                        System.out.println("Сертификат зарегестрирован автоматически!!");
                        autoRegisteredCertificatesRepository.save(new AutoRegisteredCertificate(ps.getId()));
                        f.renameTo(new File(REGISTERED + f.getName()));
                    }
                }
            }
            c++;

        }
        return "redirect:/admin/upload";
    }

    @PostMapping("/admin/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes, Model model) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED__FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            Document doc = new Document(UPLOADED__FOLDER + file.getOriginalFilename());
            doc.save(UPLOADED__FOLDER + file.getOriginalFilename() + "x", SaveFormat.DOCX);
            File fileW = new File(UPLOADED__FOLDER + file.getOriginalFilename() + "x");
            boolean valid = WordParsing.parse(fileW);
            if (valid) {
                ParsedCertificate ps = WordParsing.parseFile(fileW);
                Optional<Mark> iterable = marksRepository.findById(ps.getMark());
                if (iterable.isEmpty()) {
                    model.addAttribute("ps", ps);
                    redirectAttributes.addFlashAttribute("ps", ps);
                    fileW.renameTo(new File(REGISTERED + fileW.getName()));
                    return "redirect:/admin/upload/check";
                } else {
                    Database.getInstance().Upload(ps.getDBCertificate(), ps.getQr());
                    System.out.println("Сертификат зарегестрирован автоматически!!");
                    autoRegisteredCertificatesRepository.save(new AutoRegisteredCertificate(ps.getId()));
                    fileW.renameTo(new File(REGISTERED + file.getName()));
                    return "redirect:/admin/upload";
                }
            }
            ParsedCertificate ps = WordParsing.parseFile(fileW);
            model.addAttribute("ps", ps);
            redirectAttributes.addFlashAttribute("ps", ps);
            File fileD = new File(UPLOADED__FOLDER + file.getOriginalFilename());
            fileD.delete();
            fileW.delete();
            return "redirect:/admin/upload/check";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }
}
