import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import picocli.CommandLine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "qrCode", mixinStandardHelpOptions = true, version = "checksum 1.0",
        description = "generate qr code img.")
public class qrcode implements Callable<Integer> {
    @CommandLine.Option(names = {"-c", "--content"}, description = "text content.")
    public String content = "testC测试。。。，，，---";

    @CommandLine.Option(names = {"-l", "--length"}, description = "width or height length.")
    private Integer length = 1000;

    @CommandLine.Option(names = {"-s", "--stride"}, description = "stride of string.")
    private Integer stride = 2000;


    final private QRCodeWriter barcodeWriter = new QRCodeWriter();
    final static private Map<EncodeHintType, Object> hintMap = new EnumMap<>(EncodeHintType.class);

    public static void main(String[] args) {

        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        int exitCode = new CommandLine(new qrcode()).execute(args);

        System.exit(exitCode);
    }

    @Override
    public Integer call() throws WriterException, IOException { // your business logic goes here...
        System.out.print("generate qr code:" + content + " .....\n");


        int i=0,qrOrder=0;
        StringBuilder sb = new StringBuilder();
        while(i<content.length()){
            String substr;
            if(i+stride>=content.length()){
                substr = content.substring(i);
                generateQr(substr,qrOrder);
                i=content.length();
            }else{
                substr = content.substring(i, i + stride);
                generateQr(substr,qrOrder);
                i=i+stride;
            }
            sb.append(substr);
            qrOrder++;
        }
        System.out.print("check generated qrcode result:" + (content+"").equals(sb.toString()) +"\n");
        return 0;
    }

    private void generateQr(String content,int qrOrder) throws WriterException, IOException {

        BitMatrix bitMatrix =
                barcodeWriter.encode(content, BarcodeFormat.QR_CODE, length, length,hintMap);

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        File outputFile = new File("qrcode_"+qrOrder+".jpg");
        ImageIO.write(bufferedImage, "jpg", outputFile);

        System.out.print("generate qr code success!!^-^\n");

    }
}
