import java.util.*;

public class CodeTree {
    public final InternalNode root;
    private List<List<Integer>> codes;

    public CodeTree(InternalNode root, int symbolLimit) {
        this.root = Objects.requireNonNull(root);

        codes = new ArrayList<List<Integer>>();
        //add 257 nulls to list
        for (int i = 0; i < symbolLimit; i++) {
            codes.add(null);
        }

        buildCodeList(root, new ArrayList<Integer>());  // Fill 'codes' with appropriate data
    }

    public static CodeTree buildCodeTree(Frequencies freqs) {

        Queue<NodeWithFrequency> pqueue = new PriorityQueue<NodeWithFrequency>();

        // Add leaves for symbols with non-zero frequency
        for (int i = 0; i < freqs.frequencies.length; i++) {
            if (freqs.frequencies[i] > 0)
                pqueue.add(new NodeWithFrequency(new Leaf(i), i, freqs.frequencies[i]));
        }
//**************************************************************************************
         //Apsauga. Turi buti bent du lapai
        for (int i = 0; i < freqs.frequencies.length && pqueue.size() < 2; i++) {
            //System.out.println("TEST1");
            if (freqs.frequencies[i] == 0)
                //System.out.println("TEST2");
                pqueue.add(new NodeWithFrequency(new Leaf(i), i, 0));
        }
//*******************************************************************************

        // Repeatedly tie together two nodes with the lowest frequency
        while (pqueue.size() > 1) {
            NodeWithFrequency x = pqueue.remove();
            NodeWithFrequency y = pqueue.remove();
            pqueue.add(new NodeWithFrequency(
                    new InternalNode(x.node, y.node),
                    Math.min(x.lowestSymbol, y.lowestSymbol),
                    x.frequency + y.frequency));
        }
        /**
         * CIA BAIGEM
         */

        //Remaining node
        InternalNode node_ = (InternalNode)pqueue.remove().node;//Root
        CodeTree code = new CodeTree(node_, freqs.frequencies.length); //frequencies.length = 257, max symbols

        return code;
    }

    private void buildCodeList(Node node, List<Integer> prefix) {
        if (node instanceof InternalNode) {
            InternalNode internalNode = (InternalNode) node;

            prefix.add(0);
            buildCodeList(internalNode.leftChild, prefix);
            prefix.remove(prefix.size() - 1);

            prefix.add(1);
            buildCodeList(internalNode.rightChild, prefix);
            prefix.remove(prefix.size() - 1);

        } else if (node instanceof Leaf) {
            Leaf leaf = (Leaf) node;

//            if (leaf.symbol >= codes.size()) {
//                throw new IllegalArgumentException("Symbol exceeds symbol limit");
//            }
//            if (codes.get(leaf.symbol) != null) {
//                throw new IllegalArgumentException("Symbol has more than one code");
//            }

            codes.set(leaf.symbol, new ArrayList<Integer>(prefix));

        } else {
            System.out.println("Error");
        }
    }
}
