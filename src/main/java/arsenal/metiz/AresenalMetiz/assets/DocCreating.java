package arsenal.metiz.AresenalMetiz.assets;

import arsenal.metiz.AresenalMetiz.models.WarehousePosition;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class DocCreating {

    public static final String UPLOAD_FOLDER = "C:\\Users\\Администратор\\Desktop\\docs\\";

    public static String createDoc(List<WarehousePosition> list, long id) {
        System.out.println("Writing document...");
        try {
            // создаем модель docx документа,
            // к которой будем прикручивать наполнение (колонтитулы, текст)
            XWPFDocument docxModel = new XWPFDocument();
            String filename = String.valueOf(id);
            for (WarehousePosition p : list) {
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
}
