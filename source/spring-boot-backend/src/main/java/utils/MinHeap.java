package utils;

import java.util.ArrayList;
import java.util.List;

import com.example.todoapp.models.HeapNode;

public class MinHeap {

    private List<HeapNode> list;

    public MinHeap() {

        this.list = new ArrayList<HeapNode>();
    }

    public MinHeap(List<HeapNode> items) {

        this.list = items;
        buildHeap();
    }

    public void insert(HeapNode item) {

        list.add(item);
        int i = list.size() - 1;
        int parent = parent(i);

        while (parent != i && list.get(i).getDistance() < list.get(parent).getDistance()) {

            swap(i, parent);
            i = parent;
            parent = parent(i);
        }
    }

    public void buildHeap() {

        for (int i = list.size() / 2; i >= 0; i--) {
            minHeapify(i);
        }
    }

    public HeapNode extractMin() {

        if (list.size() == 0) {

            throw new IllegalStateException("MinHeap is EMPTY");
        } else if (list.size() == 1) {

            HeapNode min = list.remove(0);
            return min;
        }

        // remove the last item ,and set it as new root
        HeapNode min = list.get(0);
        HeapNode lastItem = list.remove(list.size() - 1);
        list.set(0, lastItem);

        // bubble-down until heap property is maintained
        minHeapify(0);

        // return min key
        return min;
    }

    public void decreaseKey(int i, HeapNode key) {

        if (list.get(i).getDistance() < key.getDistance()) {

            throw new IllegalArgumentException("Key is larger than the original key");
        }

        list.set(i, key);
        int parent = parent(i);

        // bubble-up until heap property is maintained
        while (i > 0 && list.get(parent).getDistance() > list.get(i).getDistance()) {

            swap(i, parent);
            i = parent;
            parent = parent(parent);
        }
    }

    private void minHeapify(int i) {

        int left = left(i);
        int right = right(i);
        int smallest = -1;

        // find the smallest key between current node and its children.
        if (left <= list.size() - 1 && list.get(left).getDistance() < list.get(i).getDistance()) {
            smallest = left;
        } else {
            smallest = i;
        }

        if (right <= list.size() - 1 && list.get(right).getDistance() < list.get(smallest).getDistance()) {
            smallest = right;
        }

        // if the smallest key is not the current key then bubble-down it.
        if (smallest != i) {

            swap(i, smallest);
            minHeapify(smallest);
        }
    }

    public HeapNode getMin() {

        return list.get(0);
    }

    public boolean isEmpty() {

        return list.size() == 0;
    }

    private int right(int i) {

        return 2 * i + 2;
    }

    private int left(int i) {

        return 2 * i + 1;
    }

    private int parent(int i) {

        if (i % 2 == 1) {
            return i / 2;
        }

        return (i - 1) / 2;
    }

    private void swap(int i, int parent) {

    	HeapNode temp = list.get(parent);
        list.set(parent, list.get(i));
        list.set(i, temp);
    }

}