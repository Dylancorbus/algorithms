import java.util.*;

public class TreeTraversal {


    static class Node {
		int data;
		Node next;

		Node rightNode;
		Node leftNode;

		public Node(int data) {
			this.data = data;
		}

		public Node getRightNode() {
			return rightNode;
		}



		public Node getLeftNode() {
			return leftNode;
		}

		public void setNodes(Node lt, Node rt) {
			this.leftNode = lt;
			this.rightNode = rt;
		}

		public Node getNext() {
			return next;
		}

		public void setNext(Node next) {
			this.next = next;
		}

		@Override
		public String toString() {
			try {

			return String.valueOf(data) + leftNode.toString() + rightNode.toString();
			} catch (Exception e) {
				return "";
			}
		}
	}

    public static int depthFirstSearchTree(Node node, int find) {
			if(node.data == find) return node.data;
			int left = depthFirstSearchTree(node.leftNode, find);
			int right = depthFirstSearchTree(node.rightNode, find);
			if( left == find) return left;
			if(right == find) return right;
			return 0;

	}

	public static int breadthFirstSearchTree(Node node, Queue<Node> queue, int find) {
		if(queue.isEmpty() && node != null) {
			if(node.data == find) return node.data;
			queue.add(node);
			int x = breadthFirstSearchTree(null, queue, find);
			if(x == find) return x;
		} else if(!queue.isEmpty()){
			Node node1 = queue.poll();
			if(node1.data == find) return node1.data;
			if(node1.leftNode != null) {
				if(node1.leftNode.data == find) return node1.leftNode.data;
				queue.add(node1.leftNode);
			}
			if(node1.rightNode != null) {
				if(node1.rightNode.data == find) return node1.rightNode.data;
				queue.add(node1.rightNode);
			}
			int x = breadthFirstSearchTree(null, queue, find);
			if(x == find) return x;
		}
		return 0;
	}

    public static void revLL(Node node) {
		List<Node> toRev = new ArrayList<>();
		if(node != null) {
			toRev.add(node);
		}
		Node itNode = node;

		while (itNode.next != null) {
			itNode = itNode.next;
			toRev.add(itNode);
		}
		for (int i = toRev.size() - 1; i >= 0; i--) {

			System.out.print(toRev.get(i).data);

		}
		System.out.println();

	}

    public static void main(String[] args) {
        Node depthnode1 = new Node(9);
        Node depthnode2 = new Node(4);
        Node depthnode3 = new Node(5);
        Node depthnode4 = new Node(2);                                                                   
        Node depthnode5 = new Node(3);                                                                   
        Node depthnode6 = new Node(6);                                                                   
        Node depthnode7 = new Node(8);                                                                   
        Node depthnode8 = new Node(1);                                                                   
        Node depthnode9 = new Node(7);                                                                   
        
        depthnode1.setNodes(depthnode2, depthnode3);
        depthnode2.setNodes(depthnode4, depthnode5);
        depthnode4.setNodes(depthnode8, null);
        depthnode3.setNodes(depthnode6, depthnode7);
        depthnode6.setNodes(depthnode9, null);

        System.out.println(depthFirstSearchTree(depthnode1, 6));

        Node breadthnode1 = new Node(1);
        Node breadthnode2 = new Node(2);
        Node breadthnode3 = new Node(3);
        Node breadthnode4 = new Node(4);                                                                  
        Node breadthnode5 = new Node(5);                                                                  
        Node breadthnode6 = new Node(6);                                                                   
        Node breadthnode7 = new Node(7);                                                                   
        Node breadthnode8 = new Node(8);                                                                   
        Node breadthnode9 = new Node(9);

        breadthnode1.setNodes(breadthnode2, breadthnode3);
        breadthnode2.setNodes(breadthnode4, breadthnode5);
        breadthnode4.setNodes(breadthnode8, null);
        breadthnode3.setNodes(breadthnode6, breadthnode7);
        breadthnode6.setNodes(breadthnode9, null);

        Queue<Node> queue = new LinkedList<>();
        System.out.println(breadthFirstSearchTree(breadthnode1, queue, 6));

        Node revnode1 = new Node(8);
        Node revnode2 = new Node(9);
        Node revnode3 = new Node(4);
        Node revnode4 = new Node(6);
        Node revnode5 = new Node(7);
        Node revnode6 = new Node(3);
        Node revnode7 = new Node(2);
        Node revnode8 = new Node(5);
        Node revnode9 = new Node(1);
        
        revnode9.setNext(revnode7);
        revnode7.setNext(revnode6);
        revnode6.setNext(revnode3);
        revnode3.setNext(revnode8);
        revnode8.setNext(revnode4);
        revnode4.setNext(revnode5);
        revnode5.setNext(revnode1);
        revnode1.setNext(revnode2);

        revLL(revnode9);
    }

}