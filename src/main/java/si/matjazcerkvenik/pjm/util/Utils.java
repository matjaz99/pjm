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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    public static String getFormatedTimestamp(long timestamp, PjmDateFormat format) {
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


    public static XMLGregorianCalendar getXmlGregorianCalendarNow() {
        Date current_date = new Date();
        return dateToGregorianCalendar(current_date);
    }

    public static long getMillis(XMLGregorianCalendar xmlGregorianCalendar) {
        return xmlGregorianCalendar.toGregorianCalendar().getTimeInMillis();
    }

    /**
     * Get days since
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
}
