/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package imagesteganographyrle;

/**
 *
 * @author ADMIN
 */
public class Converter {
    public static String textToBinary(String message){
        StringBuilder result = new StringBuilder("");
        for(int i=0; i<message.length(); i++){
            int ascii = (int)message.charAt(i);
            StringBuilder binaryChar = new StringBuilder(Integer.toBinaryString(ascii));
            if(binaryChar.length() < 16){
                int countdown = 16 - binaryChar.length();
                while(countdown-- > 0){
                    binaryChar.insert(0, "0");
                }
            }
            result.append(binaryChar.toString());
        }
        
        return result.toString();
    }
    
    public static String binaryToText(String binaryMessage){
        StringBuilder message = new StringBuilder("");
        int len = binaryMessage.length();
        for(int i = 0; i<len ; i+=16){
            String part = binaryMessage.substring(i, Math.min(len, i+16));
            //System.out.print(part);
            char val = (char)(Integer.parseInt(part, 2));
            if(val == '\0') break;
            //System.out.println(" -> " + val);
            message.append(val);
        }
        
        return message.toString();
    }
}
