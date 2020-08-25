import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import picocli.CommandLine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "qrCode", mixinStandardHelpOptions = true, version = "checksum 1.0",
        description = "generate qr code img.")
public class qrcode implements Callable<Integer> {
    @CommandLine.Option(names = {"-c", "--content"}, description = "text content.")
    private String content = "";

    @CommandLine.Option(names = {"-l", "--length"}, description = "width or height length.")
    private Integer length = 100;


    public static void main(String[] args) {
        int exitCode = new CommandLine(new qrcode()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws WriterException, IOException { // your business logic goes here...
        System.out.print("generate qr code:" + content + " .....\n");
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(content, BarcodeFormat.QR_CODE, length, length);

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        File outputFile = new File("image.jpg");
        ImageIO.write(bufferedImage, "jpg", outputFile);

        System.out.print("generate qr code success!!^-^\n");
        return 0;
    }
}
