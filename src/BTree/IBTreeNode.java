package BTree;

public interface IBTreeNode<K extends Comparable<K>, V> {

	/**
	 * @return the numOfKeys return number of keys in this node.
	 */
	public int getNumOfKeys();
	
	/**
	 * @param numOfKeys
	 */
	public void setNumOfKeys(int numOfKeys);
	
	/**
	 * @return isLeaf if the node is leaf or not.
	 */
	public boolean isLeaf();
	
	/**
	 * @param isLeaf
	 */
	public void setLeaf(boolean isLeaf);
	
	/**
	 * @return the keys return the list of keys of the given node.
	 */
	public String[] getKeys();
	
	/**
	 * @param keys the keys to set
	 */
	public void setKeys(String[] keys);
	
	/**
	 * @return the values return the list of values for the given node.
	 */
	public int[] getValues();
	
	/**
	 * @param values the values to set
	 */
	public void setValues(int[] values);
	
	/**
	 * @return the children return the list of children for the given node.
	 */
	public BTreeNode[] getChildren();
	
	/**
	 * @param children the children to set
	 */
	public void setChildren(BTreeNode[] children);
}
