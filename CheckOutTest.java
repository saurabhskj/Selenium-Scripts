package com.shopclues.selenium.checkout;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.internal.seleniumemulation.IsElementPresent;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
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

public class CheckOutTest {

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
    private long defTimeDelay=20000;
    private String results="";
    
	private void open() throws IOException
	{
		fw=new FileWriter("/home/saurabh/logs/res_checkout.csv",true);
	}
	public void sendMail( String testname) throws AddressException, MessagingException{
	    property.setProperty("mail.smtp.host", host);
	    Session session = Session.getDefaultInstance(property);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO,
                                 new InternetAddress(to));
        
        message.setSubject("Checkout: Warning issued in Selenium test scripts.");
        message.setText("Test case failure.\n"+testname+"\n");
        Transport.send(message);
        System.out.println("Message sent successfully....");
	}
	
	public void timeDelayCheck(long timedel,boolean flag,boolean isPrivacyPresent,FileWriter fw,String error)throws Exception
	{
		if(timeDelay()>defTimeDelay || !flag){
    		if(!flag){
    			fw.write("Failed,"+"Page load error (Exception thrown) \n");
                sendMail("Errors found in opening up the " + error+"\n"+results);
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
    	boolean flag=true;
    	open();
    	this.defTimeDelay =20000;
    	fw.write("homepage,");
    	results = new String("homepage , ");
        start();
    	fw.write(df.format(new Date()));
    	results += df.format(new Date()) +" , ";
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
 $>java -Dwebdriver.chrome.driver=/home/saurabh/chromedriver  -jar selenium-server-stand2.28.0.jar
  
    		      System.setProperty("webdriver.chrome.driver", "/home/saurabh/chromedriver");
    		      driver = new ChromeDriver();
    		  *
    		  */
    	  }
         // String weburl = "https://secure.shopclues.com/login?return_url=index.php";
    	  String weburl="http://shopclues.com/test-product-10.html";
    	  driver.get(weburl);
    	  
    	//  driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    	}
    	catch(Exception e)
    	{
    //		e.printStackTrace();
    	    flag=false;
    		System.out.println("Unable to open/find the webpage" );
    	}
    	end();
    	fw.write(df.format(new Date())+timeDelay()+",");
    	results+= df.format(new Date())+" , "+timeDelay()+".";
    	
    	/***** checking whether the page completely loaded or not. *********/
    	
    	boolean isPrivacyPresent = driver.getPageSource().contains("privacy-policy.html");
    	String error="test-product-html page in the checkout script. ";
    	timeDelayCheck(timeDelay(), flag, isPrivacyPresent, fw, error);
        Assert.assertEquals(flag, true);
  }
    
    @Test
    public void buyProd() throws Exception
    {
    	boolean flag=true;
    	start();
    	fw.write("buyProd,");
    	results = new String("buyProduct , ");
    	fw.write(df.format(new Date()));
    	results += df.format(new Date()) +" , ";
       	try{
    		element=driver.findElement(By.name("dispatch[checkout.add..33469]"));
    		element.click();
    		//driver.findElement(By.linkText("Checkout")).click();
    		driver.get("https://secure.shopclues.com/index.php?dispatch=checkout.checkout&edit_step=step_two");

    	}catch(Exception e){
    		flag=false;
    		System.out.println("check out page could not opened up. ");
    	}
    	end();
    	fw.write(df.format(new Date())+timeDelay()+",");
    	results+= df.format(new Date())+" , "+timeDelay()+".";
    	boolean isPrivacyPresent = driver.getPageSource().contains("privacy-policy.html");
    	String error="Buy Product page in the checkout script. ";
    	timeDelayCheck(timeDelay(), flag, isPrivacyPresent, fw, error);
        Assert.assertEquals(flag, true);
    }
    
    @Test
    public void signIn() throws Exception
    {
    	boolean flag=true;
    	fw.write("signIn,");
    	results = new String("signIn , ");
		start();
		fw.write(df.format(new Date()));
    	results += df.format(new Date()) +" , ";
       	try
    	{
    		//element = driver.findElement(By.className("product_homepagedealblock_name"));
    		driver.findElement(By.id("login_checkout")).sendKeys("autotest@shopclues.com");
    		/*
    		 
****        In case the test case ain't running continuously try implementing the below method.  *******
    		 
    		   element=driver.findElement(By.className("panel_login_textbox"));
    		   element=driver.findElement(By.id("login_checkout"));
    	       element.clear();
    		   element.sendKeys("autotest@shopclues.com");
    		*
    		*/
    		element=driver.findElement(By.name("password"));
    		element.clear();
    		element.sendKeys("autotest");
    	    driver.findElement(By.name("dispatch[auth.login]")).click();

    	}catch(Exception e){
    		System.out.println("element not found.");
    		flag=false;
    		e.printStackTrace();
    	}
    	
    	end();
    	fw.write(df.format(new Date())+timeDelay()+",");
    	results+= df.format(new Date())+" , "+timeDelay()+".";
    	boolean isPrivacyPresent = driver.getPageSource().contains("privacy-policy.html");
    	String error="Sign-In page in the checkout script. ";
    	timeDelayCheck(timeDelay(), flag, isPrivacyPresent, fw, error);
        Assert.assertEquals(flag, true);
    }
   
    @Test
    public void theAddress() throws Exception
    {
    	boolean flag=true;
    	fw.write("address,");
    	results = new String("theAddress , ");
    	start();
    	fw.write(df.format(new Date()));
    	results += df.format(new Date()) +" , ";
       	try{
    /****** first name ******/		
    		element =driver.findElement(By.id("s_firstname"));
    		element.clear();
    		element.sendKeys("selenium");
    		
    /****** Last name  ******/	    
    		element= driver.findElement(By.id("s_lastname"));
    	    element.clear();
    	    element.sendKeys("test");
    	    
    /****** Address   *****/
    		element=driver.findElement(By.id("s_address"));
    		element.clear();
    		element.sendKeys("40A/5, Second Floor, Sect-15");
    		
    /******* Address 2  *****/
    		element=driver.findElement(By.id("s_address_2"));
    		element.clear();
    		element.sendKeys("part -II, Chander Nagar");
    		
    /******  City  ******/		
    		element=driver.findElement(By.id("s_city"));
    		element.clear();
    		element.sendKeys("Gurgaon");
    		
    /******  State  ******/		
    		element=driver.findElement(By.id("s_state"));
    		new Select(driver.findElement(By.id("s_state"))).selectByVisibleText("Haryana");
    		
    /******  Pin code  ******/		
    		element=driver.findElement(By.id("s_zipcode"));
    		element.clear();
    		element.sendKeys("122001");
    		
    /***** Phone number  ******/		
    		element=driver.findElement(By.id("s_phone"));
    		element.clear();
    		element.sendKeys("9999999999");
    		
    		driver.findElement(By.name("dispatch[checkout.update_steps]")).click();
    		//new Select(driver.findElement(By.className("paymentTabDetail_selectbox"))).selectByVisibleText("AXIS Bank");
    	}catch(Exception e){
    		flag=false;
    	     System.out.println("Address not entered. :-(");
    	}
    	end();
    	fw.write(df.format(new Date())+timeDelay()+",");
    	results+= df.format(new Date())+" , "+timeDelay()+".";
    	boolean isPrivacyPresent = driver.getPageSource().contains("privacy-policy.html");
    	String error="address page in the checkout script. ";
    	timeDelayCheck(timeDelay(), flag, isPrivacyPresent, fw, error);
        Assert.assertEquals(flag, true);
    }
    
    @Test
    public void thePaymentGateway()throws Exception
    {
    	boolean flag=true;
    	fw.write("paymentGateway,");
    	results = new String("thePaymentGateway , ");
    	start();
    	fw.write(df.format(new Date()));
    	results += df.format(new Date()) +" , ";
       	try{
       		driver.findElement(By.linkText("Net Banking")).click();
    	    driver.findElement(By.id("payment_method_2")).click();
    	    driver.findElement(By.linkText("Continue")).click();
      	    driver.findElement(By.linkText("Place Order")).click();
      	    boolean isTextPresent= driver.getPageSource().contains("Axis Bank Internet Banking");
      	       
      	    
    	}catch(Exception e){
    		System.out.println("Payment Gateway could not be reached. :-(");
    		flag=false;
    	}
    	
    	end();
    	fw.write(df.format(new Date())+timeDelay()+",");
    	results+= df.format(new Date())+" , "+timeDelay()+".";
    	boolean isPrivacyPresent = driver.getPageSource().contains("Axis Bank Internet");
    	String error="payment gateway page in the checkout script. ";
    	this.defTimeDelay = 40000L;
    	timeDelayCheck(timeDelay(), flag, isPrivacyPresent, fw, error);
        Assert.assertEquals(flag, true);
    }
    @AfterClass
    public void close()throws Exception
    {
    	this.defTimeDelay = 20000L;
    	fw.close();
    	System.out.println(starttime+" "+endTime+" "+timeDelay());
     	driver.close();
     	driver.quit();
    }
    
}