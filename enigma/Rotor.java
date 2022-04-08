package enigma;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Varun Kandula
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _notch = new String();
        _setting = 0;
        _eachSetting = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = _permutation.wrap(posn);
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        _setting = alphabet().toInt(cposn);
    }
    /** Set setting() to POSN.  */

//    void setStellung(int posn) {
//        _eachSetting = _permutation.wrap(posn);
//    }

    int eachSetting() {
        return _eachSetting;
    }

    /** Set setting() to character POSN. */
    void setStellung(char posn) {
        _eachSetting = alphabet().toInt(posn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int enter = permutation().wrap(p + setting() - eachSetting());
        int changeOne = _permutation.permute(enter);
        int result = permutation().wrap(changeOne - setting() + eachSetting());
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(result));
        }
        return result;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int enter = permutation().wrap(e + setting() - eachSetting());
        int changeOne = _permutation.invert(enter);
        int result = permutation().wrap(changeOne - setting() + eachSetting());
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(result));
        }
        return result;
    }

    /** Returns the positions of the notches, as a string giving the letters
     *  on the ring at which they occur. */
    String notches() {
        return "";
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        if (notches().contains(_notch)
                && alphabet().toChar(_setting) == _notch.charAt(0)) {
            return true;
        }
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** Name of the rotor. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /**Setting for the rotor in integer form. */
    private int _setting;

    /**Adjusted Setting for each rotor. */
    private int _eachSetting;

    /**Notch for the specific rotor. */
    private String _notch;
}
