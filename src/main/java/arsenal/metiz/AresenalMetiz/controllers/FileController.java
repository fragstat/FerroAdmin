package arsenal.metiz.AresenalMetiz.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class FileController {

    public static final String UPLOAD_FOLDER = "C:\\Users\\Администратор\\Desktop\\docs\\";
    public static final String TRANSFER_FOLDER = "C:\\Users\\Администратор\\Desktop\\transfer\\";

    @RequestMapping(value = "/api/files/{file_name}", method = RequestMethod.GET)
    public void getFile(
            @PathVariable("file_name") String fileName,
            HttpServletResponse response) {
        try {
            // get your file as InputStream
            InputStream is = new FileInputStream(new File(UPLOAD_FOLDER + fileName + ".docx"));
            // copy it to response's OutputStream
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }

    }

    @RequestMapping(value = "/api/transferDocs/{file_name}", method = RequestMethod.GET)
    public void getTransferFile(
            @PathVariable("file_name") String fileName,
            HttpServletResponse response) {
        try {
            // get your file as InputStream
            InputStream is = new FileInputStream(new File(TRANSFER_FOLDER + fileName + ".docx"));
            // copy it to response's OutputStream
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }

    }
}
