import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Testing {
    public static void main(String[] args) throws IOException {
        String[] compress = new String[3];
        String[] decompress = new String[2];
        compress[1] = "pav.jpg";
        for(int k = 2; k < 25; ++k){
            compress[0] = Integer.toString(k);
            compress[2] = "compressed" + Integer.toString(k);
            HuffmanCompress.main(compress);
            decompress[0] = "compressed" + Integer.toString(k);
            decompress[1] = "decompressed" + Integer.toString(k) + ".jpg";
            HuffmanDecompress.main(decompress);
        }
    }
}