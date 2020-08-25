package arsenal.metiz.AresenalMetiz;

import arsenal.metiz.AresenalMetiz.models.ParsedCertificate;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class WordParsing {

    public static final String BAD_CODE = "C:\\Users\\Администратор\\Desktop\\bad_code\\";
    public static final String TO_EDIT = "C:\\Users\\Администратор\\Desktop\\toEdit\\";

    public static HashMap<String, String> parseParagraph(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));

            List<XWPFParagraph> paragraphList = xdoc.getParagraphs();
            HashMap<String, String> result = new HashMap<>();
            for (XWPFParagraph paragraph : paragraphList) {
                String text = paragraph.getText();
                if (text.contains("Грузополучатель:")) {
                    result.put("customer", text.replace("Грузополучатель:", "").trim());
                } else if (text.contains("Сертификат качества №")) {
                    String textC = text.replace("Сертификат качества №", "").trim();
                    String textCl = textC.substring(0, textC.indexOf("от") - 1).trim();
                    result.put("id", textCl);
                }
            }
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, String> parseTable(File file) {

        try {
            HashMap<String, String> parser = new HashMap<>();
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
            Iterator<IBodyElement> bodyElementIterator = xdoc.getBodyElementsIterator();
            while (bodyElementIterator.hasNext()) {
                IBodyElement element = bodyElementIterator.next();

                if ("TABLE".equalsIgnoreCase(element.getElementType().name())) {
                    List<XWPFTable> tableList = element.getBody().getTables();
                    XWPFTable table = tableList.get(0);
                    String mark = table.getRow(1).getCell(1).getText()
                            .replace("Проволока", "")
                            .replace(table.getRow(1).getCell(2).getText(), "")
                            .replace(" ", "");
                    parser.put("mark", mark.contains("ГОСТ") ? mark.substring(0, mark.indexOf("ГОСТ")) :
                            mark.substring(0, mark.indexOf("ТУ")));
                    parser.put("diameter", table.getRow(1).getCell(2).getText());
                    parser.put("plav", table.getRow(1).getCell(3).getText());
                    parser.put("part", table.getRow(1).getCell(4).getText());
                    parser.put("weight", table.getRow(1).getCell(6).getText());
                    parser.put("amount", String.valueOf(table.getNumberOfRows() - 1));

                }
            }
            return parser;
        } catch (Exception ignored) {

        }
        return null;
    }

    public static HashMap<String, String> parseDoubleTable(File file) {

        try {
            HashMap<String, String> parser = new HashMap<>();
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
            Iterator<IBodyElement> bodyElementIterator = xdoc.getBodyElementsIterator();
            while (bodyElementIterator.hasNext()) {
                IBodyElement element = bodyElementIterator.next();

                if ("TABLE".equalsIgnoreCase(element.getElementType().name())) {
                    List<XWPFTable> tableList = element.getBody().getTables();
                    XWPFTable table = tableList.get(0);
                    String mark = table.getRow(1).getCell(1).getText()
                            .replace("Проволока", "")
                            .replace(table.getRow(1).getCell(2).getText(), "")
                            .replace(" ", "");
                    parser.put("mark", mark.contains("ГОСТ") ? mark.substring(0, mark.indexOf("ГОСТ")) :
                            mark.substring(0, mark.indexOf("ТУ")));
                    parser.put("diameter", table.getRow(1).getCell(2).getText() + ";"
                            + table.getRow(2).getCell(2).getText());
                    parser.put("plav", table.getRow(1).getCell(3).getText() + ";"
                            + table.getRow(2).getCell(3).getText());
                    parser.put("part", table.getRow(1).getCell(4).getText() + ";"
                            + table.getRow(2).getCell(4).getText());
                    parser.put("weight", table.getRow(1).getCell(6).getText() + ";"
                            + table.getRow(2).getCell(5).getText());
                    parser.put("amount", String.valueOf(table.getNumberOfRows() - 1));

                }
            }
            return parser;
        } catch (Exception ignored) {

        }
        return null;
    }

    public static HashMap<String, String> parseTripleTable(File file) {

        try {
            HashMap<String, String> parser = new HashMap<>();
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
            Iterator<IBodyElement> bodyElementIterator = xdoc.getBodyElementsIterator();
            while (bodyElementIterator.hasNext()) {
                IBodyElement element = bodyElementIterator.next();

                if ("TABLE".equalsIgnoreCase(element.getElementType().name())) {
                    List<XWPFTable> tableList = element.getBody().getTables();
                    XWPFTable table = tableList.get(0);
                    String mark = table.getRow(1).getCell(1).getText()
                            .replace("Проволока", "")
                            .replace(table.getRow(1).getCell(2).getText(), "")
                            .replace(" ", "");
                    parser.put("mark", mark.contains("ГОСТ") ? mark.substring(0, mark.indexOf("ГОСТ")) :
                            mark.substring(0, mark.indexOf("ТУ")));
                    parser.put("diameter", table.getRow(1).getCell(2).getText() + ";"
                            + table.getRow(2).getCell(2).getText() + ";"
                            + table.getRow(3).getCell(2).getText());
                    parser.put("plav", table.getRow(1).getCell(3).getText() + ";"
                            + table.getRow(2).getCell(3).getText() + ";"
                            + table.getRow(3).getCell(3).getText());
                    parser.put("part", table.getRow(1).getCell(4).getText() + ";"
                            + table.getRow(2).getCell(4).getText() + ";"
                            + table.getRow(3).getCell(4).getText());
                    parser.put("weight", table.getRow(1).getCell(6).getText() + ";"
                            + table.getRow(2).getCell(5).getText() + ";"
                            + table.getRow(3).getCell(5).getText());
                    parser.put("amount", String.valueOf(table.getNumberOfRows() - 1));

                }
            }
            return parser;
        } catch (Exception ignored) {

        }
        return null;
    }

    public static String parseImages(File file, int from) {
        try {
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
            List<XWPFPictureData> pic = xdoc.getAllPictures();
            String out = "";
            if (!pic.isEmpty()) {
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(pic.get(from).getData()));
                out = QRCode.readQR(img);
            }
            return out;
        } catch (Exception ex) {
            return "";
        }
    }

    public static ParsedCertificate parseFile(File file) {
        ParsedCertificate ps = new ParsedCertificate();
        HashMap<String, String> h1 = WordParsing.parseTable(file);
        HashMap<String, String> h2 =
                WordParsing.parseParagraph(file);
        if (h1 == null || h2 == null) {
            return null;
        }
        int rows = Integer.parseInt(h1.get("amount"));
        h1 = switch (rows) {
            case 1 -> parseTable(file);
            case 2 -> parseDoubleTable(file);
            case 3 -> parseTripleTable(file);
            case 4, 5, 6, 7, 8, 9, 10 -> parseNTable(file, rows);
            default -> null;
        };
        ps.setId(h2.get("id").trim());
        try {
            ps.setCustomer(h2.get("customer").trim());
        } catch (Exception e) {
            ps.setCustomer("");
        }
        ps.setMark(h1.get("mark").trim());
        ps.setPart(h1.get("part").trim());
        ps.setPlav(h1.get("plav").trim());
        ps.setWeight(h1.get("weight").trim());
        ps.setDiameter(h1.get("diameter").trim());
        for (int c = 0; c <= 2; c++) {
            if (parseImages(file, c) != null && !parseImages(file, c).equals("")) {
                ps.setQr(parseImages(file, c));
                return ps;
            }
        }
        ps.setQr("not found");
        return ps;
    }

    public static Boolean parse(File args) {
        ParsedCertificate ps = WordParsing.parseFile(args);
        if (ps == null) {
            System.out.println("Сертификат некорректен");
            moveTo(args, TO_EDIT);
            return false;
        }
        if (ps.getQr().equals("not found")) {
            System.out.println("Код не распознан");
            moveTo(args, BAD_CODE);
            return false;
        }
        return true;
    }

    public static void moveTo(File file, String dest) {
        file.renameTo(new File(dest + file.getName()));
    }

    public static HashMap<String, String> parseNTable(File file, int rows) {

        try {
            HashMap<String, String> parser = new HashMap<>();
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
            Iterator<IBodyElement> bodyElementIterator = xdoc.getBodyElementsIterator();
            while (bodyElementIterator.hasNext()) {
                IBodyElement element = bodyElementIterator.next();
                String mark = "";
                StringBuilder diameter = new StringBuilder();
                StringBuilder plav = new StringBuilder();
                StringBuilder part = new StringBuilder();
                StringBuilder weight = new StringBuilder();

                if ("TABLE".equalsIgnoreCase(element.getElementType().name())) {
                    List<XWPFTable> tableList = element.getBody().getTables();
                    XWPFTable table = tableList.get(0);
                    mark = table.getRow(1).getCell(1).getText()
                            .replace("Проволока", "")
                            .replace(table.getRow(1).getCell(2).getText(), "")
                            .replace(" ", "");
                    for (int r = 1; r <= rows; r++) {
                        diameter.append(table.getRow(r).getCell(2).getText());
                        plav.append(table.getRow(r).getCell(3).getText());
                        part.append(table.getRow(r).getCell(4).getText());
                        weight.append(table.getRow(r).getCell(6).getText());
                        if (rows > 1 && r != rows) {
                            diameter.append(";");
                            plav.append(";");
                            part.append(";");
                            weight.append(";");
                        }
                    }

                    parser.put("mark", mark.contains("ГОСТ") ? mark.substring(0, mark.indexOf("ГОСТ")) :
                            mark.substring(0, mark.indexOf("ТУ")));
                    parser.put("diameter", diameter.toString());
                    parser.put("plav", plav.toString());
                    parser.put("part", part.toString());
                    parser.put("weight", weight.toString());
                    parser.put("amount", String.valueOf(table.getNumberOfRows() - 1));

                }
            }
            return parser;
        } catch (Exception ignored) {

        }
        return null;
    }
}
