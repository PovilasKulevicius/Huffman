import java.io.*;

public final class HuffmanDecompress {
    public static int bits = 2;

    // Command line main application function.
    public static void main(String[] args) throws IOException {
        // Handle command line arguments
        if (args.length != 2) {
            System.err.println("Usage: java HuffmanDecompress InputFile OutputFile");
            System.exit(1);
            return;
        }
        File inputFile  = new File(args[0]);
        File outputFile = new File(args[1]);

        // Perform file decompression
        try (BitInputStream in = new BitInputStream(new BufferedInputStream(new FileInputStream(inputFile)))) {
            try (BitOutputStream out =new BitOutputStream (new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                CanonicalCode canonCode = readCodeLengthTable(in);
                CodeTree code = canonCode.toCodeTree();
                decompress(code, in, out);
            }
        }
    }


    // To allow unit testing, this method is package-private instead of private.
    static CanonicalCode readCodeLengthTable(BitInputStream in) throws IOException {
        int[] codeLengths = new int[(int)Math.pow(2,bits)+1];
        for (int i = 0; i < codeLengths.length; i++) {
            // For this file format, we read 8 bits in big endian
            int val = 0;
            for (int j = 0; j < bits; j++)
                val = (val << 1) | in.readNoEof();
            codeLengths[i] = val;
        }
        return new CanonicalCode(codeLengths);
    }


    // To allow unit testing, this method is package-private instead of private.
    static void decompress(CodeTree code, BitInputStream in, BitOutputStream out) throws IOException {
        HuffmanDecoder dec = new HuffmanDecoder(in);
        HuffmanEncoder enc = new HuffmanEncoder(out);
        dec.codeTree = code;
        while (true) {
            int symbol = dec.read();
            if (symbol == (int)Math.pow(2,bits))  // EOF symbol
                break;

            for (int j = bits-1; j >= 0; j--) {
                out.write((symbol >>> j) & 1); //Pasiimamas tik vienas bitas ir irasomas. Ima tik po viena bita is val
                //System.out.println("Bit: "+((val >>> j) & 1));
            }
            //out.write(symbol);
        }
    }

}