import java.io.*;
import java.util.ArrayList;

public class HuffmanCompress {

    public static void main(String[] args) throws IOException {


        //Required two files: input and output
        if (args.length != 2) {
            System.out.println("Error: files not specified");
            System.exit(1);
            return;
        }

        try {
            File inputFile = new File(args[0]);
            File outputFile = new File(args[1]);

            Frequencies freqs = Frequencies.getFrequencies(inputFile);
            freqs.increment(256);

            //freqs.printFreqs();


            CodeTree code = CodeTree.buildCodeTree(freqs);


            //System.out.println("length: "+freqs.frequencies.length);
            CanonicalCode canonCode = new CanonicalCode(code);
            code = canonCode.toCodeTree();


            try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile))) {
                try (BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                    writeCodeLengthTable(out, canonCode);
                    compress(code, in, out);
                }
            }


        }catch(Exception e){
            e.printStackTrace();
        }
    }


    static void writeCodeLengthTable(BitOutputStream out, CanonicalCode canonCode) throws IOException {
        for (int i = 0; i < 257; i++) {
            int val = canonCode.getCodeLength(i);
            //System.out.println("val: " + val);

            // Write value as 8 bits in big endian
            for (int j = 7; j >= 0; j--) {
                out.write((val >>> j) & 1); //Pasiimamas tik vienas bitas ir irasomas. Ima tik po viena bita is val
                //System.out.println("Bit: "+((val >>> j) & 1));
            }
        }
    }

    static void compress(CodeTree code, InputStream in, BitOutputStream out) throws IOException {
        HuffmanEncoder enc = new HuffmanEncoder(out);
        enc.codeTree = code;
        while (true) {
            int b = in.read();
            //System.out.println("b: "+b);
            if (b == -1)
                break;
            enc.write(b);
            System.out.println("b: "+b);
        }
        enc.write(256);  // EOF
    }

    }
