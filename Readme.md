IMPLEMENTING CONTINUOUS MONITORING OF COMMERCIAL WEBSITE USING SELENIUM & MAVEN
						
/**********
@author – Saurabh Jain
@Date – 1st Feb, 2013
@Company – Shopclues
**********/

→ Steps to configure selenium and maven in eclipse : 
1.) Install eclipse for J2EE development and then go to help->install new software-> work with 'ADD THE LOCATION OF THE SOFTWARE TO BE DOWNLOADED'-> click on Add button and then select all the required packages and then follow the procedure after clicking on next button.

→ http://beust.com/eclipse   - testng
→ http://eclipse.org/m2e-wtp/download/ 	or 		  	  	      
→ http://download.eclipse.org/technology/m2e/releases - maven 

2) Now install the maven in the system using the steps given in the following link - http://maven.apache.org/download.cgi

3) Download the following packages from this site- http://seleniumhq.org/download/
→ selenium client driver for java
→ selenium server

4) Now create a new maven project in eclipse by going to file → new → others → maven → maven project .

5) Now open the pom.xml in the project just created above and add the dependency corresponding to each external package that you are going to import in your java program. Add the following dependencies in your pom.xml :-
→ group id - org.testng , artifact id - testng 
→  group id – javax.mail artifact id - mail
→  group id – javax.activation artifact id - activation
→ group id – org.seleniumhq.selenium artifact id - selenium-java 
→  group id – org.seleniumhq.selenium artifact id – selenium-parent

6) go to package explorer and look for your project and then maximize this dir. Src/test/java  and rename the default file – AppTest.java . To rename it, right click on this and go to refractor and rename it with the file name you want followed by Test keyword . Then click on finish it.

7) Right click on the maven project in the package explorer and then go to properties → build path → libraries .

8) Click on the add external jars/zips  and then add the following packages - 
→ selenium-java-2.28.0-zip
→ selenium-server-standalone-2.28.0.jar
→ testng-6.8.zip
→ javamail-1.4.5/mail.jar
→ jaf-1.1.1/activation.jar

9) In order to run the maven project, first go to the folder where selenium-server-standalone-2.28.0.jar is located and then type the following bash command- 
→ java  -Dwebdriver.chrome.driver=/home/saurabh/chromedriver -jar selenium-server-standalone-2.28.0.jar. To download this driver , go to http://code.google.com/p/chromedriver/downloads/list and unzip this file.

10) To build the maven project, go to the directory where your maven project is located through terminal . After navigating into it, you will see a pom.xml file. Now, type the following command in the terminal – mvn clean test .
