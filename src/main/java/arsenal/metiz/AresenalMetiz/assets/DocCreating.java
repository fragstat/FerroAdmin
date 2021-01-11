package arsenal.metiz.AresenalMetiz.assets;

import arsenal.metiz.AresenalMetiz.models.Position;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

public class DocCreating {

    public static final String UPLOAD_FOLDER = "C:\\Users\\Администратор\\Desktop\\docs\\";

    public static String createDoc(List<Position> list, long id) {
        System.out.println("Writing document...");
        try {
            // создаем модель docx документа,
            // к которой будем прикручивать наполнение (колонтитулы, текст)
            XWPFDocument docxModel = new XWPFDocument();
            String filename = String.valueOf(id);
            for (Position p : list) {
                XWPFParagraph bodyParagraph = docxModel.createParagraph();
                bodyParagraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun paragraphConfig = bodyParagraph.createRun();
                paragraphConfig.setItalic(true);
                paragraphConfig.setFontSize(12);
                paragraphConfig.setColor("06357a");
                paragraphConfig.setText(
                        "Марка: " + p.getMark() + "\n" +
                                "Диаметр: " + p.getDiameter() + "\n" +
                                "Упаковка: " + p.getPacking() + "\n" +
                                "Партия/плавка: " + p.getPart() + "/" + p.getPlav() + "\n" +
                                "Масса: " + p.getMass() + "\n" + "\n"

                );
            }

            OutputStream out = new FileOutputStream(new File(UPLOAD_FOLDER + filename + ".docx"));
            docxModel.write(out);
            out.flush();

            return filename;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeTransferDocument(ManufactureTransferView view, Long id, Double weight, Set<TableView> tv) throws Exception {
        File file = new File("C:\\Users\\Администратор\\Desktop\\Отгрузка.docx");
        FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
        XWPFDocument xwpfDocument = new XWPFDocument(fileInputStream);
        List<XWPFParagraph> paragraphs = xwpfDocument.getParagraphs();
        XWPFParagraph p1 = paragraphs.get(2);
        p1.getRuns().get(p1.getRuns().size() - 1).setText(" " + id);
        XWPFParagraph p2 = paragraphs.get(3);
        p2.getRuns().get(p2.getRuns().size() - 1).setText(" " + view.destination);
        XWPFParagraph p3 = paragraphs.get(4);
        p3.getRuns().get(p3.getRuns().size() - 1).setText(" " + view.carPlate);
        XWPFParagraph p4 = paragraphs.get(5);
        p4.getRuns().get(p4.getRuns().size() - 1).setText(" " + weight);
        XWPFTable table = xwpfDocument.getTables().get(0);
        for (TableView tableView : tv) {
            XWPFTableRow xwpfTableRow = table.createRow();
            List<XWPFTableCell> cellsList = xwpfTableRow.getTableCells();
            cellsList.get(0).setText(tableView.getMark());
            cellsList.get(1).setText(String.valueOf(tableView.getDiameter()));
            cellsList.get(2).setText(tableView.getPacking());
            cellsList.get(3).setText(String.valueOf(tableView.getMass()));
        }
        //XWPFParagraph paragraph = xwpfDocument.createParagraph();
        //paragraph.setAlignment(ParagraphAlignment.CENTER);
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //MatrixToImageWriter.writeToStream(MainController.generateQRBarcodeImage(String.valueOf(id), 1000), "png",
        // baos);
        //InputStream stream = new ByteArrayInputStream(baos.toByteArray());
        //XWPFRun r = paragraph.createRun();
        //r.addPicture(stream, XWPFDocument.PICTURE_TYPE_PNG, "test.png", 1500000, 1500000);

        xwpfDocument.write(new FileOutputStream(new File("C:\\Users\\Администратор\\Desktop\\transfer\\" + id
                + ".docx")));
    }
}
