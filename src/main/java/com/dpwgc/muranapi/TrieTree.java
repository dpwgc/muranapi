package com.dpwgc.muranapi;

import java.util.HashMap;
import java.util.Map;

public class TrieTree {

    private final TrieNode root;

    public TrieTree() {
        root = new TrieNode();
    }

    public void insert(String path, Node node) {
        TrieNode current = root;
        for (char c : path.toCharArray()) {
            current.children.putIfAbsent(c, new TrieNode());
            current = current.children.get(c);
        }
        current.node = node;
    }

    public Node search(String path) {
        TrieNode trieNode = root;
        for (char c : path.toCharArray()) {
            if (!trieNode.children.containsKey(c)) {
                return null;
            }
            trieNode = trieNode.children.get(c);
        }
        if (trieNode != null && trieNode.node != null) {
            return trieNode.node;
        }
        return null;
    }

    public static class TrieNode {
        Node node;
        Map<Character, TrieNode> children;

        public TrieNode() {
            children = new HashMap<>();
            node = null;
        }
    }
}

