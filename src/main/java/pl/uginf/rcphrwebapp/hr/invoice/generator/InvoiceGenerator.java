package pl.uginf.rcphrwebapp.hr.invoice.generator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.uginf.rcphrwebapp.config.CompanyInfoRecord;
import pl.uginf.rcphrwebapp.exceptions.InvoiceGenerationException;
import pl.uginf.rcphrwebapp.hr.invoice.InvoiceFile;
import pl.uginf.rcphrwebapp.hr.invoice.counter.InvoiceCounterService;
import pl.uginf.rcphrwebapp.hr.invoice.model.InvoiceInfoRecord;

@Component
@Slf4j
@AllArgsConstructor(onConstructor_ = @Autowired)
public class InvoiceGenerator {

    private final static BigDecimal TAX = new BigDecimal("0.23");

    private final CompanyInfoRecord companyInfo;

    private final InvoiceCounterService invoiceService;

    @Transactional
    public InvoiceFile generateInvoice(InvoiceData invoiceData, YearMonth month) {
        Document document = new Document(PageSize.A4, 36, 20, 36, 20);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        List<ServiceInfo> serviceInfoList = invoiceData.getServiceInfoList();
        Long counter = invoiceService.getCounter();

        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            document.add(createSeller(invoiceData.getInvoiceInfoRecord(), month));
            document.add(createHeader(counter.toString(), companyInfo.currency()));
            document.add(createData(serviceInfoList));
            document.add(createSum(serviceInfoList));
        } catch (Exception e) {
            logErrorAndThrowGenerationException(e);
        } finally {
            document.close();
        }

        return new InvoiceFile("invoice" + counter + ".pdf", "pdf", byteArrayOutputStream.toByteArray());
    }

    private PdfPTable createHeader(String invoiceNumber, String currency) {
        BaseFont baseFront = null;
        try {
            baseFront = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
        } catch (DocumentException | IOException e) {
            logErrorAndThrowGenerationException(e);
        }
        Font f = new Font(baseFront, 9, Font.NORMAL);

        PdfPTable table1 = new PdfPTable(7);
        table1.setWidthPercentage(100f);
        PdfPCell header = new PdfPCell(new Phrase("Invoice " + invoiceNumber));
        header.setPadding(5);
        header.setColspan(9);
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        table1.addCell(header);
        table1.getDefaultCell()
                .setBackgroundColor(BaseColor.LIGHT_GRAY);
        table1.getDefaultCell()
                .setPaddingBottom(3);
        table1.getDefaultCell()
                .setPaddingTop(2);
        table1.getDefaultCell()
                .setPaddingLeft(3);
        table1.getDefaultCell()
                .setPaddingRight(3);
        table1.getDefaultCell()
                .setBorderWidth(1);

        String currencyInBracket = "[" + currency + "]";
        String hourRateCurrency = "[" + currency + "/h]";

        Phrase lp = new Phrase("Ordinal", f);
        Phrase serviceName = new Phrase("Service name", f);
        Phrase hours = new Phrase("Hours", f);
        Phrase hourRate = new Phrase("Hour rate " + hourRateCurrency, f);
        Phrase netVal = new Phrase("Net " + currencyInBracket, f);
        Phrase tax = new Phrase("Tax " + currencyInBracket, f);
        Phrase grossVal = new Phrase("Gross " + currencyInBracket, f);

        table1.addCell(lp);
        table1.addCell(serviceName);
        table1.addCell(hours);
        table1.addCell(hourRate);
        table1.addCell(netVal);
        table1.addCell(tax);
        table1.addCell(grossVal);

        return table1;
    }

    private PdfPTable createData(List<ServiceInfo> serviceInfos) {
        PdfPTable table = new PdfPTable(7);
        table.getDefaultCell()
                .setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.getDefaultCell()
                .setPaddingRight(4);
        table.setWidthPercentage(100f);
        for (int i = 0; i < serviceInfos.size(); i++) {
            ServiceInfo serviceInfo = serviceInfos.get(i);
            table.addCell(String.valueOf(i + 1));
            table.addCell(serviceInfo.getName() + " service");
            BigDecimal quantity = serviceInfo.getQuantity();
            BigDecimal hourRate = serviceInfo.getHourRate();
            table.addCell(quantity.divide(hourRate, 2, RoundingMode.HALF_UP)
                    .toString());
            table.addCell(hourRate.toString());
            table.addCell(getNetValue(quantity).toString());
            table.addCell(getTaxValue(quantity).toString());
            table.addCell(quantity.toString());
        }
        return table;
    }

    private BigDecimal getNetValue(BigDecimal quantity) {
        return quantity.multiply(BigDecimal.valueOf(1)
                .subtract(TAX));
    }

    private BigDecimal getTaxValue(BigDecimal quantity) {
        return quantity.multiply(TAX);
    }

    private PdfPTable createSum(List<ServiceInfo> serviceInfos) {
        PdfPTable table = new PdfPTable(4);
        table.getDefaultCell()
                .setBorderWidth(1);
        table.getDefaultCell()
                .setPadding(3);
        table.getDefaultCell()
                .setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.setWidthPercentage(57);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);

        BigDecimal netSum = new BigDecimal(0);
        BigDecimal grossSum = new BigDecimal(0);

        for (ServiceInfo serviceInfo : serviceInfos) {
            BigDecimal quantity = serviceInfo.getQuantity();
            netSum = netSum.add(getNetValue(quantity));
            grossSum = grossSum.add(quantity);
        }
        netSum = netSum.setScale(2, RoundingMode.HALF_UP);
        BigDecimal taxSum = grossSum.subtract(netSum);

        table.addCell(new Phrase("Sum:"));
        table.addCell(new Phrase(netSum.toString()));
        table.addCell(new Phrase(taxSum.toString()));

        PdfPCell Suma = new PdfPCell(new Phrase(grossSum.toString()));
        Suma.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Suma.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(Suma);

        return table;
    }

    private PdfPTable createSeller(InvoiceInfoRecord invoiceInfoRecord, YearMonth month) {
        BaseFont bf = null;
        try {
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
        } catch (DocumentException | IOException e) {
            logErrorAndThrowGenerationException(e);
        }
        Font f1 = new Font(bf, 9, Font.NORMAL);
        Font f2 = new Font(bf, 10, Font.BOLD);
        Font f3 = new Font(bf, 10, Font.NORMAL);

        Phrase sellerDesc = new Phrase("`Seller`:\n", f1);
        Phrase seller = new Phrase(invoiceInfoRecord.toString(true), f2);
        sellerDesc.add(seller);

        Phrase buyerDesc = new Phrase("`Buyer`:\n", f1);
        Phrase buyer = new Phrase(companyInfo.toString(), f2);
        buyerDesc.add(buyer);

        LocalDate firstDateOfNextMonth = month.plusMonths(1)
                .atDay(1);
        Phrase info = new Phrase("Date of Issue: " + firstDateOfNextMonth + ", " + invoiceInfoRecord.addressDto()
                .getCity() + "\n\n Date of payment: 14 days " + firstDateOfNextMonth.plusDays(14), f3);

        PdfPTable table = new PdfPTable(2);
        table.getDefaultCell()
                .setBorder(0);
        table.getDefaultCell()
                .setPadding(3);

        table.setWidthPercentage(100f);

        PdfPCell cellSeller = new PdfPCell(sellerDesc);
        cellSeller.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellSeller.setBorder(Rectangle.NO_BORDER);
        cellSeller.setPaddingBottom(10);

        PdfPCell cell_info = new PdfPCell(info);
        cell_info.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell_info.setBorder(Rectangle.NO_BORDER);
        cell_info.setPaddingBottom(10);

        PdfPCell cellBuyer = new PdfPCell();
        cellBuyer.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellBuyer.setPaddingBottom(40);
        cellBuyer.setBorder(Rectangle.NO_BORDER);
        cellBuyer.setPhrase(buyerDesc);

        PdfPCell empty = new PdfPCell();
        empty.setHorizontalAlignment(Element.ALIGN_RIGHT);
        empty.setPaddingBottom(40);
        empty.setBorder(Rectangle.NO_BORDER);

        table.addCell(cellSeller);
        table.addCell(cell_info);
        table.addCell(cellBuyer);
        table.addCell(empty);
        return table;

    }

    private void logErrorAndThrowGenerationException(Exception e) {
        log.error(e.getMessage());
        throw new InvoiceGenerationException("Unknown error occurred while invoice generation, please contact administrator.");
    }
}
