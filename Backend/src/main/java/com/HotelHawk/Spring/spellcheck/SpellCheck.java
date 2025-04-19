package com.HotelHawk.Spring.spellcheck;
//done for request HTTP
import java.util.Scanner;

class AVLNode {
    String city;
    int height;
    AVLNode left, right;

    AVLNode(String city) {
        this.city = city;
        this.height = 1;
    }
}

public class SpellCheck {
    AVLNode root;

    int height(AVLNode node) {
        if (node == null)
            return 0;
        return node.height;
    }

    int max(int a, int b) {
        return (a > b) ? a : b;
    }

    int getBalance(AVLNode node) {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }

    AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        return x;
    }

    AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        return y;
    }

    AVLNode insert(AVLNode node, String city) {
        if (node == null)
            return new AVLNode(city);

        if (city.compareToIgnoreCase(node.city) < 0)
            node.left = insert(node.left, city);
        else if (city.compareToIgnoreCase(node.city) > 0)
            node.right = insert(node.right, city);
        else // Duplicate keys not allowed
            return node;

        node.height = 1 + max(height(node.left), height(node.right));

        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && city.compareToIgnoreCase(node.left.city) < 0)
            return rightRotate(node);

        // Right Right Case
        if (balance < -1 && city.compareToIgnoreCase(node.right.city) > 0)
            return leftRotate(node);

        // Left Right Case
        if (balance > 1 && city.compareToIgnoreCase(node.left.city) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && city.compareToIgnoreCase(node.right.city) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    void inorder(AVLNode node) {
        if (node != null) {
            inorder(node.left);
            System.out.print(node.city + " ");
            inorder(node.right);
        }
    }

    public static String initialize(SpellCheck tree, String cityname) {
        tree.root = tree.insert(tree.root, "TORONTO");
        tree.root = tree.insert(tree.root, "OTTAWA");
        tree.root = tree.insert(tree.root, "CALGARY");
        tree.root = tree.insert(tree.root, "VANCOUVER");
        tree.root = tree.insert(tree.root, "MONTREAL");
        tree.root = tree.insert(tree.root, "WINDSOR");
        tree.root = tree.insert(tree.root, "HALIFAX");
        tree.root = tree.insert(tree.root, "WINNIPEG");
        tree.root = tree.insert(tree.root, "EDMONTON");
        tree.root = tree.insert(tree.root, "HAMILTON");
        return edit_distance(tree, cityname.toUpperCase());
    }

    static int min(int x, int y, int z) {
        if (x <= y && x <= z)
            return x;
        if (y <= x && y <= z)
            return y;
        else
            return z;
    }

    private static int edit_dist(String city, String find, int m, int n) {
        if (m == 0)
            return n;
        if (n == 0)
            return m;
        if (city.charAt(m - 1) == find.charAt(n - 1))
            return edit_dist(city, find, m - 1, n - 1);
        return 1 + min(edit_dist(city, find, m, n - 1), // Insert
                edit_dist(city, find, m - 1, n), // Remove
                edit_dist(city, find, m - 1, n - 1));// replace
    }

    public static String edit_distance(SpellCheck tree, String find) {
        String[] closestCity = {""}; // Array to hold closest city
        int[] minDistance = {Integer.MAX_VALUE}; // Array to hold minimum distance
        //  System.out.println("Enter city name: " + find);
        inorder(tree.root, find, minDistance, closestCity);

        System.out.println("Did you mean: " + closestCity[0]);
        return closestCity[0];
    }

    private static void inorder(AVLNode node, String find, int[] minDistance, String[] closestCity) {
        if (node != null) {
            int distance = edit_dist(node.city, find, node.city.length(), find.length());
            System.out.println(distance + " " + node.city + " " + find);
            if (distance < minDistance[0]) {
                minDistance[0] = distance;
                closestCity[0] = node.city;
            }
            inorder(node.left, find, minDistance, closestCity);
            inorder(node.right, find, minDistance, closestCity);
        }
    }
    public static String main(String[] args) {
        SpellCheck tree = new SpellCheck();
        String inputCity = args[0];
        return initialize(tree, inputCity);

    }
}