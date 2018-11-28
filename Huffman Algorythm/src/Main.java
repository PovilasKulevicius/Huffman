import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) throws IOException {


        InputStream file = new FileInputStream("src/input.bat");
        byte[] bytes = new byte[file.available()];  //reads how much bytes are readable from file
        System.out.println(file.available());
        file.read(bytes);//reads the file and save all read bytes into b
        for (byte b : bytes) {
            //Gal reiktu pameginti atskirai baitus issisaugoti i koki masyva
            System.out.println(Integer.toBinaryString(b & 255 | 256).substring(1));

        }


    }
}
