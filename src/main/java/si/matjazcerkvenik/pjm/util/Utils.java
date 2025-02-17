/*
   Copyright 2021 Matjaž Cerkvenik

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package si.matjazcerkvenik.pjm.util;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * Format timestamp from millis into readable form.
     * @param timestamp unix timestamp in millis
     * @return readable date
     */
    public static String getFormattedTimestamp(long timestamp, PjmDateFormat format) {
        if (timestamp == 0) return "n/a";
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat(Props.DATE_FORMAT);
        switch (format){
            case DATE:
                sdf = new SimpleDateFormat("yyyy/MM/dd");
                break;
            case TIME:
                sdf = new SimpleDateFormat("H:mm:ss");
                break;
            case ISO8601:
                // TODO
                break;
        }
        return sdf.format(cal.getTime());
    }

    public static String getFormattedTimestamp(XMLGregorianCalendar calendar, PjmDateFormat format) {
        if (calendar == null) return "n/a";
        long millis = getMillis(calendar);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        SimpleDateFormat sdf = new SimpleDateFormat(Props.DATE_FORMAT);
        switch (format){
            case DATE:
                sdf = new SimpleDateFormat("yyyy/MM/dd");
                break;
            case DATE_SI:
                sdf = new SimpleDateFormat("dd. MMM yyyy");
                break;
            case TIME:
                sdf = new SimpleDateFormat("H:mm:ss");
                break;
            case ISO8601:
                // TODO
                break;
        }
        return sdf.format(cal.getTime());
    }

    /**
     * This will return given number of seconds in format: __d __h __m __s.
     * @param seconds
     * @return formatted time
     */
    public static String convertToDHMSFormat(int seconds) {
        int secRemain = seconds % 60;
        int minTotal = seconds / 60;
        int minRemain = minTotal % 60;
        int hourTotal = minTotal / 60;
        int hourRemain = hourTotal % 60;
        int dayTotal = hourTotal / 24;
        int dayRemain = hourTotal % 24;

        String resp = minRemain + "m " + secRemain + "s";

        if (dayTotal == 0) {
            if (hourRemain > 0) {
                resp = hourTotal + "h " + resp;
            }
        }

        if (dayTotal > 0) {
            resp = dayTotal + "d " + dayRemain + "h " + resp;
        }

        return resp;
    }

    public static String intToRomanNumber(int num) {
        // Arrays containing the Roman numeral equivalents for ones, tens, hundreds, and thousands places
        String[] thousands = {"", "M", "MM", "MMM"};
        String[] hundreds = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] tens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] ones = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        // Extract each digit from the number
        String roman = thousands[num / 1000] +
                hundreds[(num % 1000) / 100] +
                tens[(num % 100) / 10] +
                ones[num % 10];

        return roman;
    }


    public static XMLGregorianCalendar getXmlGregorianCalendarNow() {
        Date current_date = new Date();
        return dateToGregorianCalendar(current_date);
    }

    public static long getMillis(XMLGregorianCalendar xmlGregorianCalendar) {
        return xmlGregorianCalendar.toGregorianCalendar().getTimeInMillis();
    }

    /**
     * Return number of days since given calendar
     * @param xmlGregorianCalendar
     * @return days
     */
    public static int getAgeInDays(XMLGregorianCalendar xmlGregorianCalendar) {
        long age = System.currentTimeMillis() - xmlGregorianCalendar.toGregorianCalendar().getTimeInMillis();
//        System.out.println("age: " + (int) (age / 1000 / 3600 / 24));
        return (int) (age / 1000 / 3600 / 24);
    }

    public static Date gregorianCalendarToDate(XMLGregorianCalendar xmlGregorianCalendar) {
        return xmlGregorianCalendar.toGregorianCalendar().getTime();
    }

    public static XMLGregorianCalendar dateToGregorianCalendar(Date date) {
        XMLGregorianCalendar xmlDate = null;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        try {
            xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlDate;
    }


    /**
     * Return MD5 checksum of a string.
     * @param s
     * @return checksum
     */
    public static String getMd5Checksum(String s) {

        StringBuffer sb = new StringBuffer("");

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] dataBytes = s.getBytes();

            md.update(dataBytes, 0, dataBytes.length);

            byte[] mdbytes = md.digest();

            //convert the byte to hex format
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return sb.toString();

    }

    /**
     * Return only first 8 characters of MD5 checksum
     * @param s
     * @return
     */
    public static String getMd5ChecksumShort(String s) {
        String result = getMd5Checksum(s);
        return result.substring(0, 8);
    }

    /**
     * Same as above, except that it adds a random factor to string to make MD5 more random.
     * @param s
     * @return
     */
    public static String getMd5ChecksumShortSalted(String s) {
        String result = getMd5Checksum(s + System.currentTimeMillis());
        return result.substring(0, 8);
    }



    public static List<String> findHashtags(String text) {
        Map<String, Object> hashtagsMap = new HashMap<>();

        if (Utils.isNullOrEmpty(text)) return new ArrayList<>(hashtagsMap.keySet());

        Pattern pattern = Pattern.compile("#\\w+");
//        Pattern pattern = Pattern.compile("#[^0-9]+[a-zA-Z_]+");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String s = matcher.group();
            if (s.length() < 4) continue;
            if (s.startsWith("#xfeff")) continue;
//            Pattern p1 = Pattern.compile("#\\d+");
//            Matcher m1 = pattern.matcher(text);
//            if (m1.find()) continue;
            hashtagsMap.put(s, null);
        }
        return new ArrayList<>(hashtagsMap.keySet());
    }

    public static void main(String... args) {
        String text = "This is some tekst. #text It's raining on a sunny day. #34, #22aaa, #sunnyDay, #rain. \n in tako naprej #naprej...";
        findHashtags(text);
    }

}
