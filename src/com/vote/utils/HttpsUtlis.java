package com.vote.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class HttpsUtlis {


	public static void changeIp(WebDriver driver) throws Exception{
		ChromeOptions options = new ChromeOptions();
		// 设置代理ip
		String ip = "ip:port";
		options.addArguments("--proxy-server=http://" + ip);

		Thread.sleep(50000);

	}
}
