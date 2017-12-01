/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cheque.test.spoke.tdprep;

/**
 *
 * @author krissada.r
 */
public class padding {
    
    public static String spacePadding(String inputString,int length) {
        //System.out.println(inputString.length());
        //System.out.println(length-inputString.length());
        int outputLength = length-inputString.length();
        StringBuilder outputBuffer = new StringBuilder(outputLength);
        for (int i = 0; i < outputLength; i++){
            outputBuffer.append(" ");
        }
        //System.out.println(outputBuffer.length());
        return inputString+outputBuffer.toString();
    }
    
     public static String leftZeroPadding(long num, int digitSize) {
    //test for capacity being too small.
        //System.out.println("Number: " + num);
        if (digitSize < String.valueOf(num).length()) {
            return "Error : you number  " + num + " is higher than the decimal system specified capacity of " + digitSize + " zeros.";

            //test for capacity will exactly hold the number.
        } else if (digitSize == String.valueOf(num).length()) {
            return String.valueOf(num);

            //else do something here to calculate if the digitSize will over flow the StringBuilder buffer java.lang.OutOfMemoryError 

            //else calculate and return string
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digitSize; i++) {
                sb.append("0");
            }
            sb.append(String.valueOf(num));
            return sb.substring(sb.length() - digitSize, sb.length());
        }
    }
}
