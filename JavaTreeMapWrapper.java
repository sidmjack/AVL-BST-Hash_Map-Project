import java.util.TreeMap;
import java.util.Map;
import java.util.Set;

/**
 * Wrapper for Java TreeMap.
 * @param <K> Key.
 * @param <V> Value.
 */
public class JavaTreeMapWrapper<K, V> extends TreeMap<K, V>
    implements MapJHU<K, V> {

    /**
     * Constructor for Java TreeMap.
     */
    public JavaTreeMapWrapper() {
        super();
    }

    /**
     *  Find out if a key is in the map.
     *  @param key the key being searched for
     *  @return true if found, false otherwise
     */
    public boolean hasKey(K key) {
        return this.get(key) != null;
    }

    /**
     *  Find out if a value is in the map.
     *  @param value the value to search for
     *  @return true if found, false otherwise
     */
    public boolean hasValue(V value) {
        return this.values().contains(value);
    }

    /** Get a set of all the keys in the map.
     *  @return the set
     */
    public Set<K> keys() {
        return this.keySet();
    }

    /** Get a set of all the entries in the map.
     *  @return the set
     */
    public Set<Map.Entry<K, V>> entries() {
        return this.entrySet();
    }

}