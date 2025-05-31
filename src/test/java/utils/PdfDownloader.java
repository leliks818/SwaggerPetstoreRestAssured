package utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PdfDownloader {

    @Step("Отправить GET-запрос на скачивание PDF по URL: {url}")
    public static Response downloadPdf(String url) {
        return given()
                .when()
                .get(url)
                .then()
                .contentType("application/pdf")
                .statusCode(200)
                .extract()
                .response();
    }

    @Step("Сохранить PDF в файл: {fileName}")
    public static void savePdf(Response response, String fileName) throws IOException {
        byte[] pdfBytes = response.asByteArray();
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(pdfBytes);
        }
    }

    @Step("Проверить, что файл существует: {fileName}")
    public static void verifyFileExists(String fileName) {
        File file = new File(fileName);
        assertThat("Файл должен существовать", file.exists(), is(true));
    }
}
