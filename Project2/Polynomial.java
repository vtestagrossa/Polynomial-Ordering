/* 
 * Vincent Testagrossa
 * Project 2: Polynomial Linked list implementation with Strong and Weak Order checking.
 * 11JUN2022
 *
 * Requirements:
 *
 * Builds a Polynomial object with an internal linked-list format. Each Term of the object is a
 * node in the linked-list (built from an internal static Term class) that points to the next in
 * descending order by exponent. The constructor for the Polynomial takes a String of
 * space-separated numbers and determines by order and parity of the numbers which are the coefficients
 * and which are the exponents. It links them together given all the conditions are correct:
 * 
 * -The order of the terms is descending, based on the exponent
 * -There are only numbers in the string
 * -There are an even amount of tokens
 * 
 * If the coefficient is 0, it will be skipped in the constructor and fail to be added to the Polynomial.
 * 
 * A term is made of two doubles (exponent and coefficient), which
 * are passed to it in the Term's constructor, and a null link. Terms have a getCoefficient and
 * getExponent method that returns a Double representing it. 
 * 
 * Public methods:
 * length: returns the length as an integer, representing the number of terms.
 * toString(): returns a string with the format "#x^# + #x + #" with the terms that contain exponents
 * able to expand out so long as each previous term has a higher exponent. 
 * iterator(): returns an iterator for the polynomial that returns each term in succession.
 * compareTo(): compares one Polynomial to the next, using Strong Order to determine which is
 * greater.
 */

package Project2;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Polynomial implements Comparable<Polynomial>, Iterable<Polynomial.Term> {

    private Term head, previous, current;
    private int length = 0;

    /*
     * Attempts to split the string into tokens separated by a space and add each node to the polynomial with the 
     * proper coefficient and exponent in each field.
     *
     * If any of the following conditions exist, throws InvalidPolynomialSyntax:
     * -A token is not able to be converted to a double
     * -Exponents are listed in any order but descending.
     * -The supplied string does not contain pairs of coefficients and exponents all separated by spaces.
     */
    public Polynomial (String input) {
        String[] tokens = input.split(" ");
        if (tokens.length <= 0 || tokens.length % 2 != 0){
            //Terms weren't in coefficient-exponent pairs, or spacing was incorrect.
            String message = "Invalid syntax. Each term must consist of a numeric coefficient and a numeric exponent, separated by spaces";
            throw new InvalidPolynomialSyntax(message);
        }
        Double coeff, expnt;
        for (int i = 0; i < tokens.length - 1; i++) {
            if (i % 2 == 0) {
                //Tokens are paired with a coefficient followed by the exponent, so every i%2 will have a pair at i and i+1.
                //add a new node using the add method with parsed Doubles, which can throw an invalidFormat exception which
                //can be passed to InvalidPolynomialSyntax exception.
                try {
                    coeff = Double.parseDouble(tokens[i]);
                    expnt = Double.parseDouble(tokens[i+1]);
                    //coefficient of 0 is skipped.
                    if (coeff == 0) {
                        continue;
                    }
                    current = new Term(coeff, expnt);
                    if (ordered(current, previous)){
                        add(current);
                        length++;
                        if (i + 2 < tokens.length){
                            //only sets the previous term to reference the current if it's not the last term.
                            previous = current;                            
                        }
                    }
                    else {
                        String message = "Exponents were not in descending order.";
                        throw new InvalidPolynomialSyntax(message);
                    }
                }
                catch (NumberFormatException e)
                {
                    //non-numeric value was found. Throw a syntax error.
                    String message = "Polynomials must be numeric.";
                    throw new InvalidPolynomialSyntax(message);
                }
            }
        }
        if (length == 0)
        {
            String message = "Polynomial only contained terms with coefficients of 0\n" +
                            "or was empty.";
            throw new InvalidPolynomialSyntax(message);
        }
        
    }
    /*
     * A two data field node object that is used to build a linked list for the polynomial object
     */
    static class Term{
        private Double coefficient, exponent;
        private Term next;

        public Term (Double coefficient, Double exponent) {
            //constructor for the two field node
            this.coefficient = coefficient;
            this.exponent = exponent;
            this.next = null;
        }
        public Double getCoefficient() {
            return coefficient;
        }
        public Double getExponent() {
            return exponent;
        }
    }

    private void add(Term current) {
        //searches for the tail of the list and adds the next term.
        if (head == null) {
            //sets the head's reference to the current term
            head = current;
        }
        else {
            //since the head isn't null, searh through the links to find the end.
            previous = head;
            while(previous.next != null) {
                //increments the link of the previous term until it's the tail
                previous = previous.next;
            }
            //current term is added to the first null link in the chain.
            previous.next = current;
        }
    }

    
    /*
     * Checks if the exponents of the term are in strictly descending order.
     */
    private boolean ordered(Term current, Term previous) {
        if (previous != null){
            return current.getExponent() < previous.getExponent(); 
        }
        return true;
    }
    public int length() {
        return this.length;
    }

    /*
     * Uses a foreach loop to format and return a Polynomial String. 
     * -Exponents of 0 drop the variable and exponent from the term
     * -Exponents of 1 drop the exponent from the term
     */
    @Override
    public String toString() {
        String ret = "";
        DecimalFormat decimal = new DecimalFormat("0.##");
        for (Term t : this){
            if (t.getExponent() == 0) {
                //exponent of 0, only add the coefficient
                ret += decimal.format(t.getCoefficient()) + " ";
            }
            else if (t.getExponent() == 1) {
                //exponent of 1, add the coefficient * X
                ret += decimal.format(t.getCoefficient()) + "x ";
            }
            else {
                //coefficient * x ^ exponent
                ret += decimal.format(t.getCoefficient()) + "x^" + decimal.format(t.getExponent()) + " ";
            }
            if (t.next != null) {
                ret += "+ ";
            }
        }
        return ret;
    }

    /*
     * Uses a foreach loop from the Polynomial p to loop through each Term t and compare exponents. If one is greater,
     * set the return value to -1 or 1. If not, then the coefficients are compared. If they're equal, the next term is
     * compared if it exists. If it doesn't exist for one, but does for the other, then the other is greater. If the length
     * of each Polynomial is the same, they are equal. 
     */
    @Override
    public int compareTo(Polynomial p) {
        current = head;
        for (Term t : p) {
            Double e1 = current.getExponent(),
            e2 = t.getExponent(),
            c1 = current.getCoefficient(),
            c2 = t.getCoefficient();
            /*
             * Fall through the if statements to check for inequality on the exponents, then the coefficients. If they're equal,
             * check for the next term being null. If either is null, the other is greater. If both are null, they are equal.
             */
            if (e1 <= e2 && c1 < c2){
                //sorts on strong order because either both the coefficient and exponent of the previous
                //are greater than the next, or the exponents are equal and the prev coeff is greater.
                return -1;
            }
            else if (e1 > e2 || c1 > c2){
                //fails to be sorted on strong order because either the previous exponent or coefficient are greater
                //than the next.
                return 1;
            }
            //previous and next polynomial's current terms are equal. See if the next 2 are null
            else if (current.next == null && t.next == null) {
                //the polynomials are equal
                return 0;
            }
            else if (current.next == null) {
                //the first polynomial doesn't have another term, but the second one does.
                //the second polynomial is greater
                return -1;
            }
            else if (t.next == null) {
                //The first polynomial has another term, but the second doesn't.
                //The first polynomial is greater.
                return 1;
            }
            //both Polynomials are equal thus far, but the next terms have to be checked.
            current = current.next;
        }
        //something went wrong and they can't be sorted.
        return 0;
    }

    /*
     * Creates an iterator Object that returns the next "Term" node, which is an object that holds the Coefficient and
     * the Exponent of the term.
     */
    @Override
    public Iterator<Term> iterator() {
        return new Iterator<Term>() {
            Term current = head;
            @Override
            public boolean hasNext() {
                //If the current term is null, there is no next.
                return current != null;
            }

            @Override
            public Term next() {
                //If there is no other term in the polynomial, raise the exception.
                if (!hasNext()){
                    String message = "Iterator has reached the end of the list";
                    throw new NoSuchElementException(message);
                }
                //Return the term in the polynomial and set the next node as current.
                Term result = current;
                current = current.next;
                return result;
            }            
        };
    }
}
