package model;

public class Node {
    public Transaction data;
    public Node next;
    public Node prev;

    public Node(Transaction data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}