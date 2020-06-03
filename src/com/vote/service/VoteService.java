package com.vote.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsDriver;

import com.vote.utils.FileUtils;
import com.vote.utils.ImageTransferUtils;
import com.vote.utils.ImageUtils;

public class VoteService {


	public static void main (String args[]) throws IOException, InterruptedException{
		System.setProperty("webdriver.chrome.driver", "D:\\chrome_79.0.3945.16_x64\\chromedriver.exe");// chromedriver服务地址
		WebDriver driver = new ChromeDriver(); // 新建一个WebDriver 的对象，但是new 的是谷歌的驱动
		String url = "https://passport.bilibili.com/login";
		driver.get(url);


		try {
			/**
			 * WebDriver自带了一个智能等待的方法。 dr.manage().timeouts().implicitlyWait(arg0, arg1）；
			 * Arg0：等待的时间长度，int 类型 ； Arg1：等待时间的单位 TimeUnit.SECONDS 一般用秒作为单位。
			 */
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}


		//获取单个元素
		// WebElement element = driver.findElement(By.id("login-username"));

		//输入数据 用户名、密码、不勾选记住我、登录
		WebElement searchBox2=driver.findElement(By.id("login-username"));

		searchBox2.sendKeys("qqka8088");

		WebElement searchBox3=driver.findElement(By.id("login-passwd"));

		searchBox3.sendKeys("qw12345");

		WebElement searchP=driver.findElement(By.className("remember-check"));

		searchP.click();

		//鼠标点击
		driver.findElement(By.className("btn-login")).click();
		Thread.sleep(5000);


		try {
			slice(driver);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url1 = "https://www.bilibili.com/read/cv6257557?from=search";
		driver.get(url1);

		driver.findElement(By.className("btn-login").linkText("B组")).click();


		//退出浏览器
		driver.quit();

	}



	private static void slice(WebDriver driver) throws Exception{
		System.out.println("start-----------");
		Thread.sleep(1000);

		//获取指定元素的截屏

		//调用js语句
		JavascriptExecutor j1 = (JavascriptExecutor) driver;
		String js1 = "var change = document.getElementsByClassName('geetest_canvas_bg');change[0].style = 'display:block;'";
		j1.executeScript(js1);
		Thread.sleep(1000);

		WebElement loginImage=driver.findElement(By.className("geetest_canvas_bg"));
		File screen = loginImage.getScreenshotAs(OutputType.FILE);
		screen=FileUtils.copyFile(screen);
		screen=ImageUtils.resizeImage(screen, 260, 160);

		Thread.sleep(1000);



		//调用js语句
		JavascriptExecutor j = (JavascriptExecutor) driver;
		String js = "var change = document.getElementsByClassName('geetest_canvas_fullbg');change[0].style = 'display:block;'";
		j.executeScript(js);
		Thread.sleep(1000);

		WebElement loginImage2=driver.findElement(By.className("geetest_canvas_fullbg"));
		File screen2 = loginImage2.getScreenshotAs(OutputType.FILE);
		screen2=FileUtils.copyFile(screen2);
		screen2=ImageUtils.resizeImage(screen2, 260, 160);
		Thread.sleep(1000);
		System.out.println(screen.getAbsolutePath());

		int  i=ImageUtils.getDifferenceValue(screen2.getAbsolutePath(), screen.getAbsolutePath());
		System.out.println(i+"-----------");

		List<Integer> list=move(i);
		for(Integer item:list){
			Actions actions = new Actions(driver);
			WebElement slice=driver.findElement(By.className("geetest_slider_button"));
			new Actions(driver).clickAndHold(slice).perform();
			actions.moveToElement(slice, ImageTransferUtils.getValue(screen, screen2)+30, 0).perform();
			actions.release(slice).perform();
			System.out.println("click-----------");
			Thread.sleep(1000);
			if(driver.manage().getCookieNamed("DedeUserID")!=null){
				System.out.println("-------------success--------------");
				break;
			}
			Thread.sleep(2000);

			WebElement error=driver.findElement(By.className("geetest_panel_error_content"));
			if(error!=null&&error.isDisplayed()){
				driver.findElement(By.className("geetest_panel_error_content")).click();
				Thread.sleep(1000);
				System.out.println("test-----------");
				screen.delete();
				screen2.delete();
				slice(driver);
			}

		}
	}




	//人体运动模拟

	private static List<Integer> move(int distance){


		List<Integer> list=new ArrayList();
		int a=1;
		// 当前位移
		int  current = 0;
		// 因为老对不的不准确，所以自行调整一下distance
		distance = distance - 9;
		// 减速阈值 -> 也就是加速到什么位置的时候开始减速
		int  mid = distance * 4 / 5;
		//# 计算间隔
		double  t = 1;

		double  v = 2;

		while( current < distance){
			if( current < mid){
				// 加速度为正2
				a = 1;
			}
			else{
				// 加速度为负3
				a = -2;
			}
			double  v0 = v;
			v = v0 + a * t;
			double move = v0 * t + 1 / 2 * a * t * t;
			current += move;

			System.out.println("------------"+move+"-----------------偏移量");
			list.add((int) move);
		}
		return list;
	}

}
