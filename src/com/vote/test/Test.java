package com.vote.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.vote.utils.ImageTransferUtils;
import com.vote.utils.ImageUtils;
import com.vote.utils.Verify;

public class Test {

	public static void main (String args[]) throws Exception{

		String path1="E:/ACM_workspace/picture/1.png";
		String path2="E:/ACM_workspace/picture/2.png";
		//int i=ImageUtils.getDifferenceValueSelf(path2, path1);
		int i=ImageUtils.getDifferenceValue(path1, path2);
		System.out.println(i+"-----------------------");


		File file1=new File(path1);
		File file2=new File(path2);
		BufferedImage image1 = ImageIO.read(file1);
		BufferedImage image2 = ImageIO.read(file2);


		int m=Verify.getTX2(image1, image2);
		System.out.println(m+"-----------------------");

		image1=ImageTransferUtils.img_color_contrast(image1,150);
		image2=ImageTransferUtils.img_color_contrast(image2,150);
		image1=  ImageTransferUtils.grayImage(image1);
		image2=ImageTransferUtils.grayImage(image2);
		File file3 = new File("E:/ACM_workspace/picture/3.png");
		File file4 = new File("E:/ACM_workspace/picture/4.png");

		try {
			ImageIO.write(image1, "png", file3);
			ImageIO.write(image2, "png", file4);
		}catch(Exception e){
			e.getMessage();
		}
		int j=ImageUtils.getDifferenceValue("E:/ACM_workspace/picture/3.png", "E:/ACM_workspace/picture/4.png");
		System.out.println(j+"-----------------------");
	}

}
