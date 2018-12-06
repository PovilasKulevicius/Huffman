import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
// Handle command line arguments
        if (args.length != 2) {
            System.err.println("Usage: java HuffmanCompress InputFile OutputFile");
            System.exit(1);
            return;
        }
        File inputFile  = new File(args[0]);
        File outputFile = new File(args[1]);

        // Read input file once to compute symbol frequencies.
        // The resulting generated code is optimal for static Huffman coding and also canonical.
        FrequencyTable freqs = getFrequencies(inputFile);
        freqs.increment(256);  // EOF symbol gets a frequency of 1
        CodeTree code = freqs.buildCodeTree();
        CanonicalCode canonCode = new CanonicalCode(code, freqs.getSymbolLimit());
        // Replace code tree with canonical one. For each symbol,
        // the code value may change but the code length stays the same.
        code = canonCode.toCodeTree();

        // Read input file again, compress with Huffman coding, and write output file
        try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile))) {
            try (BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                writeCodeLengthTable(out, canonCode);
                compress(code, in, out);
            }
        }

//        InputStream file = new FileInputStream("src/input.bat");
//
//        BitInputStream byteStream = new BitInputStream(file);
//
//
//        ArrayList<String> Bytes = new ArrayList<String >();
//        ArrayList<Integer> freq = new ArrayList<Integer>();
//
//        byte[] bytes = new byte[file.available()];  //reads how much bytes are readable from file
//        System.out.println(file.available());
//        file.read(bytes);//reads the file and save all read bytes into b
//
//        for (byte b : bytes) {
//            //Gal reiktu pameginti atskirai baitus issisaugoti i koki masyva
//            System.out.println(Integer.toBinaryString(b & 255 | 256).substring(1));
//            Bytes.add(Integer.toBinaryString(b & 255 | 256).substring(1));
//        }
    }


    // Returns a frequency table based on the bytes in the given file.
    // Also contains an extra entry for symbol 256, whose frequency is set to 0.
    private static FrequencyTable getFrequencies(File file) throws IOException {
        FrequencyTable freqs = new FrequencyTable(new int[257]);
        try (InputStream input = new BufferedInputStream(new FileInputStream(file))) {
            while (true) {
                int b = input.read();
                if (b == -1)
                    break;
                freqs.increment(b);
            }
        }
        return freqs;
    }


    // To allow unit testing, this method is package-private instead of private.
    static void writeCodeLengthTable(BitOutputStream out, CanonicalCode canonCode) throws IOException {
        for (int i = 0; i < canonCode.getSymbolLimit(); i++) {
            int val = canonCode.getCodeLength(i);
            // For this file format, we only support codes up to 255 bits long
            if (val >= 256)
                throw new RuntimeException("The code for a symbol is too long");

            // Write value as 8 bits in big endian
            for (int j = 7; j >= 0; j--)
                out.write((val >>> j) & 1);
        }
    }


    // To allow unit testing, this method is package-private instead of private.
    static void compress(CodeTree code, InputStream in, BitOutputStream out) throws IOException {
        HuffmanEncoder enc = new HuffmanEncoder(out);
        enc.codeTree = code;
        while (true) {
            int b = in.read();
            if (b == -1)
                break;
            enc.write(b);
        }
        enc.write(256);  // EOF
    }

}