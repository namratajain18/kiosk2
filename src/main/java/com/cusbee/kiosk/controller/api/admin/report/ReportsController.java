package com.cusbee.kiosk.controller.api.admin.report;

import com.cusbee.kiosk.entity.Orders;
import com.cusbee.kiosk.repository.OrdersRepository;
import com.cusbee.kiosk.repository.PaymentsRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by ahorbat on 11.06.17.
 */
@RestController
@RequestMapping(value = "/api/admin/report")
public class ReportsController {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private PaymentsRepository paymentsRepository;

    private static String[] columns = {"Location","Name", "Email", "Order date", "Order Price", "Order Tax"};

    @RequestMapping(value = "/monthly", method = RequestMethod.GET)
    public String monthly() throws IOException {

        List<Orders> orders = ordersRepository.findByOrderDateBetween(
                DateTime.now().withTimeAtStartOfDay().minusMonths(1).toDate(),
                DateTime.now().withTimeAtStartOfDay().toDate()
        );

        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("VVOS");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);

        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        int rowNum = 1;
        for(Orders order: orders) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue("SFO Market Street - Buckhorn Grill");

            row.createCell(1)
                    .setCellValue(order.getName());

            row.createCell(2)
                    .setCellValue(order.getEmail());

            Cell dateOfBirthCell = row.createCell(3);
            dateOfBirthCell.setCellValue(order.getOrderDate());
            dateOfBirthCell.setCellStyle(dateCellStyle);

            row.createCell(4)
                    .setCellValue(String.valueOf(order.getOrderPrice()));

            row.createCell(5)
                    .setCellValue(String.valueOf(order.getOrderPriceTax()));


        }

        // Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("/var/www/html/report.xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();


        return "Success";
    }

    @RequestMapping(value = "/general", method = RequestMethod.GET)
    public String report() {

      /*  try {
            //Path path = Paths.get("/Users/ahorbat/Projects/cusbee/kiosk-server/src/main/resources/food1.png");
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("/var/www/html/report.pdf"));
            document.open();
            //Image img = Image.getInstance(path.toAbsolutePath().toString());
            //document.add(img);
            PdfPTable table = new PdfPTable(3);

            addTableHeader(table);
            addRows(table);
            //addCustomRows(table);

            document.add(table);
            document.close();

        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        FileSystemResource pdf = new FileSystemResource("/var/www/html/report.pdf");
        return pdf.getPath();*/
        return "";
    }


    private void addTableHeader(PdfPTable table) {
        Stream.of("#", "Name", "Amount")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.CYAN);
                    header.setBorderWidth(0);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table) {

        List<Orders> orders = ordersRepository.findByOrderDateBetween(
            DateTime.now().withTimeAtStartOfDay().minusMonths(1).toDate(),
            DateTime.now().withTimeAtStartOfDay().toDate()
        );

        BigDecimal tax = orders.stream()
            .map(Orders::getOrderPriceTax)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal net = orders.stream()
            .map(Orders::getOrderPrice)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        table.addCell("1");
        table.addCell("Gross Sales");
        table.addCell(String.format("$%s", tax.add(net)));
        table.addCell("2");
        table.addCell("Net Sales");
        table.addCell(String.format("$%s", net.toPlainString()));
        table.addCell("3");
        table.addCell("Tax");
        table.addCell(String.format("$%s", tax.toPlainString()));
        table.addCell("4");
        table.addCell("Order Count");
        table.addCell(String.format("%d", orders.size()));

    }

    /*private void addCustomRows(PdfPTable table)
            throws URISyntaxException, BadElementException, IOException {
        Path path = Paths.get("/Users/ahorbat/Projects/cusbee/kiosk-server/src/main/resources/food1.png");
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        img.scalePercent(10);

        PdfPCell imageCell = new PdfPCell(img);
        table.addCell(imageCell);

        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);

        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(verticalAlignCell);
    }*/
}
