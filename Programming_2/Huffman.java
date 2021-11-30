// CTS2: Simeunovic, Savo (3138210)

package code;
import java.util.*;
public class Huffman {
	public static void generateHuffmanCode(Code c) {
		List<Node> list = new ArrayList<>();
		for(int i = 0;i<c.size();i++) {
			Node temp = new Node(c.getAt(i).getProbability(), c.getAt(i).getSymbol(), -1, -1);
			list.add(temp);	
		}
		Collections.sort(list);	
		int left = list.size()-1;
		int right = list.size()-1;
		int n = list.size();
		for(int j = 0;j<n-1;j++) {
			for(int i = list.size()-1;i>-1;i--) {
				if(!list.get(i).isTaken) {
					left = right;
					right = i;
				}
			}
			if(list.get(left).symbol.length() <= list.get(right).symbol.length()) {
				int temp = left;
				left = right;
				right = temp;
			}
			list.get(left).isTaken = true;
			list.get(right).isTaken = true;
			list.add(new Node(list.get(left).probability + list.get(right).probability, list.get(left).symbol + list.get(right).symbol, right, left));
			Collections.sort(list);	
		}
		if(c.size() == 1) {
			c.getBySymbol(list.get(0).symbol).setEncoding("1");
			return;
		}
		generateCodewords(list, list.size()-1, "");
		for(int i=0;i<list.size();i++) {
			if(c.getBySymbol(list.get(i).symbol) != null) {
				if(c.getBySymbol(list.get(i).symbol).getSymbol().equals(list.get(i).symbol)) c.getBySymbol(list.get(i).symbol).setEncoding(list.get(i).codeword);
			}
		}
	}
	private static void generateCodewords(List<Node> list, int currentNode, String currentCodeword) {
		if(currentNode == -1) return;
		list.get(currentNode).codeword = currentCodeword;
		generateCodewords(list, list.get(currentNode).left, currentCodeword + "1");
		generateCodewords(list, list.get(currentNode).right, currentCodeword + "0");
	}
	private static class Node implements Comparable{
		private double probability;
		private String codeword = null;
		private String symbol;
		private int right;
		private int left;
		private boolean isTaken = false;
		@Override
		public int compareTo(Object node_) {
			double otherProb = ((Node)node_).probability;
			if(this.probability == otherProb) {
				return 0;
			}else if(this.probability > otherProb) return 1;
			return -1;
		}
		public Node(double probability, String symbol, int right, int left) {
			this.probability = probability;
			this.symbol = symbol;
			this.right = right;
			this.left = left;
		}
	}
}