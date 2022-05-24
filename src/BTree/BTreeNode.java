package BTree;

import java.util.Arrays;

import static BTree.BTree.minDegree;

public class BTreeNode implements IBTreeNode{

    int num_of_keys = 0;
    String[] keys;
    int[] values;
    BTreeNode[] children;
    boolean leaf = true;

    BTreeNode(){
        this.keys = new String[2*minDegree-1];
        this.values = new int[2*minDegree-1];
        this.children = new BTreeNode[2*minDegree];
    }

    @Override
    public int getNumOfKeys() {
        return num_of_keys;
    }

    @Override
    public void setNumOfKeys(int numOfKeys) {
        num_of_keys = numOfKeys;
    }

    public int find(Comparable key){
        for (int i=0; i<this.num_of_keys; i++){
            if (key.compareTo(this.keys[i]) == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean isLeaf() {
        return leaf;
    }

    @Override
    public void setLeaf(boolean isLeaf) {
        leaf = isLeaf;
    }

    @Override
    public String[] getKeys() {
        return keys;
    }

    @Override
    public void setKeys(String[] keys) {
        this.keys = Arrays.copyOf(keys, keys.length);
    }

    @Override
    public int[] getValues() {
        return values;
    }

    @Override
    public void setValues(int[] values) {
        this.values = Arrays.copyOf(values, values.length);
    }

    @Override
    public BTreeNode[] getChildren() {
        return children;
    }

    @Override
    public void setChildren(BTreeNode[] children) {
        this.children = Arrays.copyOf(children, children.length);
    }
}
