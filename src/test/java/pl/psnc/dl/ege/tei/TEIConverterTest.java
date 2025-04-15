package pl.psnc.dl.ege.tei;

import pl.psnc.dl.ege.configuration.EGEConfigurationManager;
import pl.psnc.dl.ege.exception.ConverterException;
import pl.psnc.dl.ege.types.ConversionActionArguments;
import pl.psnc.dl.ege.types.DataType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class TEIConverterTest {
    private TEIConverter converter;

    @org.junit.Before
    public void setUp() throws Exception {
        converter = new TEIConverter();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        converter = null;
    }

    @org.junit.Test
    public void convert() throws IOException, ConverterException {
        InputStream is = new FileInputStream("src/test/resources/test-input.xml.zip");
        OutputStream os = new FileOutputStream("src/test/resources/test-output.txt.zip");
        InputStream is2 = new FileInputStream("src/test/resources/test-input.odd.zip");
        OutputStream os2 = new FileOutputStream("src/test/resources/test-output.xml.zip");
        DataType inputType = new DataType("TEI","text/xml");
        DataType outputType = new DataType("txt","text/plain");
        ConversionActionArguments conversionActionArguments = new ConversionActionArguments(inputType, outputType, null);
        String tempDir = "src/test/temp";
        converter.convert(is, os, conversionActionArguments, tempDir);
        assertNotNull(new File("src/test/resources/test-output.txt.zip"));
        InputStream isout = new FileInputStream("src/test/resources/test-output.txt.zip");
        EGEConfigurationManager.getInstance().getStandardIOResolver().decompressStream(isout, new File("src/test/resources/test-output.txt"));
        assertEquals("Text files differ",
                new String(Files.readAllBytes(Paths.get("src/test/resources/expected-output.txt"))),
                new String(Files.readAllBytes(Paths.get("src/test/resources/test-output.txt/document.txt"))));
        inputType = new DataType("ODDC","text/xml");
        outputType = new DataType("TEI","text/xml");
        conversionActionArguments = new ConversionActionArguments(inputType, outputType, null);
        converter.convert(is2, os2, conversionActionArguments, tempDir);
        assertNotNull(new File("src/test/resources/test-output.xml.zip"));
        InputStream isout2 = new FileInputStream("src/test/resources/test-output.xml.zip");
        EGEConfigurationManager.getInstance().getStandardIOResolver().decompressStream(isout2, new File("src/test/resources/test-output.xml"));
        assertEquals("Text files differ",
                new String(Files.readAllBytes(Paths.get("src/test/resources/expected-output.xml"))),
                new String(Files.readAllBytes(Paths.get("src/test/resources/test-output.xml/document.xml"))));
        is.close();
        os.close();
        is2.close();
        os2.close();
        isout.close();
        isout2.close();
    }

    @org.junit.Test
    public void getPossibleConversions() {
        assertNotNull(converter.getPossibleConversions());
        System.out.println(converter.getPossibleConversions());
    }
}