package com.vote.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;

public class ImageUtils {



    /**
     * 通过像素对比来计算偏差值
     *
     * @param path1 原图位置
     * @param path2 滑块图位置
     * @return 偏差值
     */
    public static int getDifferenceValue(String path1, String path2) {
        int result = 0;
        File file = new File(path1);
        File file1 = new File(path2);

        try {
            BufferedImage image = ImageIO.read(file);
            BufferedImage image1 = ImageIO.read(file1);

            int width = image.getWidth();
            int height = image.getHeight();

            int[][] colors = new int[width][height];
            for (int x = 1; x < width; x++) {
                for (int y = 1; y < height; y++) {
                    int color1 = image.getRGB(x, y);
                    int color2 = image1.getRGB(x, y);
                    Math.abs(color1-color2);
                    if (color1==color2) {
                        colors[x - 1][y - 1] = 0;
                    } else {
                        colors[x - 1][y - 1] = 1;
                    }
                }
            }
            int min = 999;
            int max = -1;
            for (int x = 0; x < colors.length; x++) {
                for (int y = 0; y < colors[x].length; y++) {
                    if (colors[x][y] == 1) {
                        colors[x][y] = checkPixel(x, y, colors);
                        if (colors[x][y] == 1) {
                            if (x > max) {
                                max = x;
                            } else if (x < min) {
                                min = x;
                            }
                        }
                    }
                }
            }
            result = (max + min) / 2;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int checkPixel(int x, int y, int[][] colors) {
        int result = colors[x][y];
        int num = 0;
        if ((y + 30) < colors[x].length) {
            for (int i = 1; i <= 30; i++) {
                int color = colors[x][y + i];
                if (color == 0) {
                    num += 1;
                }
            }
            if (num > 15) {
                return 0;
            }
        }
        return result;
    }


    public static void createImage(int width, int height, int ints[][], String name) throws IOException {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphic = bi.createGraphics();
        graphic.setColor(new Color(0x003D1CFF));
        graphic.fillRect(0, 0, width, height);
        for (int x = 0; x < ints.length; x++) {
            for (int y = 0; y < ints[x].length; y++) {
                if (ints[x][y] == 1) {
                    bi.setRGB(x, y, 0xFF7F2E);
                }
            }
        }
        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("png");
        ImageWriter writer = it.next();
        File f = new File("c://" + name + ".png");
        ImageOutputStream ios = ImageIO.createImageOutputStream(f);
        writer.setOutput(ios);
        writer.write(bi);
    }


    /**
     * 部分截图（元素截图）
     * 有时候需要元素的截图，不需要整个截图
     * @throws Exception
     */
    public static File elementSnapshot(WebElement element) throws Exception {
        //创建全屏截图
        WrapsDriver wrapsDriver = (WrapsDriver)element;
        File screen = ((TakesScreenshot)wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
        BufferedImage image = ImageIO.read(screen);
        //获取元素的高度、宽度
        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();

        //创建一个矩形使用上面的高度，和宽度
        Rectangle rect = new Rectangle(width, height);
        //元素坐标
        Point p = element.getLocation();
        BufferedImage img = image.getSubimage(p.getX(), p.getY(), rect.width, rect.height);
        ImageIO.write(img, "png", screen);
        return screen;
    }


    /**
     *
     * @param srcPath 原图片路径
     * @param desPath  转换大小后图片路径
     * @param width   转换后图片宽度
     * @param height  转换后图片高度
     * @throws IOException
     */
    public static File resizeImage(File srcFile,
                                   int width, int height) throws IOException {

        Image srcImg = ImageIO.read(srcFile);
        BufferedImage buffImg = null;
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //使用TYPE_INT_RGB修改的图片会变色
        buffImg.getGraphics().drawImage(
                srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,
                0, null);
        String path = System.getProperty("user.dir");

        File file=new File(path+"/jpg"+srcFile.getName());
        ImageIO.write(buffImg, "JPEG",file );
        System.out.println(file.getAbsolutePath());
        srcFile.delete();
        return file;
    }



    /**
     * 通过像素对比来计算偏差值(自己测试)
     *
     * @param path1 原图位置
     * @param path2 滑块图位置
     * @return 偏差值
     * @throws IOException
     */
    public static int getDifferenceValueSelf(String path1, String path2) throws IOException {
        int result = 0;
        File file = new File(path1);
        File file1 = new File(path2);
        Integer m= null;
        Integer n=null;
        BufferedImage image = ImageIO.read(file);
        BufferedImage image1 = ImageIO.read(file1);

        int width = image.getWidth();
        int height = image.getHeight();



        int[][] colors = new int[width][height];
        for (int y = 1; y < height; y++) {

            System.out.println("------------"+y+"--------------y轴");
            for (int x = 1; x < width; x++) {

                int color1 = image.getRGB(x, y);
                int color2 = image1.getRGB(x, y);

                int bit=0;
                if(color1>color2){
                    bit=color1-color2;
                }else{
                    bit=color2-color1;
                }

                if (bit<150) {
                    colors[x - 1][y - 1] = 0;
                } else {
                    colors[x - 1][y - 1] = 1;
                    if(m==null){
                        m=x;
                    }
                }

                int color3 = image.getRGB(width-x, y);
                int color4 = image1.getRGB(width-x,y);
                int bit1=0;
                if(color3>color4){
                    bit1=color3-color4;
                }else{
                    bit1=color4-color3;
                }
                if (bit1<150) {
                    colors[width-x][y - 1] = 0;
                } else {
                    colors[width-x][y - 1] = 1;
                    if(n==null){
                        n=width-x;
                    }
                }
            }
            if(m!=null&&n!=null){

                System.out.println("------------"+m+"--------------"+n);
                return n-m;
            }
        }



        return height;

    }



    /**
     * 通过像素对比来计算偏差值
     *
     * @param path1 原图位置
     * @param path2 滑块图位置
     * @return 偏差值
     */
    public static int getDifferenceValue2(String path1, String path2) {
        int result = 0;
        File file = new File(path1);
        File file1 = new File(path2);

        try {
            BufferedImage image = ImageIO.read(file);
            BufferedImage image1 = ImageIO.read(file1);

            int width = image.getWidth();
            int height = image.getHeight();

            int[][] colors = new int[width][height];
            for (int x = 1; x < width; x++) {
                for (int y = 1; y < height; y++) {
                    int color1 = image.getRGB(x, y);
                    int color2 = image1.getRGB(x, y);

                    if (Math.abs(color1-color2)<40) {
                        colors[x - 1][y - 1] = 0;
                    } else {
                        colors[x - 1][y - 1] = 1;
                    }
                }
            }
            int min = 999;
            int max = -1;
            for (int x = 0; x < colors.length; x++) {
                for (int y = 0; y < colors[x].length; y++) {
                    if (colors[x][y] == 1) {
                        colors[x][y] = checkPixel(x, y, colors);
                        if (colors[x][y] == 1) {
                            if (x > max) {
                                max = x;
                            } else if (x < min) {
                                min = x;
                            }
                        }
                    }
                }
            }
            result = (max + min) / 2;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
