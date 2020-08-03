/**********************************************************************
 * class represents a file on the computer.
 * has methods to replace text from .doc file, and to create a
 * Excel file from a list.
 * and has methods to send file to user to download or by email.
 **********************************************************************/

package my_types;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class MyFile {

    private String fileName;

    public MyFile(String fileName) {
        this.fileName = fileName;
    }

    /* creates new .docx file from .docx template, where students info is inserted in
    place of placeholders of template file. placeholders are between '#' symbols.
     */
    public void replaceTextInDocxFile(Student student, String newFileName) {
        try {
            //opens .docx template file
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            InputStream inputFile = externalContext.getResourceAsStream("/resources/templates/"
                    + this.fileName);
            XWPFDocument doc = new XWPFDocument(inputFile);

            //path to new file
            String realOutputPath = externalContext.getRealPath("/resources/templates/tmp/");
            realOutputPath += "\\" + newFileName;
            this.fileName = realOutputPath;

            //copying .docx file and replacing placeholders with students info            
            try (FileOutputStream fileOut = new FileOutputStream(new File(realOutputPath))) {
                for (XWPFParagraph p : doc.getParagraphs()) {
                    List<XWPFRun> runs = p.getRuns();
                    if (runs != null) {
                        for (XWPFRun r : runs) {
                            String text = r.getText(0);
                            System.out.println(text);
                            if (text != null && text.contains("#")) {
                                if (text.contains("#first#")) {
                                    r.setText(student.getFirstName(), 0);
                                } else if (text.contains("#last#")) {
                                    r.setText(student.getLastName(), 0);
                                } else if (text.contains("#id#")) {
                                    r.setText(student.getId(), 0);
                                } else if (text.contains("#address#")) {
                                    r.setText(student.getAddress().toString(), 0);
                                } else if (text.contains("#dateSterted#")) {
                                    LocalDate dateSterted = student.getDateOfStart();
                                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                    r.setText(dtf.format(dateSterted), 0);
                                } else if (text.contains("#currentDate#")) {
                                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                    LocalDateTime now = LocalDateTime.now();
                                    r.setText(dtf.format(now), 0);
                                } else if (text.contains("#currentYear#")) {
                                    LocalDateTime now = LocalDateTime.now();
                                    r.setText(String.valueOf(now.getYear()), 0);
                                }
                            }
                        }
                    }
                }
                //create file
                doc.write(fileOut);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MyFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MyFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendEmail(String sendToAddress) throws MessagingException {
        Email email = new Email();
        email.sendEmail(fileName, sendToAddress);

    }

    //sends file to client
    public void download(String typeOfDocument) throws IOException {
        File file = new File(this.fileName);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response
                = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.reset();
        response.setHeader("Content-Type", " application/" + typeOfDocument);
        response.setHeader("Content-Disposition", "attachment;filename= " + file.getName());
        try (OutputStream responseOutputStream = response.getOutputStream();
                InputStream fileInputStream = new FileInputStream(file)) {
            byte[] bytesBuffer = new byte[2048];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(bytesBuffer)) > 0) {
                responseOutputStream.write(bytesBuffer, 0, bytesRead);
            }
            responseOutputStream.flush();
        }
        facesContext.responseComplete();
    }

    // creates excel file from data in ArrayList
    public File createExcelFile(ArrayList<List<String>> listOfData, String sheetName) {
        File file = new File(this.fileName);
        // Blank workbook 
        XSSFWorkbook workbook = new XSSFWorkbook();
        // blank sheet 
        XSSFSheet sheet = workbook.createSheet(sheetName);
        // Iterate over data and writes to sheet 
        int rowNum = 0;
        for (List<String> rowData : listOfData) {
            // create new row in sheet 
            Row row = sheet.createRow(rowNum++);
            int cellnum = 0;
            for (String cellData : rowData) {
                // create new cell in row 
                Cell cell = row.createCell(cellnum++);
                cell.setCellValue(cellData);
            }
        }
        try (FileOutputStream out = new FileOutputStream(file)) {
            workbook.write(out);
        } catch (IOException ex) {
            Logger.getLogger(MyFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return file;
    }
}
