import java.io.*;

public class Frequencies {

    public int[] frequencies;

    public Frequencies() {
        frequencies = new int[257];  // Defensive copy
    }

    public static Frequencies getFrequencies(File file) throws IOException {
        try {
        Frequencies freqs = new Frequencies();
            InputStream input = new BufferedInputStream(new FileInputStream(file));


            while (true) {
                int b = input.read();
                //System.out.println(b);
                if (b == -1) {
                    break;
                }
                freqs.increment(b);
            }
            return freqs;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void increment(int symbol) {
        checkSymbol(symbol);
//        if (frequencies[symbol] == Integer.MAX_VALUE) {
//            throw new IllegalStateException("Maximum frequency reached");
//        }
        frequencies[symbol]++;
    }

    private void checkSymbol(int symbol) {
        if (symbol < 0 || symbol >= 257) {
            throw new IllegalArgumentException("Illegal symbol. Symbol out of range.");
        }
    }

    public void printFreqs(){
        for (int i = 0; i< 257; i++){
            System.out.println(frequencies[i]);
        }

    }


}

