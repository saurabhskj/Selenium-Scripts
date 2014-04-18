package com.shopclues.selenium.searching;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.Selenium;

public class SearchingTest {
	/**
	 * @author: saurabh jain
	 */
	
	private Selenium selenium=null;
	private WebElement element = null;
	private WebDriver driver=null;
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

    
    private String results="";
    private String browser="*firefox",port="4444";
    private DateFormat df= new SimpleDateFormat("yyyy/MM/dd,HH:mm:ss,");
	private FileWriter fw;
	private	String from="seleniumtest@shopclues.com",to="tops@shopclues.com";
	private	String host="localhost";
	private	Properties property=System.getProperties();
    private DesiredCapabilities capability;
	
	private void open() throws IOException
	{
		fw=new FileWriter("/home/saurabh/logs/res_searching.csv",true);
	}

	public void sendMail( String testname) throws AddressException, MessagingException{
	    property.setProperty("mail.smtp.host", host);
	    Session session = Session.getDefaultInstance(property);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO,
                                 new InternetAddress(to));
        
        message.setSubject("Search: Warning issued in Selenium test scripts.");
        message.setText("Test case failure.\n"+testname+"\n");
        Transport.send(message);
        System.out.println("Message sent successfully....");
	}
	
	public void timeDelayCheck(long timedel,boolean flag,boolean isPrivacyPresent,FileWriter fw,String error,String results)throws Exception
	{
		if(timeDelay()>20000L || !flag){
    		if(!flag){
    			fw.write("Failed,"+"Page load error (Exception thrown)\n");
                sendMail("Errors found in " + error+"\n"+ results);
    		}
    		else
    		    fw.write("Passed,"+"with load time limit exceeded\n");
    	}
    	else if(!isPrivacyPresent)
    		fw.write("Passed,"+"Privacy Policy Element may be missing\n");
    	else
    		fw.write("Passed\n");
  		
	}
    //@Parameters({"browser","port"})
	
	@BeforeClass()
	
	public void homePage() throws Exception
	{
    	boolean flag=true;
    	open();
    	results=new String("homepage , ");
    	fw.write("homepage,");
    	start();
    	fw.write(df.format(new Date()));
    	results+=df.format(new Date())+" , ";
		String url="http://localhost:".concat(port).concat("/wd/hub");
	 	try{
	 		if(browser.equals("*firefox")){
	 			capability = DesiredCapabilities.firefox();
	 			capability.setPlatform(Platform.LINUX);
	 			driver = new RemoteWebDriver(new URL(url), capability);
	 			//driver= new FirefoxDriver();
	 		}
	 		else
	 		{
	 			capability = DesiredCapabilities.chrome();
	 			capability.setPlatform(Platform.LINUX);
	 			driver = new RemoteWebDriver(new URL(url), capability);
			    
     		   //driver = new ChromeDriver();
	 		}
	 		driver.get("http://shopclues.com");
		//	driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		}
		catch(Exception e){
            flag=false; 
			System.out.println("message: "+ e.getMessage());
			e.printStackTrace();
		}
        end();
    	fw.write(df.format(new Date())+timeDelay()+",");
    	results+=df.format(new Date())+" , "+timeDelay()+".";
    	boolean isPrivacyPresent = driver.getPageSource().contains("privacy-policy.html");
    	String error="opening up the homepage link in the Searching Test script. ";
    	timeDelayCheck(timeDelay(), flag, isPrivacyPresent, fw, error,results);        
        Assert.assertEquals(flag, true);
}
	
	//@Parameters({"browser","port"})  
	@Test()
	public void search() throws Exception
	{
    	boolean flag=true;
    	results=new String("search , ");
    	fw.write("search,");
    	start();
    	fw.write(df.format(new Date()));
    	results+=df.format(new Date())+" , ";
    	   try{
		    element= driver.findElement(By.id("q"));
			element.clear();
			element.sendKeys("Samsung");
			element.submit();
		}
		catch(Exception e){
			flag=false;
			System.out.println("Search Element could not found.");
			e.printStackTrace();
		}
    	end();
    	fw.write(df.format(new Date())+timeDelay()+",");
    	results+=df.format(new Date())+" , "+timeDelay();
    	boolean isPrivacyPresent = driver.getPageSource().contains("privacy-policy.html");
    	String error="function named 'search' in the Searching Test script. ";
    	timeDelayCheck(timeDelay(), flag, isPrivacyPresent, fw, error,results);
        Assert.assertEquals(flag, true);
		
	}
	
	//  @Parameters({"browser","port"})
	  @AfterClass()
	
	public void afterClass() throws IOException
	{
        fw.close();		
		driver.close();
	 	driver.quit();
	}

}