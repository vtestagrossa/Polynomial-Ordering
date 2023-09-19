/* 
 * Vincent Testagrossa
 * Project 2: Polynomial Linked list implementation with Strong and Weak Order checking.
 * 11JUN2022
 *
 * Requirements: 
 *
 * This class is a helper class for checking whether or not a provided list is sorted. Requires
 * a comparable list to be passed to it for default checking, or a comparable list with a comparator.
 */

package Project2;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public class OrderedList {
    /*
     * Takes a list object and checks whether or not it is in descending order based on the comparator
     * provided. If no comparitor is provided, the method calls the overloaded constructor with a null
     * comparator.
     */
    public static <T extends Comparable<? super T>> boolean checkSorted(List <T> list){
        return checkSorted(list, null);
    }
    public static <T extends Comparable<? super T>> boolean checkSorted(List<T> list, Comparator<? super T> c){
        if (list.isEmpty() || list.size() == 1) {
            return true;
        }
        ListIterator<T> iter = list.listIterator();
        T current, previous = iter.next();
        int d = 0;
        while (iter.hasNext()){
            current = iter.next();
            d = compare(previous, current, c);
            if (d > 0){
                //previous was larger than current, so the list is not in ascending order according to the comparison used.
                return false;
            }
            previous = current;
        }
        //only return true if compare(previous, current, c) resulted in previous being smaller on the final iteration of the loop.
        if (d < 0){
            return true; 
        }
        //They were all equal, and sorting is impossible.
        return false;
    }
    /*
    * Compares two objects with or without a Comparator. If c is null, uses the
    * natural ordering. Borrowed heavily from the Collections source, but modified
    * to fit the needs of this class. In order to enforce type safety, had to include
    * <T extends Comparable<? super T>> as opposed to the <T> that's in binarySearch in
    * the Java.util.Arrays class, which is what the Java.util.Collections.sort methods
    * use. I preferred this over casting a comparator for the compareTo method.
    * 
    * Source: 
    * https://developer.classpath.org/doc/java/util/Collections-source.html
    * lines 619 through 628.
    */
    static final <T extends Comparable<? super T>> int compare(T o1, T o2, Comparator<? super T> c){
        return c == null ? o1.compareTo(o2) : c.compare(o1, o2);
    }
}
