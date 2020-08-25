package br.com.technologies.venom.servico;

import java.util.ArrayList;
import java.util.List;

class CustomLinkedList {

    Node head;

    class Node {
        String data;
        Node next;

        // Constructor
        Node(String d) {
            data = d;
            next = null;
        }
    }

    void removeDuplicates() {
        Node ptr1 = null, ptr2 = null, dup = null;
        ptr1 = head;

        while (ptr1 != null && ptr1.next != null) {
            ptr2 = ptr1;
            while (ptr2.next != null) {
                if (ptr1.data.equals(ptr2.next.data)) {
                    dup = ptr2.next;
                    ptr2.next = ptr2.next.next;
                    System.gc();
                } else {
                    ptr2 = ptr2.next;
                }
            }
            ptr1 = ptr1.next;
        }
    }

    public void add(String new_data) {
        Node new_node = new Node(new_data);
        new_node.next = head;
        head = new_node;
    }

    void print() {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.data + " ");
            temp = temp.next;
        }
        System.out.println();
    }

    public List<String> getList() {
        List<String> lista = new ArrayList<>();
        Node temp = head;
        while (temp != null) {
            lista.add(temp.data);
            temp = temp.next;
        }
        return lista;
    }
}