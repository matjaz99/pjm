package si.matjazcerkvenik.pjm.util;

public class Utils {

    /**
     * Return true if string equals null or is empty (length = 0)
     * @param s
     * @return
     */
    public static boolean isNullOrEmpty(String s) {
        if (s == null || s.trim().length() == 0) return true;
        return false;
    }

}
