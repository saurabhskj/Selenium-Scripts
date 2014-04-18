package com.shopclues.selenium.myaccount;


import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.*;
/**
 * 
 * @author saurabh jain
 *
 */
public class MyAccountTest {

	private WebElement element = null;
	private WebDriver driver=null;
	private DesiredCapabilities capabilities= null;
    private long starttime=0 ;
    private long endTime=0;

    public void start(){
        this.starttime=System.currentTimeMillis();
    }
    public void end(){
        this.endTime=System.currentTimeMillis();
    }

    public long getStartTime(){
        return this.starttime;
    }
        public long getEndTime(){
        return this.endTime;
    }

    public long timeDelay(){
        return this.endTime-this.starttime;
    }

    private DateFormat df= new SimpleDateFormat("yyyy/MM/dd,HH:mm:ss,");
	private FileWriter fw;
	private String browser="*firefox",port="4444";
	private	String from="seleniumtest@shopclues.com",to="tops@shopclues.com";
	private	String host="localhost";
	private	Properties property=System.getProperties();
	private String results="";
 
	private void open() throws IOException
	{
		fw=new FileWriter("/home/saurabh/logs/res_myaccount.csv",true);
	}

	public void sendMail( String testname) throws AddressException, MessagingException{
	    property.setProperty("mail.smtp.host", host);
	    Session session = Session.getDefaultInstance(property);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO,
                                 new InternetAddress(to));
        
        message.setSubject("My Accounts: Warning issued in Selenium test scripts.");
        message.setText("Test case failure.\n"+testname+"\n");
        Transport.send(message);
        System.out.println("Message sent successfully....");
	}
	
	public void timeDelayCheck(long timedel,boolean flag,boolean isPrivacyPresent,FileWriter fw,String error, String results)throws Exception
	{
		if(timeDelay()>20000L || !flag){
    		if(!flag){
    			fw.write("Failed,"+"Page load error (Exception thrown) \n");
                sendMail("Errors found in " + error+"\n"+results+"\n");
    		}
    		else
    		    fw.write("Passed,"+"with load time limit exceeded\n");
    	}
    	else if(!isPrivacyPresent)
    		fw.write("Passed,"+"Privacy Policy Element may be missing\n");
    	else
    		fw.write("Passed\n");
  		
	}

    @BeforeClass
    
    public void homePage() throws Exception
    {
    	open();
    	boolean flag=true;
    	fw.write("homepage,");
    	results=new String("homepage , ");
    	start();
    	fw.write(df.format(new Date()));
     	results+=df.format(new Date())+" , ";
    	try
    	{
		  String url="http://localhost:".concat(port).concat("/wd/hub");   		
    	  if(browser.equals("*firefox")){
    		  capabilities = DesiredCapabilities.firefox();
    		  capabilities.setPlatform(Platform.LINUX);
    		  driver = new RemoteWebDriver(new URL(url), capabilities);
          //  driver = new FirefoxDriver();
          }
    	  else
    	  {
    		  ///********* to run it on selenium grid and test this on remote machine **********///
    		  /******* run this command in terminal $> java -Dwebdriver.chrome.driver=/home/saurabh/chromedriver  -jar selenium-server-standalone-2.28.0.jar ****
    		  *****/
    		  capabilities = DesiredCapabilities.chrome();
    		  capabilities.setPlatform(Platform.LINUX);
    		  driver = new RemoteWebDriver(new URL(url), capabilities);
    		  
    		  
    		  /********** when runs on local machine it cannot be used in selenium grid *******/
    		  
    		  /*
    		   * 
    		   *  
    		      System.setProperty("webdriver.chrome.driver", "/home/saurabh/chromedriver");
    		      driver = new ChromeDriver();
    		  *
    		  */
    	  }
          String weburl = "http://www.shopclues.com";
    	  driver.get(weburl);
    	  
    	}
    	catch(Exception e)
    	{
    	    flag=false;
    	    e.printStackTrace();
    		System.out.println("Unable to open/find the webpage" );
    	}
    	end();
    	fw.write(df.format(new Date())+timeDelay()+",");
    	results+=df.format(new Date())+" , "+timeDelay();
    	boolean isPrivacyPresent = driver.getPageSource().contains("privacy-policy.html");
    	String error="opening up the home page link in the My Account Test script. ";
    	timeDelayCheck(timeDelay(), flag, isPrivacyPresent, fw, error,results);
        Assert.assertEquals(flag, true);
	
    	/***** checking whether the page completely loaded or not. *********/
 
    	//boolean privacyIspresent = driver.findElement(By.linkText("Privacy policy")).isDisplayed();
    	
    }
    
 // @Parameters({"browser","port"})
    @Test
    public void clickOnSignIn() throws Exception
    {
    	boolean flag=true;
    	fw.write("clickOnSignIn,");
    	results = new String("clickOnSignIn ");

    	start();
    	fw.write(df.format(new Date()));
    	results+=df.format(new Date())+" , ";
    	try{
    	    element=driver.findElement(By.linkText("Sign-In"));
    	    element.click();
    	}catch(Exception e){
    		flag=false;
    		e.printStackTrace();
    		System.out.println("Element not found.");
    	}
    	end();
    	fw.write(df.format(new Date())+timeDelay()+",");
    	results+=df.format(new Date())+" "+timeDelay();
    	boolean isPrivacyPresent = driver.getPageSource().contains("privacy-policy.html");
    	String error="the function named 'click on signIn' in the My Account Test script. ";
    	timeDelayCheck(timeDelay(), flag, isPrivacyPresent, fw, error,results);
        Assert.assertEquals(flag, true);
    }
    
    @Test
    public void signInDet() throws Exception
    {
    	boolean flag=true;
    	fw.write("signInDet,");
    	start();
    	results= new String("signInDet ");
    	fw.write(df.format(new Date()));
    	results+=df.format(new Date())+" , ";
    	try{
    		element=driver.findElement(By.id("email"));
    		element.clear();
    		element.sendKeys("autotest@shopclues.com");
    		element=driver.findElement(By.id("password"));
    		element.clear();
    		element.sendKeys("autotest");
    		
    		driver.findElement(By.name("dispatch[auth.login]")).click();
    	}catch(Exception e){
    		flag=false;
    		System.out.println("emailid/password input box not found.");
    	}
    	end();
    	fw.write(df.format(new Date())+timeDelay()+",");
    	results+=df.format(new Date())+" , "+timeDelay();
     	boolean isPrivacyPresent = driver.getPageSource().contains("privacy-policy.html");
    	String error="opening up the signIn link or entering details in email/password column(s) in the My Account Test script. ";
    	timeDelayCheck(timeDelay(), flag, isPrivacyPresent, fw, error,results);
        Assert.assertEquals(flag, true);
    }
    
    @Test
    public void t1_ordersHistory() throws Exception
    {
    	boolean flag=true;
    	fw.write("orderHistory,");
    	results= new String("orderHistory ");
    	start();
    	
    	fw.write(df.format(new Date()));
    	results+=df.format(new Date())+" , ";
    	try{
    		driver.findElement(By.linkText("Orders History")).click();
    	}catch(Exception e){
    		flag=false;
    		System.out.println("Orders history link/element could not clicked/found.");
            e.printStackTrace();
    	}
    	end();
    	fw.write(df.format(new Date())+timeDelay()+",");
    	results+=df.format(new Date())+" , "+timeDelay();
    	boolean isPrivacyPresent = driver.getPageSource().contains("privacy-policy.html");
    	String error="my orders history link in the My Account Test script. ";
    	timeDelayCheck(timeDelay(), flag, isPrivacyPresent, fw, error,results);
        Assert.assertEquals(flag, true);
    }
    
    @AfterClass
    public void close() throws IOException
    {
    	fw.close();
    	System.out.println(starttime+" "+endTime+" "+timeDelay());
     	driver.close();
      	driver.quit();
    }
    
}