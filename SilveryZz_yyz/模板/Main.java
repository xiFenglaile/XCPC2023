import java.io.*;
import java.util.*;

public class Main {
    static int p,q;
    public static void main(String[] ac) {
        ac in=new ac();
        
        in.println();
        
        in.flush();in.close();
    }


    //字典树插入搜查
    static class Trie{
        class Node{
            boolean end;Node next[];
            Node(){next=new Node [26];}
        }
        Node root=new Node();
        
        void insert(String s){
            Node tmp =root;
            for(char ch:s.toCharArray()){
                int idx=ch-'a';
                if(tmp.next[idx]==null)tmp.next[idx]=new Node();
                tmp=tmp.next[idx];
            }tmp.end=true;
        }
        boolean search(String w){
            Node tmp=root;
            for(char ch:w.toCharArray()){
                if(tmp.next[ch-'a']==null)return false;
                tmp=tmp.next[ch-'a'];
            }
            return tmp.end;
        }
        boolean startsWith(String w){
            Node tmp=root;
            for(char ch:w.toCharArray()){
                if(tmp.next[ch-'a']==null)return false;
                tmp=tmp.next[ch-'a'];
            }
            return true;
        }
    }
    //kmp -> indexof
    static class kmp{
        int next[];String s;String txt;
        kmp(String s,String txt){next=new int[s.length()];this.s=s;this.txt=txt;}
        void getnext(){
          for(int j=1,k=0;j<next.length;j++){
            while(k>0 && s.charAt(j)!=s.charAt(k))k=next[k-1];
            if(s.charAt(j)==s.charAt(k))k++;
            next[j]=k;
          }
        }
        int idx(){
          for(int i=0,j=0;i<txt.length();i++){
            while(j==s.length()||(j>0 && txt.charAt(i)!=s.charAt(j)))j=next[j-1];
            if(s.charAt(j)==txt.charAt(i))j++;
            if(j==s.length())return i-j+1;
          }return -1;
        }
      }
    
    
    //并查集
    static class DSU{
        int id[];int rank[];
        DSU(int n){
            id=new int[n+1];rank=new int[n+1];
            for(int i=0;i<=n;i++){id[i]=i;rank[i]=1;}
        }
        int find(int x){
            if(x==id[x])return x;
            return id[x]=id[find(id[x])];
        }
        void union(int x,int y){
            int xroot=find(x);int yroot=find(y);
            if(xroot!=yroot){
                if(rank[y]<=rank[x])id[yroot]=xroot;
                else id[xroot]=yroot;
                if(rank[xroot]==rank[yroot])xroot++;
            }
        }
    }
    //反转一个数
    int rev(int x) {
        int y = 0;
        for (; x > 0; x /= 10) {
            y = y * 10 + x % 10;
        }
        return y;
    }
    
    static class BIT{//binary indexed tree
        int nums[],tree[],n;//部分写法是天一阁0号元素"0"这样add就不用k+1了
        BIT(int []ns){
            nums=ns;n=ns.length;
            tree=new int[n]; 
            for(int i=0;i<n;i++)add(i,nums[i]);
        }
        void update(int k,int x){//nums[k]=x
            add(k,x-nums[k]);
        }
        void add(int k,int x){//nums[k]+=x
            for(int i=k+1;i<=n;i+=lowbit(i)){
                tree[i-1]+=x;
            }
        }
        int sum(int l,int r){
            return presum(r)-presum(l-1);

        }
        int presum(int k){
            int ans=0;
            for(int i=k+1;i>0;i-=lowbit(i)){
                ans+=tree[i-1];
            }return ans;
        }
        

        int lowbit(int x){return (x)&(-x);}
    }

    static class BITp{//p了个区间修改
        int diff[],tree[],n;
        BITp(int []nums){
            n=nums.length;//别加上int什么的给替换了
            diff=new int[n];tree=new int[n];
            diff[0]=nums[0];
            for(int i=1;i<n;i++)diff[i]=nums[i]-nums[i-1];
            for(int i=0;i<n;i++)add(i,diff[i]);    
        }
        void update(int l,int r,int x){//令l-r上所有元素加上x
            add(l,x);
            add(r+1,-x);
        }
        void add(int k,int x){
            for(int i=k+1;i<=n;i+=lowbit(i)){
                tree[i-1]+=x;
            }
        }
        int presum(int k){
            int ans=0;
            for(int i=k+1;i>0;i-=lowbit(i))ans+=tree[i-1];
            return ans;
        }
        int lowbit(int x){return x & -x;}
    }
    static class BITpp{//pp了个区改区查
        int diff[],tree[],helpertree[];
        int n;
        BITpp(int[] nums){
            n=nums.length;diff=new int [n];tree=new int[n];helpertree=new int [n];
            diff[0]=nums[0];
            for(int i=1;i<n;i++)diff[i]=nums[i]-nums[i-1];
            for(int i=0;i<n;i++){
                add(tree,i,diff[i]);
                add(helpertree,i,i*diff[i]);
            }
        }
        void update(int l,int r,int x){
            add(tree,l,x);
            add(tree,r+1,-x);
            add(helpertree,l,l*x);
            add(helpertree,r+1,(r+1)*(-x));
        }
        void add(int tmptree[],int k,int x){
            for(int i=k+1;i<=n;i+=lowbit(i)){
                tmptree[i-1]+=x;
            }
        }
        int sum(int l,int r){
            int sum1=l*presum(tree, l-1)-presum(helpertree, l-1);
            int sum2=(r+1)*presum(tree, r)-presum(helpertree, r);
            return sum2-sum1;
        }
        int presum(int tmptree[],int k){//差分数组的前缀和
            int ans=0;
            for(int i=k+1;i>0;i-=lowbit(i)){
                ans+=tmptree[i-1];
            }return ans;
        }
        int lowbit(int x){
            return x&-x;
        }
    }
    static class RMBIT{//区间最值
        int tree[];
        int n,nums[];
        RMBIT(int ns[]){
            int n=ns.length;
            tree=new int[n+1];
            nums=new int[n+1];
            for(int i=1;i<=n;i++){
                nums[i]=ns[i-1];
                update(i,nums[i]);
            }
        }
        void update(int k,int x){
            while(k<=n){
                tree[k]=x;
                for(int i=1;i<lowbit(k);i<<=1){
                    tree[k]=Math.max(tree[k],tree[k-1]);
                }x+=lowbit(x);
            }
        }
        int rmax(int l,int r){
            int ans=0;
            while(l<=r){
                ans=Math.max(ans,nums[r]);
                r--;
                while(r-l>lowbit(r)){
                    ans=Math.max(ans,tree[r]);
                    r-=lowbit(r);
                }
            }
            return ans;
        }
        int lowbit(int x){return x&-x;}
    }
    static class SegmentTree {// 单点修改，区间查询和 求最值时为O（n）
        int nums[], tree[], n;
    
        SegmentTree(int[] ns) {
            nums = ns;
            n = ns.length;
            tree = new int[4 * n];
    //		build(0, n - 1, 1);
        }
    
        void build(int s, int t, int i) {
            if (s == t) {
                tree[i] = nums[s];
                return;
            }
            int c = s + (t - s) / 2;
            build(s, c, i * 2);
            build(c + 1, t, i * 2 + 1);
            tree[i] = tree[i * 2] + tree[i * 2 + 1];
        }
    
        void add(int i, int x) {// nums[i]+=x驱动
            add(i, x, 0, n - 1, 1);
        }
    
        void update(int i, int x) {
            add(i, x - nums[i], 0, n - 1, 1);
            nums[i] = x;
        }
    
        int query(int i) {
            return nums[i];
        }
    
        int sum(int l, int r) {
            return sum(l, r, 0, n - 1, 1);
        }
    
        int max(int l, int r) {
            return max(l, r, 0, n - 1, 1);
        }
    
        void add(int id, int x, int s, int t, int i) {
            if (s == t) {
                tree[i] += x;
                return;
            }
            int c = s + (t - s) / 2;
            if (id <= c)
                add(id, x, s, c, i * 2);
            else
                add(id, x, c + 1, t, i * 2 + 1);
            tree[i] =Math.max(tree[i*2],tree[i*2+1]);
        }
    
        int sum(int l, int r, int s, int t, int i) {
            if (l <= s && t <= r)
                return tree[i];
            int c = s + (t - s) / 2, sum = 0;
            if (l <= c)
                sum += sum(l, r, s, c, i * 2); // 递归累加目标区间落在c左侧(含c)的区间和
            if (r > c)
                sum += sum(l, r, c + 1, t, i * 2 + 1); // 递归累加目标区间落在c右侧的区间和
            return sum;
    
        }
    
        int max(int l, int r, int s, int t, int i) {
            if (l <= s && t <= r)
                return tree[i];
            int c = s + (t - s) / 2;
            int lmax = Integer.MIN_VALUE, rmax = Integer.MIN_VALUE;
            if (l <= c)
                lmax = max(l, r, s, c, i * 2);
            if (r > c)
                rmax = max(l, r, c + 1, t, i * 2 + 1);
            return Math.max(lmax, rmax);
        }
    
        int min(int l, int r, int s, int t, int i) {
            if (s == t)
                return tree[s];
            int c = s + (t - s) / 2;
            int lmin = Integer.MAX_VALUE, rmin = Integer.MAX_VALUE;
            if (l <= c)
                lmin = min(l, r, s, c, i * 2);
            if (r > c)
                lmin = min(l, r, s, c + 1, i * 2 + 1);
            return Math.min(lmin, rmin);
        }
    
    }
    static class SegmentTreeBasic {
        int[] nums, treeSum, treeMin, treeMax;// 所有操作都将在log级别完成
        int n;
    
        public SegmentTreeBasic(int[] ns) {
            this.nums = ns;
            this.n = ns.length;
            this.treeSum = new int[4 * n];
            this.treeMin = new int[4 * n];
            this.treeMax = new int[4 * n];
            build(0, n - 1, 1);
        }
    
        public void add(int i, int x) { // 单点修改(驱动): nums[i] += x
            add(i, x, 0, n - 1, 1);
        }
    
        public void update(int i, int x) {// 单点修改(驱动): nums[i] = x
            update(i, x, 0, n - 1, 1);
        }
    
        public int query(int i) { // 单点查询 (驱动): 查询 nums[i]
            return query(i, 0, n - 1, 1);
        }
    
        public int sum(int l, int r) { // 区间查询(驱动): nums[l]~nums[r]之和
            return sum(l, r, 0, n - 1, 1);
        }
    
        public int min(int l, int r) { // 区间查询 (驱动): 查询[l,r]中的最小值
            return min(l, r, 0, n - 1, 1);
        }
    
        public int max(int l, int r) { // 区间查询 (驱动): 查询[l,r]中的最大值
            return max(l, r, 0, n - 1, 1);
        }
    
        // 单点查询 (具体): 查询 nums[i]，尾递归
        private int query(int idx, int s, int t, int i) {
            if (s == t)
                return treeSum[i];
            int c = s + (t - s) / 2;
            if (idx <= c)
                return query(idx, s, c, i * 2);
            else
                return query(idx, c + 1, t, i * 2 + 1);
        }
    
        // 单点修改: nums[idx] += x
        private void add(int idx, int x, int s, int t, int i) {
            if (s == t) {
                treeSum[i] += x; // 增量更新
                treeMin[i] += x; // 增量更新
                treeMax[i] += x; // 增量更新
                return;
            }
            int c = s + (t - s) / 2;
            if (idx <= c)
                add(idx, x, s, c, i * 2);
            else
                add(idx, x, c + 1, t, i * 2 + 1);
            pushUpSum(i);
            pushUpMin(i);
            pushUpMax(i);
        }
    
        // 单点修改: nums[idx] = x
        private void update(int idx, int x, int s, int t, int i) {
            if (s == t) {
                treeSum[i] = x; // 覆盖更新
                treeMin[i] = x; // 覆盖更新
                treeMax[i] = x; // 覆盖更新
                return;
            }
            int c = s + (t - s) / 2;
            if (idx <= c)
                update(idx, x, s, c, i * 2);
            else
                update(idx, x, c + 1, t, i * 2 + 1);
            pushUpSum(i);
            pushUpMin(i);
            pushUpMax(i);
        }
    
        // 区间查询: nums[l]~nums[r]之和
        private int sum(int l, int r, int s, int t, int i) {
            if (l <= s && t <= r)
                return treeSum[i];
            int c = s + (t - s) / 2, sum = 0;
            if (l <= c)
                sum += sum(l, r, s, c, i * 2); // 递归累加目标区间落在c左侧(含c)的区间和
            if (r > c)
                sum += sum(l, r, c + 1, t, i * 2 + 1); // 递归累加目标区间落在c右侧的区间和
            return sum;
        }
    
        // 区间查询: 查询[l,r]中的最小值
        private int min(int l, int r, int s, int t, int i) {
            if (l <= s && t <= r)
                return treeMin[i];
            int c = s + (t - s) / 2, lmin = Integer.MAX_VALUE, rmin = Integer.MAX_VALUE;
            if (l <= c)
                lmin = min(l, r, s, c, i * 2);
            if (r > c)
                rmin = min(l, r, c + 1, t, i * 2 + 1);
            return Math.min(lmin, rmin);
        }
    
        // 区间查询: 查询[l,r]中的最大值
        private int max(int l, int r, int s, int t, int i) {
            if (l <= s && t <= r)
                return treeMax[i];
            int c = s + (t - s) / 2, lmax = Integer.MIN_VALUE, rmax = Integer.MIN_VALUE;
            if (l <= c)
                lmax = max(l, r, s, c, i * 2);
            if (r > c)
                rmax = max(l, r, c + 1, t, i * 2 + 1);
            return Math.max(lmax, rmax);
        }
    
        // 构建线段树(tree数组)
        private void build(int s, int t, int i) {
            if (s == t) { // s: start,nums当前区间起点下标，t: terminal,nums当前结点区间末尾下标
                treeSum[i] = nums[s];
                treeMin[i] = nums[s];
                treeMax[i] = nums[s];
                return;
            }
            int c = s + (t - s) / 2;
            build(s, c, i * 2);
            build(c + 1, t, i * 2 + 1);
            pushUpSum(i);
            pushUpMin(i);
            pushUpMax(i);
        }
    
        // pushUpSum: 更新 treeSum[i]
        private void pushUpSum(int i) {
            treeSum[i] = treeSum[i * 2] + treeSum[i * 2 + 1];
        }
    
        // pushUpMin: 更新 treeMin[i]
        private void pushUpMin(int i) {
            treeMin[i] = Math.min(treeMin[i * 2], treeMin[i * 2 + 1]);
        }
    
        // pushUpMax: 更新 treeMax[i]
        private void pushUpMax(int i) {
            treeMax[i] = Math.max(treeMax[i * 2], treeMax[i * 2 + 1]);
        }
    }
    
    


    //快读用 flush
    static class ac extends PrintWriter{
        StringTokenizer st;
        BufferedReader br;
        ac(){this(System.in,System.out);}
        ac(InputStream i,OutputStream o){
            super(o);br=new BufferedReader(new InputStreamReader(i));
        }
        String next(){
            try{while(st==null||!st.hasMoreTokens())st=new StringTokenizer(br.readLine());
            return st.nextToken();}catch (Exception e){}
            return null;
        }
        int nextint(){return Integer.parseInt(next());}
        long nextlong(){return Long.parseLong(next());}
    }
}
