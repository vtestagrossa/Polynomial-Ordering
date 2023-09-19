/* 
 * Vincent Testagrossa
 * Project 2: Polynomial Linked list implementation with Strong and Weak Order checking.
 * 11JUN2022
 *
 * Requirements: None.
 *
 * Should be thrown when the terms of a polynomial are not in descending order of exponents, or when 
 * there are not terms which are pairs of numbers separated by spaces, with spaces between the terms.
 */

package Project2;

public class InvalidPolynomialSyntax extends RuntimeException {
    public InvalidPolynomialSyntax(String message){
        super(message);
    }
}
