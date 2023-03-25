/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package imagesteganographyrle;

import java.util.ArrayList;

/**
 *
 * @author ADMIN
 */
public class AffineCipher {
    
    private static int gcd(int n1, int n2) {
        int gcd = 1;
        for (int i = 1; i <= n1 && i <= n2; i++) {
            if (n1 % i == 0 && n2 % i == 0) {
                gcd = i;
            }
        }
        return gcd;
    }
    
    private static ArrayList<Integer> array_a = new ArrayList<>();
    private static void cacula_array_a(){
        for(int i = 0; i < 8363; i++){
            if(gcd(i, 8363) == 1){
                //System.out.println(i);
                array_a.add(i);
            }
        }
    }
    
    private static ArrayList<String> genListKeys(String password){
        cacula_array_a();
        ArrayList<String> keys = new ArrayList<>();
        int a, b, pad = 0;
        for(int i=0; i<password.length(); i++){
            int ascii = (int)password.charAt(i);
            pad += ascii;
        }
        
        for(int i=0; i<password.length(); i++){
            int ascii = (int)password.charAt(i);
            a =  array_a.get((ascii + pad) % (array_a.size()));
            b = (ascii + pad) % 8363;
            keys.add(a+","+b);
        }
        
        return keys;
    }
    
    public static String encrypt(String plaintext, String password){
        ArrayList<String> keys = genListKeys(password);
        int a, b, indexOfk = 0;
        StringBuilder ciphertext = new StringBuilder("");
        for(int i = 0; i < plaintext.length(); i++){
            String[] k = keys.get(indexOfk).split(",");
            a = Integer.valueOf(k[0]);
            b = Integer.valueOf(k[1]);
            
            char cipher = (char) ((a*(int)plaintext.charAt(i) + b)%8363);
            ciphertext.append(cipher);
            indexOfk++;
            if(indexOfk >= keys.size()){
                indexOfk = 0;
            }
        }
        
        return ciphertext.toString();
    }
    
    public static String decrypt(String ciphertext, String password){
        ArrayList<String> keys = genListKeys(password);
        int a, a_inv = 0, b, indexOfk = 0;
        StringBuilder plaintext = new StringBuilder("");
        for(int i=0; i<ciphertext.length(); i++){
            String[] k = keys.get(indexOfk).split(",");
            a = Integer.valueOf(k[0]);
            b = Integer.valueOf(k[1]);
            for(int j=1; j<8363; j++){
                if((a * j)%8363 == 1){
                    a_inv = j;
                    
                    break;
                }
            }
            
            char plain = (char) (Math.floorMod(a_inv * ((int)ciphertext.charAt(i) - b), 8363));
            plaintext.append(plain);
            indexOfk++;
            if(indexOfk >= keys.size()){
                indexOfk = 0;
            }
        }
        
        
        return plaintext.toString();
    }
}
