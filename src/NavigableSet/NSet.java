package NavigableSet;

import java.util.*;

/**
 * Implementation of NavigableSet
 * @author Dana Muksinova
 * @param <T> the type of elements maintained by this set
 */
public class NSet <T> extends AbstractSet<T> implements NavigableSet<T> {
    private ArrayList<T> data;
    private Comparator<T> comparator;

    public NSet(Comparator c){
        data = new ArrayList<>();
        this.comparator = c;
    }

    public NSet(Comparator c, Collection<T> collection){
        this.comparator = c;
        Iterator it = collection.iterator();
        while(it.hasNext()){
            data.add((T)it.next());
        }
        data.sort(comparator);
    }

    /**
     * Adds the specified element to this set if it is not already present.
     * If this set already contains the element, the call leaves the set unchanged and returns false.
     * @param t element to be added to this set
     * @return true if this set did not already contain the specified element
     */
    public boolean add(T t){
        if (isEmpty()){
            data.add(t);
            return true;
        }
        for (int i = 0; i < size(); i++) {
            if (comparator.compare(t, data.get(i)) == 0){
                return false;
            }
            if (comparator.compare(t, data.get(i)) < 0){
                data.add(i, t);
                return true;
            }
        }
        data.add(size(), t);
        return true;
    }

    /**
     * Returns the greatest element in this set strictly less than the given element,
     * or null if there is no such element.
     * @param t the value to match
     * @return the greatest element less than t, or null if there is no such element
     */
    @Override
    public T lower(T t) {
        if (size() == 0){
            return null;
        }
        Iterator it = iterator();
        T previous = null;
        T current;
        while (it.hasNext()) {
            current = (T) it.next();
            if (!(comparator.compare(current, t) < 0)) {
                return previous;
            }
            previous = current;
        }
        return null;
    }

    /**
     * Returns the greatest element in this set less than or equal to the given element,
     * or null if there is no such element.
     * @param t the value to match
     * @return the greatest element less than or equal to t, or null if there is no such element
     */
    @Override
    public T floor(T t) {
        if (size() == 0){
            return null;
        }
        Iterator it = iterator();
        T previous = null;
        T current;
        while (it.hasNext()){
            current = (T) it.next();
            if (comparator.compare(current,t) > 0){
                return previous;
            }
            previous = current;
        }
        return null;
    }

    /**
     * Returns the least element in this set greater than or equal to the given element,
     * or null if there is no such element.
     * @param t the value to match
     * @return the least element greater than or equal to t, or null if there is no such element
     */
    @Override
    public T ceiling(T t) {
        if (size()==0){
            return null;
        }
        Iterator it = descendingIterator();
        T previous = null;
        T current;
        while (it.hasNext()){
            current = (T) it.next();
            if (comparator.compare(current, t) < 0){
                return previous;
            }
            previous = current;
        }
        return null;
    }

    /**
     * Returns the least element in this set strictly greater than the given element,
     * or null if there is no such element.
     * @param t the value to match
     * @return the least element greater than t, or null if there is no such element
     */
    @Override
    public T higher(T t) {
        if (size()==0){
            return null;
        }
        Iterator it = descendingIterator();
        T previous = null;
        T current;
        while (it.hasNext()){
            current = (T) it.next();
            if (!(comparator.compare(current, t) > 0)){
                return previous;
            }
            previous = current;
        }
        return null;
    }

    /**
     * Retrieves and removes the first (lowest) element, or returns null if this set is empty.
     * @return the first element, or null if this set is empty
     */
    @Override
    public T pollFirst() {
        if (size() == 0){
            return null;
        }
        Iterator it = iterator();
        if (it.hasNext()){
            T first = (T)it.next();
            data.remove(0);
            return first;
        }
        return null;
    }

    /**
     * Retrieves and removes the last (highest) element, or returns null if this set is empty.
     * @return the last element, or null if this set is empty
     */
    @Override
    public T pollLast() {
        if (size() == 0){
            return null;
        }
        T last = data.get(size() - 1);
        data.remove(size() - 1);
        return last;
    }

    /**
     * Returns a reverse order view of the elements contained in this set.
     * @return a reverse order view of this set
     */
    @Override
    public NavigableSet<T> descendingSet() {
        Iterator it = descendingIterator();
        Collection<T> col = null;
        while (it.hasNext()){
            col.add((T)it.next());
        }
        NavigableSet<T> desSet = new NSet<T>(comparator, col);
        return desSet;
    }

    /**
     * Returns an iterator over the elements in this set, in descending order.
     * @return an iterator over the elements in this set, in descending order
     */
    @Override
    public Iterator<T> descendingIterator() {
        return new DescendingInnerIterator(data);
    }

    /**
     * Returns a view of the portion of this set whose elements range from fromElement to toElement.
     * If fromElement and toElement are equal, the returned set is empty unless
     * fromInclusive and toInclusive are both true. The returned set is backed by this set,
     * so changes in the returned set are reflected in this set, and vice-versa.
     * The returned set supports all optional set operations that this set supports.
     * @param fromElement low endpoint of the returned set
     * @param fromInclusive true if the low endpoint is to be included in the returned view
     * @param toElement high endpoint of the returned set
     * @param toInclusive true if the high endpoint is to be included in the returned view
     * @return a view of the portion of this set whose elements range from fromElement,
     * inclusive, to toElement, exclusive
     * @throws IllegalArgumentException if fromElement is greater than toElement;
     * or if this set itself has a restricted range, and fromElement or toElement lies outside the bounds of the range.
     * @throws NoSuchElementException if Set does not contains fromElement or toElement
     */
    @Override
    public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
        if ((comparator.compare(fromElement,toElement) > 0) || (comparator().compare(fromElement, first()) < 0)
                ||(comparator.compare(toElement, last())) > 0){
            throw new IllegalArgumentException();
        }
        if (!(data.contains(fromElement)) || !(data.contains(toElement))){
            throw new NoSuchElementException();
        }
        NSet<T> subNSet = new NSet<T>(comparator);
        if ((comparator.compare(fromElement, toElement) == 0) && !((fromInclusive) && (toInclusive))) {
            return subNSet;
        }
        int from;
        int to;
        if (fromInclusive){
            from = data.indexOf(fromElement);
        }
        else {
            from = data.indexOf(fromElement) + 1;
        }
        if (toInclusive){
            to = data.indexOf(toElement);
        }
        else {
            to = data.indexOf(toElement) - 1;
        }
        for (int i = from; i <= to; i++) {
            subNSet.add(data.get(i));
        }
        return subNSet;
    }

    /**
     * Returns a view of the portion of this set whose elements are less than (or equal to, if inclusive is true)
     * toElement. The returned set is backed by this set, so changes in the returned set are reflected in this set,
     * and vice-versa. The returned set supports all optional set operations that this set supports.
     * @param toElement high endpoint of the returned set
     * @param inclusive true if the high endpoint is to be included in the returned view
     * @return a view of the portion of this set whose elements are less than
     * (or equal to, if inclusive is true) toElement
     * @throws IllegalArgumentException if this set itself has a restricted range,
     * and toElement lies outside the bounds of the range
     * @throws NoSuchElementException if Set does not contains toElement
     */
    @Override
    public NavigableSet<T> headSet(T toElement, boolean inclusive) {
        if ((comparator.compare(toElement, first()) < 0) || (comparator.compare(toElement, last()) > 0)){
            throw new IllegalArgumentException();
        }
        if (!(data.contains(toElement))){
            throw new NoSuchElementException();
        }
        NSet<T> headNSet = new NSet<T>(comparator);
        int from = 0;
        int to;
        if (inclusive){
            to = data.indexOf(toElement);
        }
        else{
            to = data.indexOf(toElement) - 1;
        }
        for (int i = from; i <= to ; i++) {
            headNSet.add(data.get(i));
        }
        return null;
    }

    /**
     * Returns a view of the portion of this set whose elements are greater than (or equal to, if inclusive is true)
     * fromElement. The returned set is backed by this set, so changes in the returned set are reflected in this set,
     * and vice-versa. The returned set supports all optional set operations that this set supports.
     * @param fromElement low endpoint of the returned set
     * @param inclusive true if the low endpoint is to be included in the returned view
     * @return a view of the portion of this set whose elements are greater than or equal to fromElement
     * @throws IllegalArgumentException if this set itself has a restricted range,
     * and fromElement lies outside the bounds of the range
     * @throws NoSuchElementException if Set does not contains fromElement
     */
    @Override
    public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
        if ((comparator.compare(fromElement, first()) < 0) || (comparator.compare(fromElement, last()) > 0)){
            throw new IllegalArgumentException();
        }
        if (!(data.contains(fromElement))){
            throw new NoSuchElementException();
        }
        NSet<T> tailNSet = new NSet<T>(comparator);
        int from;
        int to = size() - 1;
        if (inclusive){
            from = data.indexOf(fromElement);
        }
        else{
            from = data.indexOf(fromElement) + 1;
        }
        for (int i = from; i <= to ; i++) {
            tailNSet.add(data.get(i));
        }
        return tailNSet;
    }

    /**
     * Returns the comparator used to order the elements in this set,
     * or null if this set uses the natural ordering of its elements.
     * @return the comparator used to order the elements in this set,
     * or null if this set uses the natural ordering of its elements
     */
    @Override
    public Comparator<? super T> comparator() {
        return this.comparator;
    }

    /**
     * Returns a view of the portion of this set whose elements range from fromElement, inclusive,
     * to toElement, exclusive. (If fromElement and toElement are equal, the returned set is empty.)
     * The returned set is backed by this set, so changes in the returned set are reflected in this set,
     * and vice-versa. The returned set supports all optional set operations that this set supports.
     * @param fromElement low endpoint (inclusive) of the returned set
     * @param toElement high endpoint (exclusive) of the returned set
     * @return a view of the portion of this set whose elements range
     * from fromElement, inclusive, to toElement, exclusive
     * @throws IllegalArgumentException if fromElement is greater than toElement;
     * or if this set itself has a restricted range, and fromElement or toElement lies outside the bounds of the range
     * @throws NoSuchElementException if Set does not contains fromElement or toElement
     */
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        if ((comparator.compare(fromElement,toElement) > 0) || (comparator().compare(fromElement, first()) < 0)
                ||(comparator.compare(toElement, last())) > 0){
            throw new IllegalArgumentException();
        }
        if (!(data.contains(fromElement)) || !(data.contains(toElement))){
            throw new NoSuchElementException();
        }
        NSet<T> subNSet = new NSet<T>(comparator);
        if (comparator.compare(fromElement, toElement) == 0){
            return subNSet;
        }
        int from = data.indexOf(fromElement);
        int to = data.indexOf(toElement) - 1;
        for (int i = from; i <= to; i++) {
            subNSet.add(data.get(i));
        }
        return subNSet;
    }

    /**
     * Returns a view of the portion of this set whose elements are strictly less than toElement.
     * The returned set is backed by this set, so changes in the returned set are reflected in this set,
     * and vice-versa. The returned set supports all optional set operations that this set supports.
     * @param toElement high endpoint (exclusive) of the returned set
     * @return a view of the portion of this set whose elements are strictly less than toElement
     * @throws IllegalArgumentException if this set itself has a restricted range,
     * and toElement lies outside the bounds of the range
     * @throws NoSuchElementException if Set does not contains toElement
     */
    @Override
    public SortedSet<T> headSet(T toElement) {
        if ((comparator.compare(toElement, first()) < 0) || (comparator.compare(toElement, last()) > 0)){
            throw new IllegalArgumentException();
        }
        return headSet(toElement, false);
    }

    /**
     * Returns a view of the portion of this set whose elements are greater than or equal to fromElement.
     * The returned set is backed by this set, so changes in the returned set are reflected in this set, and vice-versa.
     * The returned set supports all optional set operations that this set supports.
     * @param fromElement low endpoint (inclusive) of the returned set
     * @return a view of the portion of this set whose elements are greater than or equal to fromElement
     * @throws IllegalArgumentException if this set itself has a restricted range,
     * and fromElement lies outside the bounds of the range
     * @throws NoSuchElementException if Set does not contains fromElement
     */
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        if ((comparator.compare(fromElement, first()) < 0) || (comparator.compare(fromElement, last()) > 0)){
            throw new IllegalArgumentException();
        }
        return tailSet(fromElement, true);
    }

    /**
     * Returns the first (lowest) element currently in this set.
     * @return the first (lowest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public T first() {
        if (size() == 0){
            throw new NoSuchElementException();
        }
        return data.get(0);
    }

    /**
     * Returns the last (highest) element currently in this set.
     * @return the last (highest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    @Override
    public T last() {
        if (size() == 0){
            throw new NoSuchElementException();
        }
        return data.get(size() - 1);
    }

    /**
     * Returns an iterator over the elements in this set, in ascending order.
     * @return an iterator over the elements in this set, in ascending order
     */
    @Override
    public Iterator<T> iterator(){
        return new InnerIterator(data);
    }

    /**
     * Returns the number of elements in this set (its cardinality).
     * @return the number of elements in this set (its cardinality)
     */
    @Override
    public int size() {
        return data.size();
    }

    /**
     * Returns true if this set contains no elements.
     * @return true if this set contains no elements
     */
    public boolean isEmpty(){
        return size() == 0;
    }

    /**
     * Inner class implementing class Iterator. Uses ascending order.
     */
    private class InnerIterator implements Iterator<T> {
        protected int cursor;
        protected ArrayList<T> data;

        protected InnerIterator(ArrayList<T> data) {
            this.data = data;
            this.cursor = 0;
        }

        @Override
        public boolean hasNext() {
            return cursor < data.size();
        }

        @Override
        public T next() {
            if (hasNext()) {
                return data.get(cursor++);
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * Inner class that uses descending order. Implements Iterator class.
     */
    private class DescendingInnerIterator implements Iterator<T>{
        protected int curcor;
        protected ArrayList<T> data;

        protected DescendingInnerIterator(ArrayList<T> data){
            this.data = data;
            this.curcor = size() - 1;
        }
        @Override
        public boolean hasNext() {
            return curcor > 0;
        }

        @Override
        public T next() {
            if (hasNext()){
                return data.get(curcor--);
            }
            else{
                throw new NoSuchElementException();
            }
        }
    }

    /**
     *Compares the specified object with this set for equality. Returns true if the specified object is also a set,
     * the two sets have the same size, and every member of the specified set is contained in this set
     * (or equivalently, every member of this set is contained in the specified set).
     * @param o object to be compared for equality with this set
     * @return true if the specified object is equal to this set
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NSet)){
            return false;
        }
        NSet<T> nSet2 = (NSet) o;
        if (size() != nSet2.size()){
            return false;
        }
        Iterator it = iterator();
        Iterator it2 = nSet2.iterator();
        while (it.hasNext()){
            if (it.next() != it2.next()){
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the hash code value for this set.
     * @return the hash code value for this set
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), data);
    }
}
