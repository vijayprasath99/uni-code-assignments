


// THIS IMPLEMENTATION ALLOWS DUPLICATES, IF YOU DON'T WANT DUPLICATES, REMOVE REFERENCES TO frequency you should be good to go.
// Sample : https://github.com/eugenp/tutorials/blob/master/data-structures/src/main/java/com/baeldung/avltree/AVLTree.java
class AVLTree {
    public Node root;

    void insert(int i) {
        root = insert(i, root);
    }

    void delete(int i) {
        root = delete(i, root);
    }

    int getLargest() {
        Node n = rightMostNode(root);
        return n.val;
    }

    void removeLargest() {
        Node n = rightMostNode(root);
        if(n != null) {
            delete(n.val);
        }
    }

    Node rightMostNode(Node n) {
        if(n == null) {
            return null;
        }
        while(n.right != null) {
            n = n.right;
        }
        return n;
    }


    Node delete(int i, Node n) {
        if(n == null) {
            return n;
        }

        if(i < n.val) {
            n.left = delete(i, n.left);
        } else if(i > n.val) {
            n.right = delete(i, n.right);
        } else {
            n.frequency--;
            if(n.frequency > 0) {
                // Remove this if condition and make the first else if as the if condtition and
                // to implement only unique keys
                return n;
            } else if(n.left == null || n.right == null) {
                n = n.left == null ? n.right : n.left;
            } else {
                Node successor = leftMostNode(n.right);
                n.val = successor.val;
                n.right = delete(successor.val, n.right);
            }
        }

        if(n != null) {
            n = rebalance(n);
        }

        return n;
    }

    Node leftMostNode(Node n) {
        while(n.left != null) {
            n = n.left;
        }
        return n;
    }

    Node insert(int i, Node node) {
        if(node == null) {
            return new Node(i);
        } else if (i < node.val) {
            node.left = insert(i, node.left);
        } else if(i > node.val) {
            node.right = insert(i, node.right);
        } else {
            node.frequency++;
            return node;
        }

        return rebalance(node);
    }

    Node rebalance(Node node) {
        updateHeight(node);
        int bf = balanceFactor(node);
        if(bf < -1) {
            if(height(node.left.left) > height(node.left.right)) {
                node = rotateRight(node);
            } else {
                node.left = rotateLeft(node.left);
                node = rotateRight(node);
            }
        } else if (bf > 1) {
            if(height(node.right.right) > height(node.right.left)) {
                node = rotateLeft(node);
            } else {
                node.right = rotateRight(node.right);
                node = rotateLeft(node);
            }
        }
        return node;
    }

    Node rotateLeft(Node n) {
        Node m = n.right;
        Node p = m.left;
        m.left = n;
        n.right = p;
        updateHeight(n);
        updateHeight(m);
        return m;
    }

    Node rotateRight(Node n) {
        Node m = n.left;
        Node q = m.right;
        m.right = n;
        n.left = q;
        updateHeight(n);
        updateHeight(m);
        return m;
    }

    void updateHeight(Node n) {
        if(n == null) return;
        n.height = Math.max(height(n.left), height(n.right)) + 1;
    }

    int balanceFactor(Node n) {
        return height(n.right) - height(n.left);
    }

    int height(Node node) {
        if(node == null) return 0;
        return node.height;
    }
}

class Node {
    public int val;
    public Node left, right;
    public int height;
    public int frequency;

    public Node(int val) {
        this.val = val;
        this.height = 1;
        this.frequency = 1;
    }
}