package enigma;

import java.util.ArrayList;
import java.util.Scanner;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Varun Kandula
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;

        String temp = cycles.trim();
        if (!temp.isEmpty()) {
            if (temp.charAt(temp.length() - 1) != ')') {
                throw new EnigmaException("Bad Permutation");
            }
            if (temp.isEmpty()) {
                throw new EnigmaException("Bad Permutation");
            }
        }

        convertCyclesToArrList();
        addRemainingLetters();
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        cycleSplit.add(cycle);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return alphabet().size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char ltr = alphabet().toChar(wrap(p));
        char converted = permute(ltr);
        int convertedToInt = alphabet().toInt(converted);
        return convertedToInt;

    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char ltr = alphabet().toChar(wrap(c));
        char converted = invert(ltr);
        int convertedToInt = alphabet().toInt(converted);
        return convertedToInt;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {

        if (!alphabet().contains(p)) {
            throw new EnigmaException("The character you want "
                    + "to permute isnt in the alphabet");
        }
        String before = Character.toString(p);
        for (String eachCycle: cycleSplit) {
            if (eachCycle.contains(before)) {
                if (eachCycle.length() == 1) {
                    return eachCycle.charAt(0);
                } else {
                    if (eachCycle.indexOf(before) == eachCycle.length() - 1) {
                        return eachCycle.substring(0, 1).charAt(0);
                    } else {
                        return eachCycle.substring(
                                eachCycle.indexOf(before) + 1,
                                eachCycle.indexOf(before) + 2).charAt(0);
                    }
                }
            }
        }
        return 0;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (!alphabet().contains(c)) {
            throw new EnigmaException("The character you want to permute"
                    + " isnt in the alphabet");
        }

        String before = Character.toString(c);
        for (String eachCycle: cycleSplit) {
            if (eachCycle.contains(before)) {
                if (eachCycle.length() == 1) {
                    return eachCycle.charAt(0);
                } else {
                    if (eachCycle.indexOf(before) == 0) {
                        return eachCycle.substring(
                                eachCycle.length() - 1).charAt(0);
                    } else {
                        return eachCycle.substring(
                                eachCycle.indexOf(before) - 1,
                                eachCycle.indexOf(before)).charAt(0);
                    }
                }
            }
        }
        return 0;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        return true;
    }


    public void convertCyclesToArrList() {
        String addSpace = _cycles.replaceAll("[)(]", " ");
        Scanner in = new Scanner(addSpace);
        while (in.hasNext()) {
            cycleSplit.add(in.next());
        }
    }

    void addRemainingLetters() {
        String alphaCopy = _alphabet.fullAlpha();

        String allCycles = "";
        for (String i: cycleSplit) {
            allCycles += i;
        }

        String removeChars = alphaCopy.replaceAll(allCycles, "");

        for (int i = 0; i < removeChars.length(); i++) {
            cycleSplit.add(String.valueOf(removeChars.charAt(i)));
        }
    }
    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Arraylist of strings containing all of the permutation cycles. */
    private ArrayList<String> cycleSplit = new ArrayList<>();

    /** String containing initial permutation cycles (w/ parentheses). */
    private String _cycles;

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /**
     * @return returns the private cycleSplit arrayList.
     */
    ArrayList<String> getCycleSplit() {
        return cycleSplit;
    }
}
