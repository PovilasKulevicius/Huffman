import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {


        //Reikia dvieju failu input ir output
        if (args.length != 2) {
            System.out.println("Nenurodyti failai");
            System.exit(1);
            return;
        }

        try {
            File inputFile = new File(args[0]);
            File outputFile = new File(args[1]);

            Frequencies freqs = Frequencies.getFrequencies(inputFile);
            freqs.increment(256);

            freqs.printFreqs();


            CodeTree code = CodeTree.buildCodeTree(freqs);


        }catch(Exception e){
            e.printStackTrace();
        }



    }
    }
