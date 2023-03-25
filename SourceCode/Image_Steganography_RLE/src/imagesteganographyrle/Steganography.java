/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package imagesteganographyrle;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author ADMIN
 */
public class Steganography {
    private static final int t= 5;
    public static int caculateLenOfMessage(int[][] pixel){
        int count = 0;
        for(int y=0; y<pixel.length; y++){
            for(int x=0; x<pixel[y].length-1; x++){
                //tinh run-length ai
                int runlength = 1;
                while(x < pixel[y].length-2 && pixel[y][x] == (pixel[y][x+1])){
                    runlength++;
                    x++;
                }
                
                //int back = x;
                x++;
                //tinh run-length ai+1
                int runlength2 = 1;
                while(x < pixel[y].length-2 && pixel[y][x] == (pixel[y][x+1])){
                    runlength2++;
                    x++;
                }
                //int skip = x;
                //x = back;
                
                if(runlength >= t && runlength2 >= t){
                    count++;
                }
            }
        }
        return count/16;
    }
    
    
    
    public static int[][] embedInMatrix(int[][] pixel, String binaryMessage){
        int continu = 1;
        for(int y=0; y<pixel.length; y++){
            for(int x=0; x<pixel[y].length-1; x++){
                if(binaryMessage.length() <= 0){
                    continu = 0;
                    break;
                }
                //lay bit message
                char m = binaryMessage.charAt(0);
                
                //tinh run-length ai
                int runlength = 1;
                while(x < pixel[y].length-2 && pixel[y][x] == (pixel[y][x+1])){
                    runlength++;
                    x++;
                }
                
                int back = x;
                x++;
                //tinh run-length ai+1
                int runlength2 = 1;
                while(x < pixel[y].length-2 && pixel[y][x] == (pixel[y][x+1])){
                    runlength2++;
                    x++;
                }
                int skip = x;
                x = back;
                
                if(runlength >= t && runlength2 >= t){
                    if(m == '0'){
                        if(runlength%2 == 0){
                            binaryMessage = binaryMessage.substring(1);
                            //System.out.println("run1: " + runlength + " run2: " + runlength2 + " -> giau bit: " + m);
                        }else{
                            //rl la le dich chuyen ai+1 sang trai 1 pixel
                            pixel[y][x] = pixel[y][x+1];
                            runlength--;
                            x--;
                            binaryMessage = binaryMessage.substring(1);
                            //System.out.println("run1: " + runlength + " run2: " + runlength2 + " -> giau bit: " + m);
                        }
                    }else if(m == '1'){
                        if(runlength%2 != 0){
                            binaryMessage = binaryMessage.substring(1);
                            //System.out.println("run1: " + runlength + " run2: " + runlength2 + " -> giau bit: " + m);
                        }else{
                            //rl la chan, dich chuyen ai+1 sang phai
                            pixel[y][x+1] = pixel[y][x];
                            runlength++;
                            x++;
                            binaryMessage = binaryMessage.substring(1);
                            //System.out.println("run1: " + runlength + " run2: " + runlength2 + " -> giau bit: " + m);
                        }
                    }
                }else if(runlength == t-1){
                    pixel[y][x] = pixel[y][x+1];
                    x--;
                    runlength--;
                }else if(runlength2 == t-1){
                    pixel[y][x+1] = pixel[y][x];
                    x++;
                    runlength2--;
                }
                x = skip;
            }
            int lastx = pixel[y].length-1;
            if(continu == 1){
                pixel[y][lastx] = ((pixel[y][lastx] >> 1) << 1) | 1;
            }else{
                pixel[y][lastx] = ((pixel[y][lastx] >> 1) << 1);
            }
        }
        
        return pixel;
    }
    
    static BufferedImage CloneImage(BufferedImage orgImage){
        ColorModel colorModel = orgImage.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = orgImage.copyData(null);
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }
    
    public static int[][] getPixelMatrix(BufferedImage Image, int width, int height){
        int[][] pixel = new int[height][width];
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                pixel[y][x] = Image.getRGB(x, y);
            }
        }
        
        return pixel;
    }
    
    public static BufferedImage Embed(BufferedImage orgImage, String message){
        //clone image
        BufferedImage cloneImage = CloneImage(orgImage);
        int width = cloneImage.getWidth();
        int height = cloneImage.getHeight();
        //get array pixel
        int[][] pixel = getPixelMatrix(cloneImage, width, height);

        //giau tin trong matrix pixel
        String binaryMessage = Converter.textToBinary(message);
        pixel = embedInMatrix(pixel, binaryMessage);
        
        
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                cloneImage.setRGB(x, y, pixel[y][x]);
            }
        }
        
        return cloneImage;
    }
    
    public static String Extract(BufferedImage steganoImage){
        //get pixel matrix
        int[][] pixel = getPixelMatrix(steganoImage, steganoImage.getWidth(), steganoImage.getHeight());
        
        //trich xuat thong tin
        StringBuilder bits = new StringBuilder("");
        for(int y = 0; y < pixel.length; y++){
            for(int x = 0; x < pixel[y].length-1; x++){
                int runlength = 1;
                while(x < pixel[y].length-2 && pixel[y][x] == pixel[y][x+1]){
                    runlength++;
                    x++;
                }
                
                //int back = x;
                x++;
                //tinh run-length ai+1
                int runlength2 = 1;
                while(x < pixel[y].length-2 && pixel[y][x] == (pixel[y][x+1])){
                    runlength2++;
                    x++;
                }
//                int skip = x;
//                x = back;

                if(runlength >= t-1 && runlength2 >= t-1){
                    if(runlength%2==0){
                        bits.append("0");
                        //System.out.println("run1: " + runlength + " run2: " + runlength2);
                    }else{
                        bits.append("1");
                    }
                }
            }
            int lastx = pixel[y].length-1;
            int lastBit = pixel[y][lastx] << 31;
            if(lastBit == 0) break;
        }
        
        String result = Converter.binaryToText(bits.toString());
        return result;
    }
}
