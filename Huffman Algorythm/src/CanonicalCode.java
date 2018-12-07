import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class CanonicalCode {

    private int symbolLimit = 257;
    private int[] codeLengths;

    public CanonicalCode(CodeTree tree) {
        //Objects.requireNonNull(tree);
//        if (symbolLimit < 2)
//            throw new IllegalArgumentException("At least 2 symbols needed");
        codeLengths = new int[symbolLimit];
        buildCodeLengths(tree.root, 0);
    }


    // Recursive helper method for the above constructor.
    private void buildCodeLengths(Node node, int depth) {
        if (node instanceof InternalNode) {
            InternalNode internalNode = (InternalNode)node;
            buildCodeLengths(internalNode.leftChild , depth + 1);
            buildCodeLengths(internalNode.rightChild, depth + 1);
        } else if (node instanceof Leaf) {
            int symbol = ((Leaf)node).symbol;
//            if (symbol >= codeLengths.length)
//                throw new IllegalArgumentException("Symbol exceeds symbol limit");
//            // Note: CodeTree already has a checked constraint that disallows a symbol in multiple leaves
//            if (codeLengths[symbol] != 0)
//                throw new AssertionError("Symbol has more than one code");
            codeLengths[symbol] = depth;
        } else {
            System.out.println("Error. Node is nor a leaf nor an internalNode");
        }
    }



    /*---- Various methods ----*/

    /**
     * Returns the symbol limit for this canonical Huffman code.
     * Thus this code covers symbol values from 0 to symbolLimit&minus;1.
     * @return the symbol limit, which is at least 2
     */
    public int getSymbolLimit() {
        return codeLengths.length;
    }


    /**
     * Returns the code length of the specified symbol value. The result is 0
     * if the symbol has node code; otherwise the result is a positive number.
     * @param symbol the symbol value to query
     * @return the code length of the symbol, which is non-negative
     * @throws IllegalArgumentException if the symbol is out of range
     */
    public int getCodeLength(int symbol) {
        if (symbol < 0 || symbol >= codeLengths.length)
            throw new IllegalArgumentException("Symbol out of range");
        return codeLengths[symbol];
    }


    /**
     * Returns the canonical code tree for this canonical Huffman code.
     * @return the canonical code tree
     */
    public CodeTree toCodeTree() {
        List<Node> nodes = new ArrayList<Node>();
        for (int i = max(codeLengths); i >= 0; i--) {  // Descend through code lengths
            List<Node> newNodes = new ArrayList<Node>();

            // Add leaves for symbols with positive code length i
            if (i > 0) {
                for (int j = 0; j < codeLengths.length; j++) {
                    if (codeLengths[j] == i)
                        newNodes.add(new Leaf(j)); //Isidedame ASCII reiksmes ilgiausio kodo
                }
            }

            // Merge pairs of nodes from the previous deeper layer
            for (int j = 0; j < nodes.size(); j += 2) {
                newNodes.add(new InternalNode(nodes.get(j), nodes.get(j + 1))); //Sujungia lapus i InternalNodes
            }
            nodes = newNodes;
        }
        return new CodeTree((InternalNode)nodes.get(0), codeLengths.length);
    }


    // Returns the maximum value in the given array, which must have at least 1 element.
    private static int max(int[] array) {
        int result = array[0];
        for (int x : array)
            result = Math.max(x, result);
        return result;
    }

}