package apiITest;

import utils.PdfDownloader;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class DownloadTest {

    @Test
    public void testDownloadCommerceBankPdf() throws IOException {
        String endpoint = "https://www.commercebank.com/-/media/cb/pdf/personal/bank/statement_sample1.pdf";
        String fileName = "commerce_bank_statement.pdf";

        Response response = PdfDownloader.downloadPdf(endpoint);
        PdfDownloader.savePdf(response, fileName);
        PdfDownloader.verifyFileExists(fileName);
    }
}