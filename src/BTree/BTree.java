package BTree;


public class BTree implements IBTree{

    private BTreeNode root;
    public static int minDegree;

    public BTree(int min_degree){
        minDegree = min_degree;
        root = new BTreeNode();
        root.leaf = true;
        root.num_of_keys = 0;
    }

    @Override
    public int getMinimumDegree() {
        return minDegree;
    }

    @Override
    public IBTreeNode getRoot() {
        return root;
    }

    @Override
    public void insert(Comparable key, Object value) {
        if (search(root, key) != null){
            BTreeNode node = search(root, key);
            int current_val = node.values[node.find(key)];
            node.values[node.find(key)]= current_val+1;
            return;
        }else {
            BTreeNode r = root;
            if (r.num_of_keys == 2*this.minDegree-1){
                BTreeNode s = new BTreeNode();
                root = s;
                s.leaf = false;
                s.num_of_keys = 0;
                s.children[0] = r;
                split(s,0,r);
                insert_values(s,key);

            }else{
                insert_values(r,key);
            }
        }
    }

    @Override
    public BTreeNode search(Comparable key) {
        BTreeNode node  = search(root,key);
        return node;
    }

    private void split(BTreeNode x, int pos, BTreeNode y){
        BTreeNode z= new BTreeNode();
        z.leaf = y.leaf;
        z.num_of_keys = this.minDegree-1;
        for (int j=0; j<this.minDegree-1; j++){
            z.keys[j] =  y.keys[j+this.minDegree];
        }
        if (!y.leaf){
            for (int j=0; j<this.minDegree; j++){
                z.children[j] = y.children[j+this.minDegree];
            }
        }
        y.num_of_keys = this.minDegree-1;
        for (int j=x.num_of_keys; j>=pos+1; j--){
            x.children[j+1] = x.children[j];
        }
        x.children[pos+1] = z;

        for (int j=x.num_of_keys-1; j>=pos; j--){
            x.keys[j+1] = x.keys[j];
        }
        x.keys[pos] = y.keys[this.minDegree-1];
        x.num_of_keys ++ ;
    }

    public void show(){
        this.show (this.root);
    }

    private void show(BTreeNode node){
        assert (node==null);
        for (int i=0; i<node.num_of_keys; i++){
            //System.out.println("hi");
            System.out.print(node.keys[i]+" ");
        }
        System.out.println();
        if (!node.isLeaf()){
            for (int i=0; i<node.num_of_keys+1; i++){
                show((BTreeNode) node.children[i]);
            }
        }
    }

    private void insert_values(BTreeNode node,Comparable key){
        if (node.isLeaf()){
            int i;
            for (i = node.num_of_keys-1; i>=0 && (key.compareTo(node.keys[i])<0); i--){
                node.keys[i+1] = node.keys[i];
            }
            node.keys[i+1] =  key.toString();
            node.num_of_keys++;
        }else {
            int i;
            for (i = node.num_of_keys-1; i>=0 && (key.compareTo(node.keys[i])<0); i--){};
            i++;
            BTreeNode tmp = (BTreeNode)node.children[i];
            if (tmp.num_of_keys == 2*this.minDegree-1) {
                split(node, i, tmp);
                if (key.compareTo(node.keys[i]) > 0){
                    i++;
                }
            }
            insert_values((BTreeNode)node.children[i], key);
        }
    }

    public BTreeNode search(BTreeNode node, Comparable key) {
        int i=0;
        if (node == null) return null;

        for (i=0; i< node.num_of_keys; i++){
            if (key.compareTo(node.keys[i]) < 0){
                break;
            }
            if (key.compareTo(node.keys[i]) == 0){
                return node;
            }
        }
        if (node.isLeaf()) return null;
        else return search(node.children[i], key);
    }

    @Override
    public boolean delete(Comparable key) {
        if(search(root, key) == null) return false;
        else remove(root, key);
        return true;
    }

    public boolean remove(BTreeNode node , Comparable key) {
        int pos = node.find(key);
        if (pos != -1){
            if(node.isLeaf()){
                int i=0;
                for (i=0; i<node.num_of_keys && (key.compareTo(node.keys[i])!=0); i++){
                };
                for (;i< node.num_of_keys;i++){
                    if (i!=2*minDegree-2){
                        node.keys[i] = node.keys[i+1];
                    }
                }
                node.num_of_keys--;
                return true;
            }
            if (!node.isLeaf()){
                BTreeNode prev = node.children[pos];
                String prevKey="";
                if (prev.num_of_keys>=minDegree){
                    for (;;){
                        if (prev.isLeaf()){
                            prevKey = prev.keys[prev.num_of_keys-1];
                            break;
                        }else{
                            prev = prev.children[prev.num_of_keys];
                        }
                    }
                    remove(prev,prevKey);
                    node.keys[pos] = prevKey;
                    return true;
                }
                BTreeNode next = node.children[pos+1];

                if (next.num_of_keys >= minDegree){
                    String nextKey = next.keys[0];
                    if (!next.isLeaf()){
                        next = next.children[0];
                        for(;;){
                            if (next.isLeaf()){
                                nextKey = next.keys[next.num_of_keys-1];
                                break;
                            }else {
                                next = next.children[next.num_of_keys];
                            }
                        }
                    }
                    remove(next,nextKey);
                    node.keys[pos] = nextKey;
                    return true;
                }
                int temp = prev.num_of_keys+1;
                prev.keys[prev.num_of_keys++] = node.keys[pos];
                for(int i=0, j=prev.num_of_keys; i< next.num_of_keys; i++){
                    prev.keys[j++] = next.keys[i];
                    prev.num_of_keys++;
                }
                for(int i=0; i< next.num_of_keys+1; i++){
                    prev.children[temp++] = next.children[i];
                }

                node.children[pos] = prev;

                for (int i=pos; i<node.num_of_keys; i++){
                    if (i!= 2*minDegree-2){
                        node.keys[i] = node.keys[i+1];
                    }
                }
                for (int i=pos+1; i<node.num_of_keys+1; i++){
                    if (i!= 2*minDegree-1){
                        node.children[i] = node.children[i+1];
                    }
                }

                node.num_of_keys--;
                if (node.num_of_keys==0){
                    if (node == root){
                        root = node.children[0];
                    }
                    node = node.children[0];
                }
                remove(prev, key);
                return true;
            }
        }else {
            for(pos =0; pos<node.num_of_keys; pos++){
                if(key.compareTo(node.keys[pos]) < 0){
                    break;
                }
            }
            BTreeNode temp = node.children[pos];
            if (temp.num_of_keys >= minDegree){
                remove(temp, key);
                return true;
            }
            if (true){
                BTreeNode nb =null;
                String divider = "";
                if(pos!=node.num_of_keys && (node.children[pos+1].num_of_keys >= minDegree)){
                    divider = node.keys[pos];
                    nb = node.children[pos+1];
                    node.keys[pos] = nb.keys[0];
                    temp.keys[temp.num_of_keys++] = divider;
                    temp.children[temp.num_of_keys] = nb.children[0];
                    for (int i=1; i<nb.num_of_keys; i++){
                        nb.keys[i-1] = nb.keys[i];
                    }
                    for (int i=1; i<nb.num_of_keys; i++){
                        nb.children[i-1] = nb.children[i];
                    }
                    nb.num_of_keys--;
                    remove(temp,key);
                    return true;
                }else if(pos!=0 && (node.children[pos-1].num_of_keys >= minDegree)){
                    divider = node.keys[pos-1];
                    nb = node.children[pos-1];
                    node.keys[pos-1] = nb.keys[nb.num_of_keys-1];
                    BTreeNode child = nb.children[nb.num_of_keys];
                    nb.num_of_keys--;

                    for (int i=temp.num_of_keys; i>0; i--){
                        temp.keys[i] = temp.keys[i-1];
                    }
                    temp.keys[0] = divider;
                    for (int i=temp.num_of_keys; i>0; i--){
                        temp.children[i] = temp.children[i-1];
                    }
                    temp.children[0] = child;
                    temp.num_of_keys++;
                    remove(temp,key);
                    return true;
                }else{
                    BTreeNode lt = null;
                    BTreeNode rt = null;
                    boolean last = false;
                    if(pos!=node.num_of_keys){
                        divider = node.keys[pos];
                        lt = node.children[pos];
                        rt = node.children[pos+1];
                    }else{
                        divider = node.keys[pos-1];
                        lt = node.children[pos];
                        rt = node.children[pos-1];
                        last = true;
                        pos--;
                    }
                    for(int i=pos; i<node.num_of_keys-1;i++){
                        node.keys[i] = node.keys[i+1];
                    }
                    for(int i=pos+1; i<node.num_of_keys;i++){
                        node.children[i] = node.children[i+1];
                    }
                    node.num_of_keys--;
                    lt.keys[lt.num_of_keys++] = divider;

                    for (int i=0, j=lt.num_of_keys; i<rt.num_of_keys+1;i++,j++){
                        if (i<rt.num_of_keys){
                            lt.keys[j] = rt.keys[i];
                        }
                        lt.children[j] = rt.children[i];
                    }
                    lt.num_of_keys += rt.num_of_keys;
                    if (node.num_of_keys==0){
                        if(node == root){
                            root = node.children[0];
                        }
                        node = node.children[0];
                    }
                    remove(lt,key);
                    return true;
                }
            }
        }
        return false;
    }


    public static void main(String[] args){
        BTree b = new BTree(3);
        b.insert("08","");
        b.insert("09","");
        b.insert("10","");
        b.insert("11","");
        b.insert("15","");
        b.insert("20","");
        b.insert("12","");

        b.show();
        /* 10 8 9 11 15 17 20  not found*/
        /* 10 8 9 11 12 15 20   found*/

        if (b.search(b.root,"12") != null){
            System.out.println();
            System.out.println("found");
        }else {
            System.out.println("not found");
        }

        b.delete("10");
        System.out.println();
        b.show();

        boolean x = b.delete("08");
        System.out.println();
        System.out.println(x);
        System.out.println();
        b.show();
    }
}
