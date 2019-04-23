package com.hxd.search.hashlTable;

import com.hxd.base.Queue;

/**
 * 	用大小为M的数组保存N个键值对,其中M>N,需要依靠数组中的空位解决碰撞冲突,称之为开放地址散列表.最简单的方法叫做线性探测法
 *  The {@code LinearProbingHashST} class represents a symbol table of generic
 *  key-value pairs.
 *  It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods.
 *  It also provides a <em>keys</em> method for iterating over all of the keys.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be {@code null}—setting the
 *  value associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  This implementation uses a linear probing hash table. It requires that
 *  the key type overrides the {@code equals()} and {@code hashCode()} methods.
 *  The expected time per <em>put</em>, <em>contains</em>, or <em>remove</em>
 *  operation is constant, subject to the uniform hashing assumption.
 *  The <em>size</em>, and <em>is-empty</em> operations take constant time.
 *  Construction takes constant time.
 *  <p>
 *  线性探测法:当碰撞发生时(当一个键的散列值已经被另一个不同的键占用).直接检查散列表中的下一个位置(将索引值加1)这样的
 *  线性探测可能会产生三种结果: 命中(该位置的键和被查找的相同);未命中(键为空,该位置没有键);继续查找(该位置的键和
 *  被查找的键不同);
 *  <p>
 *  开放地址的散列表的核心思想是与其将内存用作链表使用不如将它们作为散列表的空元素.这些空元素可以作为查找结束的标志.实现
 *  中使用了并行数组,一条保存键,一条保存值,并使用散列函数产生访问数据所需的数组索引.开放地址类的散列表的性能也依赖于a=
 *  N/M的比值,但意义不同将a称之为散列表的使用率.对于拉链法的散列表a是每条链表的长度,因此一般大于1;对于基于线性探测的
 *  散列表,a是表中已被占用的空间的比例,它是不可能大于1的.为了保证性能,将动态调整数组的大小来保证使用率在1/8到1/2之间
 *  <p>
 *  线性探测的平均成本取决于元素在插入数组后聚集成一组连续的条目,也叫做键簇.例如插入C键会产生一个长度为3的键簇(ACS),此
 *  时插入H键需要探测4次,因为H的散列值为该键簇的第一个位置.显然,短小的键簇才能保证较高的效率
 *  <p>
 * 	@author 候旭东 20170215
 * */

public class LinearProbingHashST<Key, Value> {
	private static final int INIT_CAPACITY =  4;
	
	private int n;				//符号表中键值对的总数
	private int m;				//线性探测表的大小
	private Key[] keys;			//键
	private Value[] values;		//值
	
	public LinearProbingHashST() {
		this(INIT_CAPACITY);
	}

	@SuppressWarnings("unchecked")
	public LinearProbingHashST(int capacity) {
		m = capacity;
		n = 0;
		keys = (Key[]) new Object[m];
		values = (Value[]) new Object[m];
	}
	
	public int size() {
		return n;
	}
	
	public boolean isEmpty() {
		return size() == 0;
	}
	
	public boolean contains(Key key) {
		if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
	}
	
	private int hash(Key key) {
		return (key.hashCode() & 0x7fffffff) % m;
	}
	
	private void resize(int capacity) {
		LinearProbingHashST<Key, Value> temp = new LinearProbingHashST<Key,Value>(capacity);
		for (int i = 0; i < m; i++) {
			if (keys[i] != null) {
				temp.put(keys[i],values[i]);
			}
		}
		keys = temp.keys;
		values = temp.values;
		m = temp.m;
	}
	
	public void put(Key key, Value value) {
		if (key == null) throw new IllegalArgumentException("first argument to put() is null");
		
		if (value == null){
			delete(key);
			return;
		}
		
		if (n >= m/2) resize(2*m);
		
		int i;
		for (i = hash(key); keys[i] != null; i=(i + 1) % m) {
			if(keys[i].equals(key)) {
				values[i] = value;
				return;
			}
		}
		keys[i] = key;
		values[i] = value;
		n++;
	}
	
	public Value get(Key key) {
		if (key == null) throw new IllegalArgumentException("argument to get() is null");
		for (int i = hash(key); keys[i] != null; i=(i+1) % m)
			if(keys[i].equals(key))
				return values[i];
		return null;
	}
	/**
	 * 直接将该键所在的位置设为null是不行的,因为这使得在此位置之后的元素无法被查找,因此需要将被删除键的右侧的所有键重新插入散列表
	 * 
	 * */
	public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        if (!contains(key)) return;
        
        int i = hash(key);
        while (!key.equals(keys[i]))
        	i = (i + 1) % m;
        keys[i] = null;
        values[i] = null;
        
        i = (i + 1) % m;
        while (keys[i] != null) {
        	Key keyToRehash = keys[i];
        	Value valueToTehash = values[i];
        	keys[i] = null;
        	values[i] = null;
        	n--;
        	put(keyToRehash,valueToTehash);
        	i = (i + 1) % m;
        }
        n--;
        
        if (n > 0 && n <= m/8) resize(m/2);
        
        assert check();
	}
	
	public Iterable<Key> keys() {
		Queue<Key> queue = new Queue<Key>();
		for(int i = 0; i < m; i++)
			if(keys[i] != null)
				queue.enqueue(keys[i]);
		return queue;
	}
	
	// integrity check - don't check after each put() because
    // integrity not maintained during a delete()
    private boolean check() {

        // check that hash table is at most 50% full
        if (m < 2*n) {
            System.err.println("Hash table size m = " + m + "; array size n = " + n);
            return false;
        }

        // check that each key in table can be found by get()
        for (int i = 0; i < m; i++) {
            if (keys[i] == null) continue;
            else if (get(keys[i]) != values[i]) {
                System.err.println("get[" + keys[i] + "] = " + get(keys[i]) + "; vals[i] = " + values[i]);
                return false;
            }
        }
        return true;
    }
    
    public static void main(String[] args) {
		LinearProbingHashST<String, Integer> st = new LinearProbingHashST<String,Integer>();
		String[] strings = new String[]{"S","E","A","R","C","H","E","X","A","M","P","L","E"};
		int i = 0;
		for(String string : strings)
			st.put(string, i++);
		for(String s : st.keys()) 
			System.out.println(s + " " + st.get(s)); 
		System.out.println(st.size());
	}
}
