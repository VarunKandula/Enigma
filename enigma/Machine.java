package enigma;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;



/** Class that represents a complete enigma machine.
 *  @author Varun Kandula
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _objectRotors = allRotors.toArray();
        _rotors = new HashMap<String, Rotor>();

        for (Object each: _objectRotors) {
            Rotor temp = (Rotor) each;
            _rotors.put(temp.name(), temp);
        }

    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Return Rotor #K, where Rotor #0 is the reflector, and Rotor
     *  #(numRotors()-1) is the fast Rotor.  Modifying this Rotor has
     *  undefined results. */
    Rotor getRotor(int k) {
        return _accRotors.get(k);
    }

    Alphabet alphabet() {
        return _alphabet;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _movingRotors = 0;
        _accRotors = new ArrayList<>();


        if (!_rotors.get(rotors[0]).reflecting()) {
            throw new EnigmaException("Left most rotor is not a reflector");
        }
        for (int i = 0; i < rotors.length; i++) {
            for (int j = i + 1; j < rotors.length; j++) {
                if (rotors[i].equals(rotors[j])) {
                    throw new EnigmaException("Duplicate Rotor Name");
                }
            }
        }

        for (int i = 0; i < rotors.length; i++) {
            Rotor curr = _rotors.get(rotors[i]);

            if (!_rotors.keySet().contains(rotors[i])) {
                throw new EnigmaException("Bad rotor name");
            }

            if (i > 0 && curr.reflecting()) {
                throw new EnigmaException("Reflector is in the wrong spot");
            }

            if (curr.rotates()) {
                _movingRotors += 1;
            }

            _accRotors.add(curr);
        }

        if (_movingRotors != _pawls) {
            throw new EnigmaException("Wrong number of arguments");
        }
    }


    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() > numRotors() - 1) {
            throw new EnigmaException("Wheel settings too long");
        }
        if ((setting.length() < numRotors() - 1)) {
            throw new EnigmaException("Enigma exception too short");
        }

        for (int i = 0; i < setting.length(); i++) {
            char s = setting.charAt(i);
            if (!alphabet().contains(s)) {
                throw new EnigmaException("Character "
                        + "in settings is not in alphabet");
            }
            _accRotors.get(i + 1).set(s);
        }
    }

    /** Return the current plugboard's permutation. */
    Permutation plugboard() {
        return _plugboard.permutation();
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard =
                new FixedRotor("Plugboard", plugboard);
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advanceRotors();
        if (Main.verbose()) {
            System.err.printf("[");
            for (int r = 1; r < numRotors(); r += 1) {
                System.err.printf("%c",
                        alphabet().toChar(getRotor(r).setting()));
            }
            System.err.printf("] %c -> ", alphabet().toChar(c));
        }
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(c));
        }
        c = applyRotors(c);
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c%n", alphabet().toChar(c));
        }
        return c;
    }

    /** Advance all rotors to their next position. */

    private void advanceRotors() {
        boolean[] advRotor1 = new boolean[numRotors()];
        int fastRotor = _accRotors.size() - 1;
        advRotor1[fastRotor] = true;

        for (int i = _accRotors.size() - fastRotor; i < fastRotor; i++) {
            if (getRotor(i + 1).atNotch()) {
                advRotor1[i] = true;
            }
        }
        for (int i = _accRotors.size() - fastRotor + 1; i < fastRotor; i++) {
            if (getRotor(i - 1).rotates() && getRotor(i).atNotch()) {
                advRotor1[i] = true;
            }
        }
        for (int i = 0; i < fastRotor + 1; i++) {
            if (advRotor1[i]) {
                getRotor(i).advance();
            }
        }

    }
    /** Return the result of applying the rotors to the character C (as an
     *  index in the range 0..alphabet size - 1). */
    private int applyRotors(int c) {

        int beforePerm = c;

        for (int i = _accRotors.size() - 1; i >= 0; i--) {
            beforePerm = _accRotors.get(i).
                    convertForward(beforePerm);
        }

        int afterForwardPass = beforePerm;

        for (int i = 1; i < _accRotors.size(); i++) {
            afterForwardPass = _accRotors.get(i).
                    convertBackward(afterForwardPass);
        }

        return afterForwardPass;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String rv = "";
        for (int i = 0; i < msg.length(); i++) {
            char ltr = msg.charAt(i);
            int before = _alphabet.toInt(ltr);
            int after = convert(before);
            rv += _alphabet.toChar(after);
        }
        return rv;
    }

    void setRingstellung(String settings) {
        String originalAlpha = getRotor(1).alphabet().fullAlpha();
//        for (int i = 0; i < settings.length(); i++) {
//            char adj = settings.charAt(i);
//            Rotor curr = getRotor(i + 1);
//            String localAlpha = originalAlpha;
//            String firstHalf = localAlpha.substring(0, originalAlpha.indexOf(adj));
//
//            String secondHalf = localAlpha.substring(originalAlpha.indexOf(adj));
//            curr.alphabet().resetAlpha(secondHalf + firstHalf);
//
//
//        }
        for (int j = 0; j < settings.length(); j++) {
            char currAt = settings.charAt(j);
            getRotor(j + 1).setStellung(currAt);
        }
    }




    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /**TOTAL number of rotors in machine. */
    private int _numRotors;

    /**TOTAL number of pawls in machine. */
    private int _pawls;

    /**Array that stores the object versions of the collection. */
    private Object[] _objectRotors;

    /**hashmap containg all the rotors in the system. */
    private HashMap<String, Rotor> _rotors;

    /**Actual rotors in the specific machine. */
    private ArrayList<Rotor> _accRotors;

    /**plug board for the machine. */
    private FixedRotor _plugboard;

    /**number of moving rotors. */
    private int _movingRotors;


}
