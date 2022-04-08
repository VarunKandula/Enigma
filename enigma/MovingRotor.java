package enigma;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Varun Kandula
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean reflecting() {
        return false;
    }

    @Override
    void advance() {
        set(setting() + 1);
    }

    @Override
    boolean atNotch() {
        int currSetting = permutation().wrap(setting());
        char settingAtChar = alphabet().toChar(currSetting);
        if (_notches.indexOf(settingAtChar) >= 0) {
            return true;
        }
        return false;
    }

    @Override
    String notches() {
        return _notches;
    }

    /**private string variable that contains the
     * respective notches for the moving rotor. */
    private String _notches;
}
