package com.example.test.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 文字生成水印图片并45度倾斜铺满图片
 */
public class WatermarkImgUtils {

//    public static void main(String[] args) throws IOException {
//        System.out.println("开始水印：");
//        //new WatermarkImgUtils().addWatermark("d:/ttt1.jpg", "d:/ttt2.jpg", "你好，世界！", "jpg");
//        new WatermarkImgUtils().addWaterImg2("d:/ttt1.png", "d:/ttt3.png", "d:/x1.png", "jpg");
//        System.out.println("水印完成。");
//    }

    /**
     * @description
     * @param sourceImgPath 源图片路径
     * @param tarImgPath 保存的图片路径
     * @param waterMarkContent 水印内容
     * @param fileExt 图片格式
     * @return void
     */
    public void addWatermark(String sourceImgPath, String tarImgPath, String waterMarkContent,String fileExt){
        Font font = new Font("宋体", Font.BOLD, 36);//水印字体，大小
        Color markContentColor = Color.red;//水印颜色
        Integer degree = 45;//设置水印文字的旋转角度
        float alpha = 0.5f;//设置水印透明度
        OutputStream outImgStream = null;
        try {
            File srcImgFile = new File(sourceImgPath);//得到文件
            Image srcImg = ImageIO.read(srcImgFile);//文件转化为图片
            int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);//获取图片的高
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();//得到画笔
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            g.setColor(markContentColor); //设置水印颜色
            g.setFont(font);              //设置字体
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));//设置水印文字透明度
            if (null != degree) {
                g.rotate(Math.toRadians(degree));//设置水印旋转
            }
            JLabel label = new JLabel(waterMarkContent);
            FontMetrics metrics = label.getFontMetrics(font);
            int width = metrics.stringWidth(label.getText());//文字水印的宽
            int rowsNumber = srcImgHeight/width;// 图片的高  除以  文字水印的宽    ——> 打印的行数(以文字水印的宽为间隔)
            int columnsNumber = srcImgWidth/width;//图片的宽 除以 文字水印的宽   ——> 每行打印的列数(以文字水印的宽为间隔)
            //防止图片太小而文字水印太长，所以至少打印一次
            if(rowsNumber < 1){
                rowsNumber = 1;
            }
            if(columnsNumber < 1){
                columnsNumber = 1;
            }
            for(int j=0;j<rowsNumber;j++){
                for(int i=0;i<columnsNumber;i++){
                    g.drawString(waterMarkContent, i*width + j*width, -i*width + j*width);//画出水印,并设置水印位置
                }
            }
            g.dispose();// 释放资源
            // 输出图片
            outImgStream = new FileOutputStream(tarImgPath);
            ImageIO.write(bufImg, fileExt, outImgStream);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        } finally{
            try {
                if(outImgStream != null){
                    outImgStream.flush();
                    outImgStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getMessage();
            }
        }
    }

    public void addWaterImg(String sourceImgPath, String tarImgPath, String waterMarkImgPath,String fileExt) throws IOException {
        File markImgFile = new File(waterMarkImgPath);//得到文件
        Image markImg = ImageIO.read(markImgFile);
        //Font font = new Font("宋体", Font.BOLD, 36);//水印字体，大小
        //Color markContentColor = Color.red;//水印颜色
        Integer degree = 0;//设置水印文字的旋转角度
        float alpha = 1f;//设置水印透明度
        OutputStream outImgStream = null;
        try {
            File srcImgFile = new File(sourceImgPath);//得到文件
            Image srcImg = ImageIO.read(srcImgFile);//文件转化为图片
            int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);//获取图片的高
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();//得到画笔
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            //g.setColor(markContentColor); //设置水印颜色
            //g.setFont(font);              //设置字体
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));//设置水印文字透明度
            if (null != degree) {
                g.rotate(Math.toRadians(degree));//设置水印旋转
            }
//            JLabel label = new JLabel(markImg.);
//            FontMetrics metrics = label.getFontMetrics(font);
            int width = markImg.getWidth(null);//metrics.stringWidth(label.getText());//文字水印的宽
            int rowsNumber = srcImgHeight/width;// 图片的高  除以  文字水印的宽    ——> 打印的行数(以文字水印的宽为间隔)
            int columnsNumber = srcImgWidth/width;//图片的宽 除以 文字水印的宽   ——> 每行打印的列数(以文字水印的宽为间隔)
            //防止图片太小而文字水印太长，所以至少打印一次
            if(rowsNumber < 1){
                rowsNumber = 1;
            }
            if(columnsNumber < 1){
                columnsNumber = 1;
            }
            for(int j=0;j<rowsNumber;j++){
                for(int i=0;i<columnsNumber;i++){
                    int x = i*width + j*width;
                    int y = -i*width + j*width;
                    System.out.println("x:"+x+",y:"+y);
                    g.drawImage(markImg, x, y,null);
                    if(i==2){
                        break;
                    }
                }
            }
            g.dispose();// 释放资源
            // 输出图片
            outImgStream = new FileOutputStream(tarImgPath);
            ImageIO.write(bufImg, fileExt, outImgStream);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        } finally{
            try {
                if(outImgStream != null){
                    outImgStream.flush();
                    outImgStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getMessage();
            }
        }
    }

    public void addWaterImg2(String sourceImgPath, String tarImgPath, String waterMarkImgPath,String fileExt) throws IOException {
        File markImgFile = new File(waterMarkImgPath);//得到文件
        Image markImg = ImageIO.read(markImgFile);
        //Font font = new Font("宋体", Font.BOLD, 36);//水印字体，大小
        //Color markContentColor = Color.red;//水印颜色
        Integer degree = 0;//设置水印文字的旋转角度
        float alpha = 1f;//设置水印透明度
        OutputStream outImgStream = null;
        try {
            File srcImgFile = new File(sourceImgPath);//得到文件
            Image srcImg = ImageIO.read(srcImgFile);//文件转化为图片
            int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);//获取图片的高
            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();//得到画笔
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            //g.setColor(markContentColor); //设置水印颜色
            //g.setFont(font);              //设置字体
//            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));//设置水印文字透明度
//            if (null != degree) {
//                g.rotate(Math.toRadians(degree));//设置水印旋转
//            }
//            JLabel label = new JLabel(markImg.);
//            FontMetrics metrics = label.getFontMetrics(font);
//            int width = markImg.getWidth(null);//metrics.stringWidth(label.getText());//文字水印的宽
//            int rowsNumber = 1;// 图片的高  除以  文字水印的宽    ——> 打印的行数(以文字水印的宽为间隔)
//            int columnsNumber = 1;//图片的宽 除以 文字水印的宽   ——> 每行打印的列数(以文字水印的宽为间隔)
            int x1 = markImg.getWidth(null);
            int y1 = markImg.getHeight(null);
            g.drawImage(markImg, 797, 1581,x1,y1,null);
            g.dispose();// 释放资源
            // 输出图片
            outImgStream = new FileOutputStream(tarImgPath);
            ImageIO.write(bufImg, fileExt, outImgStream);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        } finally{
            try {
                if(outImgStream != null){
                    outImgStream.flush();
                    outImgStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getMessage();
            }
        }
    }
}
