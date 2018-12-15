import java.io.*;
import java.util.Scanner;

public final class HuffmanDecompress {
    public static int bits;

    // Command line main application function.
    public static void main(String[] args) throws IOException {
        String inputFileName = "";
        String outputFileName = "";
        if(args.length == 2){
            inputFileName = args[0];
            outputFileName = args[1];
        }
        else {
            Scanner c = new Scanner(System.in);
            System.out.println("Įveskite dekoduojamą failą: ");
            String inputfile = c.next();
            System.out.println("Įveskite failą į kurį bus atspaustas kodas: ");
            String outputfile = c.next();
            c.close();
            // Handle command line arguments
//        if (args.length != 2) {
//            System.err.println("Usage: java HuffmanDecompress InputFile OutputFile");
//            System.exit(1);
//            return;
//        }
        }
        File inputFile  = new File(inputFileName);
        File outputFile = new File(outputFileName);

        // Perform file decompression
        try (BitInputStream in = new BitInputStream(new BufferedInputStream(new FileInputStream(inputFile)))) {
            try (BitOutputStream out =new BitOutputStream (new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                bits = readFirstByte(in);
                System.out.println("_________decompress_____");
                CanonicalCode canonCode = readCodeLengthTable(in);
                CodeTree code = canonCode.toCodeTree();
                decompress(code, in, out);
                System.out.println("dec end");
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
        dec.codeTree = code;
        while (true) {
            int symbol = dec.read();
            //System.out.println("found " + symbol);
            if (symbol == (int)Math.pow(2,bits)) {// EOF symbol
                break;
            }
            //System.out.println("written as");
            for (int j = bits-1; j >= 0; j--) {
                out.write((symbol >>> j) & 1); //Pasiimamas tik vienas bitas ir irasomas. Ima tik po viena bita is val
                //System.out.println("Bit: "+((val >>> j) & 1));
            }
            //out.write(symbol);
        }
        System.out.println("writing remaining");
        int bitsLeft = readFirstByte(in);
        System.out.println("bits left" + bitsLeft);
        writeRemaining(bitsLeft, in, out);
        System.out.println("pab");
    }
    static public int readFirstByte(BitInputStream in)throws IOException{
        int val = 0;
        for (int j = 0; j < 8; j++)
            val = (val << 1) | in.readNoEof();
        return val;
    }
    static public void writeRemaining(int bitsLeft, BitInputStream in, BitOutputStream out) throws IOException {
        int [] arr = new int [bitsLeft];
        for(int k = 0; k<bitsLeft; ++k){
            int bit = in.read();
            arr[k] = bit;
            if(bit == -1)break;
        }
        for(int k = bitsLeft-1; k>=0; k--){
            out.write(arr[k]);
        }
    }
}