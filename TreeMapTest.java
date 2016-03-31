/** TESTS for 600.226 Spring 2016 Project 2 - BSTMap implementation
 *  Explicit tests for metrics after inserting and insert caused rehashing
 *  Explicit tests for contents and lookup after inserting, with duplicate vals
 *  Explicit test for linear probing when insert, get, contains
 *  Explicit test for clear, probe functions
 *  Explicit test that put of existing key replaces value, no key changes
 *  Explicit test remove all, remove duplicate values - contents and metrics
 *  Explicit tests of tombstones:
 *    get/insert/probing after remove with tombstones
 *    rehashing when ghosts > size
 * 
 *  Explicit tests of iterator functions
 *  Missing tests: exceptions - you should throw them as specified, we may
 *  add tests for them later.
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.AbstractMap;
import java.util.Set;
import java.util.Collection;
import java.util.TreeMap;

public class TreeMapTest {

    static BSTMap<Integer, String> e4;  // empty map, max load .4
    static BSTMap<Integer, String> e7;  // empty map, max load .7
    static BSTMap<Integer, String> all;  // all in map
    
    // note - Integer hashCode() returns the int value
    static Integer[] iray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    static HashSet<Integer> ikeys;
    static String[] sray = {"zro", "one", "two", "tre", "for", "fyv", "six", "svn", "ate", "nyn", "ten"};
    static String[] sray2 = {"ten", "nyn", "ate", "svn", "six", "fyv", "for", "tre", "two", "one","zro"}; 
    static ArrayList<String> svals;
    static String[] nray = new String[sray.length];
    static ArrayList<String> nvals;
    static HashSet<AbstractMap.SimpleEntry<Integer,String>> entries;
    static AbstractMap.SimpleEntry<Integer,String> pair;


    /** This is a helper template method to see if two collections have the
     *  same contents, but not necessarily in the same order.
     *  @param c1 the first collection
     *  @param orig the other collection
     *  @return true if same values, false otherwise
     */
    public static <T> boolean sameCollection(Collection<T> c1, Collection<T> orig) {
        ArrayList<T> c2 = new ArrayList<T>(orig); // make copy
        if (c1.size() != c2.size())
            return false;
        for (T val : c1) {  // uses iterator
            if (!c2.remove(val))   // so count will be accurate
                return false;
        }
        if (c2.size() != 0)  // should be empty by now
            return false;
        return true;  // passed all tests
    }

    @BeforeClass
    public static void init() {
        ikeys = new HashSet<Integer>();
        for (Integer val: iray) {
            ikeys.add(val);
        }

        svals= new ArrayList<String>();
        for (String val: sray) {
            svals.add(val);
        }

        nvals = new ArrayList<String>(svals.size());
        int idx = 0;
        for (String val: svals) {
            nray[idx] = "N"+val;
            nvals.add(nray[idx]);
            idx++;
        }

        entries = new HashSet<AbstractMap.SimpleEntry<Integer,String>>();
        for (int i=0; i < iray.length; i++) {
            pair = new AbstractMap.SimpleEntry<Integer,String>(iray[i],sray[i]);
            entries.add(pair);
        }
    }

    @Before        
    public void setup() {
        // these start out empty before each test, different load factors
        e4 = new BSTMap<Integer, String>();   // load 2 of 5 is max
        e7 = new BSTMap<Integer, String>();   // load 3 of 5 is max

        // this is full set, assuming put works correctly
        all = new BSTMap<Integer, String>();
        for (int i=0; i < iray.length; i++) {
            all.put(iray[i], sray[i]);
        }
    }


    @Test //ok
    public void testInit() {
        assertEquals(ikeys.toString(), "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]");
        assertEquals(svals.toString(), "[zro, one, two, tre, for, fyv, six, svn, ate, nyn, ten]");
        assertEquals(nvals.toString(), "[Nzro, None, Ntwo, Ntre, Nfor, Nfyv, Nsix, Nsvn, Nate, Nnyn, Nten]");

        // won't necessarily be in this order, so we can't test
        // assertEquals(entries.toString(), "[3=tre, 6=six, 0=zro, 1=one, 10=ten, 5=fyv, 8=ate, 9=nyn, 4=for, 7=svn, 2=two]");
    }


    @Test //ok
    public void testEmptyMap() {
        //testEmptyMetrics(.7f);
        //testEmptyMetrics(.4f);
        assertNull(e4.get(1));
        assertNull(e4.remove(1));
        assertFalse(e4.hasKey(1));
        assertFalse(e4.hasValue("one"));
        assertTrue(e4.keys().isEmpty());
        assertTrue(e4.values().isEmpty());
        assertTrue(e4.entries().isEmpty());
    }

    @Test //ok
    public void testClearEmpty() {
        assertTrue(e7.isEmpty());
        e7.clear();
        assertTrue(e7.isEmpty());
        assertEquals(0, e7.size());
        //assertTrue(.7f == e7.getMaxLoad());
        //assertTrue(0 == e7.getLoad());
        //assertEquals(5, e7.getCapacity());  
        assertTrue(e7.keys().isEmpty());
        assertTrue(e7.values().isEmpty());
        assertTrue(e7.entries().isEmpty());
        for (int i=0; i < iray.length; i++) {
            assertNull(e7.get(iray[i]));
            assertFalse(e7.hasKey(iray[i]));
            assertFalse(e7.hasValue(sray[i]));
        }
    }

    @Test //ok
    public void testClear() {
        assertTrue(e7.isEmpty());
        for (int i=0; i < iray.length; i++) {
            e7.put(iray[i],sray[i]);
        }
        int size = e7.size();
        //int cap = e7.getCapacity();
        assertTrue(size == iray.length);
        //assertTrue(e7.getLoad() == (float) size / cap);
        //assertTrue(e7.getMaxLoad() == .7f);
        e7.clear();
        assertTrue(e7.isEmpty());
        assertEquals(0, e7.size());
        //assertTrue(.7f == e7.getMaxLoad());
        //assertTrue(0 == e7.getLoad());
        //assertEquals(cap, e7.getCapacity());  // don't resize or reshash
        assertTrue(e7.keys().isEmpty());
        assertTrue(e7.values().isEmpty());
        assertTrue(e7.entries().isEmpty());
        for (int i=0; i < iray.length; i++) {
            assertNull(e7.get(iray[i]));
            assertFalse(e7.hasKey(iray[i]));
            assertFalse(e7.hasValue(sray[i]));
        }
    }

    // @Test
    // public void testGetMaxLoad() {
    //     assertTrue(.4f == e4.getMaxLoad());
    //     assertTrue(.7f == e7.getMaxLoad());
    //     e4.put(20,"twenty");
    //     e4.put(30,"thirty");
    //     e7.put(20,"twenty");
    //     e7.put(30,"thirty");
    //     assertTrue(.4f == e4.getMaxLoad());
    //     assertTrue(.7f == e7.getMaxLoad());
    //}

    @Test //ok 
    public void testPutGetHas() {
        assertTrue(e4.isEmpty());
        // very hard to test one without the others
        for (int i=0; i < iray.length; i++) {
            assertNull(e4.put(iray[i],sray[i]));  // no old value
            assertEquals(sray[i], e4.get(iray[i]));
            assertTrue(e4.hasKey(iray[i]));
            assertTrue(e4.hasValue(sray[i]));
        }
        // make sure all are still there
        for (int i=0; i < iray.length; i++) {
            assertEquals(sray[i], e4.get(iray[i]));
            assertTrue(e4.hasKey(iray[i]));
            assertTrue(e4.hasValue(sray[i]));
        }
    }

    @Test //ok
    public void testPutCollections() { //ok
        HashSet<Integer> keys = new HashSet<Integer>();
        ArrayList<String> vals = new ArrayList<String>();
        HashSet<AbstractMap.SimpleEntry<Integer,String>> pairs = new HashSet<AbstractMap.SimpleEntry<Integer,String>>();
        for (int i=0; i < iray.length; i++) {
            assertNull(e7.put(iray[i],sray[i]));  // new key
            keys.add(iray[i]);
            vals.add(sray[i]);
            pairs.add(new AbstractMap.SimpleEntry<Integer,String>(iray[i],sray[i]));
            assertEquals(keys, e7.keys());
            assertTrue(sameCollection(e7.values(),vals));
            for (Entry<Integer, String> p: e7.entries()) {
                assertTrue(pairs.contains(new AbstractMap.SimpleEntry<Integer,String>(p.getKey(), p.getValue()))); 
            }
        }
        // now do duplicate values
        int key;
        for (int i=0; i < iray.length; i++) {
            key = iray[i]; 
            assertNotNull(e7.put(key,sray2[i]));  // overwrite old entries
            keys.add(key);  // no dups
            vals.remove(sray[i]);
            vals.add(sray2[i]);  // yes dups
            pairs.add(new AbstractMap.SimpleEntry<Integer,String>(key,sray2[i])); 
            assertEquals(keys, e7.keys());
            System.out.println(e7.values().toString());
            System.out.println(vals.toString());
            assertTrue(sameCollection(e7.values(),vals));
            for (Entry<Integer, String> p: e7.entries()) {
                assertTrue(pairs.contains(new AbstractMap.SimpleEntry<Integer, String>(p.getKey(), p.getValue())));
            }
        }
        // for (int i=0; i < iray.length; i++) {
        //     assertEquals(sray[i], e7.get(iray[i]));
        //     assertEquals(sray[i], e7.get(iray[i]+20));
        // }
    }

    @Test //ok
    public void testPutOverwrites() {
        int size = all.size();
        assertTrue(size == iray.length);
        Set<Integer> keys = all.keys();
        assertEquals(ikeys,keys);
        for (int i=0; i < iray.length; i++) {
            assertEquals(sray[i], all.put(iray[i],nray[i])); // returns old
            assertEquals(size, all.size());  // no size change
            assertEquals(nray[i], all.get(iray[i]));  // overwritten
            assertFalse(all.hasValue(sray[i]));  // overwritten
            assertTrue(all.hasValue(nray[i]));  // new value
            assertTrue(all.hasKey(iray[i]));  // key still there
        }
        // check again after all overwrites
        assertEquals(size, all.size());  // no size change
        assertEquals(keys, all.keys());  // same keys
        assertEquals(keys.toString(), all.keys().toString()); // same key order
        for (int i=0; i < iray.length; i++) {
            assertEquals(nray[i], all.get(iray[i]));  // overwritten
            assertFalse(all.hasValue(sray[i]));  // overwritten
            assertTrue(all.hasValue(nray[i]));  // new value
            assertTrue(all.hasKey(iray[i]));  // key still there
        }
    }

    /*
    @Test
    public void testEqualsHashcode() {
        e7 = new TreeMap<Integer, String>(.4f);   // same max as e4
        for (int i=0; i < iray.length; i++) {
            e4.put(iray[i],sray[i]);
            e7.put(iray[i],sray[i]);
            assertTrue(e4.equals(e7));
            assertEquals(e7.hashCode(), e4.hashCode());
        }
        for (int i=0; i < iray.length; i++) {
            assertEquals(e4.remove(iray[i]),e7.remove(iray[i]));
            assertTrue(e4.equals(e7));
            assertTrue(e7.hashCode() == e4.hashCode());
        }
    }
    */

    @Test //ok
    public void testMetricsFewAdds() {  // assuming put works
        int size = 0;
        //int cap = 5;
        //float max = .4f;    // .4 * 5 means 2 elements before full
        assertEquals(size, e4.size());
        //assertEquals(cap, e4.getCapacity());
        //assertTrue(max == e4.getMaxLoad());  
        //assertTrue(e4.getLoad() == (float) size / cap);
        //assertTrue(e4.getLoad() <= e4.getMaxLoad());

        e4.put(20,"twenty");  // 1st insert
        size++;
        assertEquals(size, e4.size());
        //assertEquals(cap, e4.getCapacity());
        //assertTrue(max == e4.getMaxLoad());  
        //assertTrue(e4.getLoad() == (float) size / cap);
        //assertTrue(e4.getLoad() <= e4.getMaxLoad());
        
        e4.put(30,"thirty");  // 2nd insert
        size++;
        assertEquals(size, e4.size());
        //assertEquals(cap, e4.getCapacity());
        //assertTrue(max == e4.getMaxLoad());  
        //assertTrue(e4.getLoad() == (float) size / cap);
        //assertTrue(e4.getLoad() <= e4.getMaxLoad());

        // next insert triggers rehash
        // new capacity must be odd
        // new load must be <= maxload / 2
        // oldsize / cap <= maxload / 2
        // oldsize*2/maxload <= cap
        e4.put(40,"fourty");
        size++;  // 3 elements, 3/5 > .4, must rehash
        assertEquals(size, e4.size());
        assertEquals("fourty", e4.get(40));

        //assertTrue(max == e4.getMaxLoad());
        // new capacity puts original 2 elements at or below .2 load
        // new capacity must be >= 2 / .2 = 10
        //cap = e4.getCapacity();
        //assertTrue("load factor rule after rehash new cap is "+cap, (size-1) * 2.0 / max <= cap);  // load factors rule
        //assertTrue(cap % 2 == 1);  // must be odd
        //assertTrue(e4.getLoad() == (float) size / cap);
        //assertTrue(e4.getLoad() <= e4.getMaxLoad());
    }

    // public int addEntriesRehashTest(TreeMap<Integer, String> hm, int offset, int size) {
    //     int oldcap, newcap;
    //     boolean full = false;
    //     float max = hm.getMaxLoad();
    //     oldcap = hm.getCapacity();
    //     for (int i=0; i < iray.length; i++) {
    //         hm.put(iray[i] + offset,sray[i]);
    //         size++; 
    //         assertEquals(size, hm.size());
    //         newcap = hm.getCapacity();
    //         if (full) {
    //             // rehash was triggered before this put
    //             assertTrue(newcap >= 2 * oldcap);
    //             assertTrue((size-1) * 2.0 / max <= newcap);  // load factors rule
    //             full = false;
    //         }
    //         if ((size+1.0) / newcap > max) {
    //             full = true;  // next one causes rehash
    //         }
    //         assertTrue(newcap % 2 == 1);  // must be odd
    //         assertTrue(hm.getLoad() == (float) size / newcap);
    //         assertTrue(hm.getLoad() <= hm.getMaxLoad());
    //         assertTrue(max == hm.getMaxLoad());
    //         oldcap = newcap;
    //     }
    //     return size;
    // }
                           
    // @Test
    // public void testMetricsPutRehash() {
    //     // now test putting 30 things into maxload .7 hashmap
    //     int size = 0;
    //     float max = .7f;
    //     int cap;
    //     boolean full = false;
    //     size = addEntriesRehashTest(e7, 0, size);
    //     size = addEntriesRehashTest(e7, 20, size);
    //     size = addEntriesRehashTest(e7, 50, size);
    //     for (int i=0; i < iray.length; i++) {
    //         e7.put(iray[i],sray[i]);    // duplicate keys not allowed
    //         // replaces values instead
    //         // size doesn't change
    //         assertEquals(size, e7.size());
    //         cap = e7.getCapacity();
    //         assertTrue(cap % 2 == 1);  // must be odd
    //         assertTrue(e7.getLoad() == (float) size / cap);
    //         assertTrue(e7.getLoad() <= e7.getMaxLoad());
    //         assertTrue(max == e7.getMaxLoad());
    //     }
    // }

    @Test // ok
    public void testAfterAddAll() {
        for (Map.Entry<Integer,String> p : entries) {
            e4.put(p.getKey(), p.getValue());
        }
        assertFalse(e4.isEmpty());
        assertTrue(e4.size() == entries.size());
        assertEquals(ikeys, e4.keys());
        assertTrue(sameCollection(svals, e4.values()));
        for(Entry<Integer, String> p: e4.entries()) {
            assertTrue(entries.contains(
                    new AbstractMap.SimpleEntry<Integer,String>(p.getKey(),p.getValue())));
        }
    }

    @Test //ok
    public void testRemoveAll() {
        int size = all.size();
        //int cap = all.getCapacity();
        Set<Integer> keys = all.keys();
        Collection<String> vals = all.values();
        for (int i=0; i < iray.length; i++) {
            assertEquals(sray[i], all.remove(iray[i])); // returns val
            size--;
            assertEquals(size, all.size());  // size decrease
            assertNull(all.get(iray[i]));  // key not there now
            assertFalse(all.hasValue(sray[i]));  // gone
            assertFalse(all.hasKey(iray[i]));  // gone
            keys.remove(iray[i]);
            vals.remove(sray[i]);
            assertEquals(keys, all.keys());
            assertTrue(sameCollection(vals, all.values()));
        }
        assertTrue(all.isEmpty());
        assertTrue(all.keys().isEmpty());
        assertTrue(all.values().isEmpty());
        assertTrue(all.entries().isEmpty());
        assertEquals(0, all.size());
        //assertEquals(cap, all.getCapacity());  // don't resize even if rehash
        //assertTrue(.5f == all.getMaxLoad());
        //assertTrue(0 == all.getLoad());
    }

    @Test //ok
    public void testRemoveDuplicateValues() {
        for (int i=0; i < iray.length; i++) {
            all.put(iray[i]+20,sray[i]);  // duplicate values allowed
        }
        int size = all.size();
        assertTrue(size == iray.length*2);
        //int cap = all.getCapacity();
        //int ghosts = 0;
        //assertEquals(0, all.ghosts());  // no tombstones yet
        Set<Integer> keys = all.keys();
        Collection<String> vals = all.values();  // has duplicates
        for (int i=0; i < iray.length; i++) {
            assertEquals(sray[i],all.remove(iray[i])); // returns val
            size--;
            //ghosts++;
            assertEquals(size, all.size());  // size decrease
            //assertEquals(ghosts, all.ghosts());  // ghost increase
            //assertTrue(all.size() >= all.ghosts());  // more active than inactive
            assertNull(all.get(iray[i]));  // key not there now
            assertFalse(all.hasKey(iray[i]));  // key gone
            assertTrue(all.hasValue(sray[i]));  // val still there
            assertTrue(all.hasKey(iray[i]+20)); // new key there
            keys.remove(iray[i]);
            vals.remove(sray[i]);  // only one copy removed
            assertEquals(keys, all.keys());
            assertTrue(sameCollection(vals, all.values()));
        }
        //assertEquals(cap, all.getCapacity());  // don't resize even if rehash
        //assertTrue(.5f == all.getMaxLoad());   // never changes
        assertTrue(all.size() == iray.length); // half the values there
        //assertTrue(all.size() == all.ghosts()); // removed half
        //assertTrue(all.getLoad() == (float) all.size() / cap);
        for (int i=0; i < iray.length; i++) {
            assertEquals(all.get(iray[i]+20),sray[i]); 
        }
    }

    @Test //ok
    public void testIteratorFullNoCollisions() {
        // start with load factor 1 table
        BSTMap<Integer, String> full = new BSTMap<Integer,String>();
        HashSet<AbstractMap.SimpleEntry<Integer,String>> pairs = new HashSet<AbstractMap.SimpleEntry<Integer,String>>();
        int cap = 5;
        // put 5 entries in to fill
        for (int i=0; i < cap; i++) {
            full.put(i, i+"");
            pairs.add(new AbstractMap.SimpleEntry<Integer,String>(i, i+""));
        }
        for (Map.Entry<Integer, String> p: full.entries()) {
            assertTrue(pairs.contains(new AbstractMap.SimpleEntry<Integer, String>(p.getKey(),p.getValue())));
        }
        Iterator<Map.Entry<Integer, String>> it = full.iterator();
        int count = 0;
        while (it.hasNext()) {
            assertEquals(new AbstractMap.SimpleEntry<Integer,String>(count, count+""), it.next());
            count++;
        }
        assertEquals("iterated through all elements", cap, count);
        assertEquals(cap, full.size());  // everything still there
        for (Entry<Integer, String> p: full.entries()) {
            assertTrue(pairs.contains(new AbstractMap.SimpleEntry<Integer, String>(p.getKey(), p.getValue())));
        }
        // do it all again
        it = full.iterator();
        count = 0;
        while (it.hasNext()) {
            assertEquals(new AbstractMap.SimpleEntry<Integer,String>(count, count+""), it.next());
            count++;
        }
        assertEquals("iterated through all elements", cap, count);
        assertEquals(cap, full.size());  // everything still there
        for (Entry<Integer, String> p: full.entries()) {
            assertTrue(pairs.contains(new AbstractMap.SimpleEntry<Integer, String>(p.getKey(), p.getValue())));
        }
    }

    @Test //ok
    public void testIteratorFullRemove() {
        // start with load factor 1 table
        BSTMap<Integer, String> full = new BSTMap<Integer,String>();
        HashSet<AbstractMap.SimpleEntry<Integer,String>> pairs = new HashSet<AbstractMap.SimpleEntry<Integer,String>>();
        int cap = 5;
        // put 5 entries in to fill
        for (int i=0; i < cap; i++) {
            full.put(i, i+"");
            pairs.add(new AbstractMap.SimpleEntry<Integer,String>(i, i+""));
        }
        for (Entry<Integer, String> p: full.entries()) {
            assertTrue(pairs.contains(new AbstractMap.SimpleEntry<Integer, String>(p.getKey(), p.getValue())));
        }
        Iterator<Map.Entry<Integer, String>> it = full.iterator();
        int count = cap;
        int i = 0;
        System.out.println("\n\n" + full.toString());
        while (it.hasNext()) {
            Iterator<Map.Entry<Integer, String>> it2 = full.iterator();
            while (it2.hasNext()) {
                System.out.print(it2.next() + ", ");
            }
            System.out.println();


            Map.Entry<Integer, String> tmp = it.next();
            System.out.println(tmp);
            // assertEquals(tmp.getKey(),(Integer)i);
            // assertEquals(tmp.getValue(), i + "");
            it.remove();
            System.out.println("\n\n" + full.toString());
            count--;
            pairs.remove(new AbstractMap.SimpleEntry<Integer,String>(i, i+""));
            assertEquals(count, full.size());
            for (Entry<Integer, String> p: full.entries()) {
                assertTrue(pairs.contains(new AbstractMap.SimpleEntry<Integer, String>(p.getKey(), p.getValue())));
            }
            i++;
        }
        System.out.println("\n\n" + full.toString());
        assertEquals("iterated through all elements", 0, count);
        assertEquals("removed all entries with iterator", 0, full.size());
        assertEquals(0, full.entries().size());
        assertEquals(0, full.keys().size());
        assertEquals(0, full.values().size());
        // 5 entries removed, should all be tombstones
        // we don't rehash when removing through iterator
        //it = full.iterator();
        count = 0;
        //AbstractMap.SimpleEntry<Integer, String> e;
        // while (it.hasNext()) {
        //     e = it.next();
        //     assertTrue(e.isTombstone());
        //     count++;
        // }
        // assertEquals(cap, count);
    }

    @Test //ok
    public void testIteratorAllEntryTypes() {
        // using load factor .7 table
        HashSet<Integer> keys = new HashSet<Integer>();
        ArrayList<String> vals = new ArrayList<String>();
        AbstractMap.SimpleEntry<Integer,String>[] entries = new AbstractMap.SimpleEntry[iray.length];
        HashSet<AbstractMap.SimpleEntry<Integer,String>> pairs = new HashSet<AbstractMap.SimpleEntry<Integer,String>>();
        for (int i=0; i < iray.length; i++) {
            assertNull(e7.put(iray[i],sray[i]));  // new key
            keys.add(iray[i]);
            vals.add(sray[i]);
            entries[i] = new AbstractMap.SimpleEntry<Integer,String>(iray[i],sray[i]);
            pairs.add(new AbstractMap.SimpleEntry<Integer,String>(iray[i],sray[i]));
            assertEquals(keys, e7.keys());
            assertTrue(sameCollection(e7.values(),vals));
            for (Entry<Integer, String> p: e7.entries()) {
                assertTrue(pairs.contains(new AbstractMap.SimpleEntry<Integer, String>(p.getKey(), p.getValue())));
            }
        }
        Iterator<Map.Entry<Integer , String>> it = e7.iterator();

        //int cap = e7.getCapacity();
        int size = e7.size();
        //assertTrue(size < cap);  // has empty slots at end
        int count = 0;
        Map.Entry<Integer,String> e;

        while (it.hasNext()) {
            e = it.next();
            if (count < size) {
                assertEquals(entries[count],
                        new AbstractMap.SimpleEntry<Integer,String>(e.getKey(), e.getValue()));
            }
            if (count % 3 == 0) {  // only remove 1/3 of the entries, no rehash
                it.remove();  
                pairs.remove(new AbstractMap.SimpleEntry<Integer,String>(e.getKey(), e.getValue())); 
                keys.remove(e.getKey());
                vals.remove(e.getValue());
            }
            count++;
        }
        //assertEquals(count, cap);  // iterated through all slots

        
        assertEquals(keys,e7.keys());
        assertTrue(sameCollection(vals, e7.values()));
        for (Entry<Integer, String> p: e7.entries()) {
            assertTrue(pairs.contains(new AbstractMap.SimpleEntry<Integer,String>(p.getKey(), p.getValue()))); 
        }
    }

}
   