# Selenium Showcase

[![Build Status](http://13.53.170.81:8080/buildStatus/icon?job=selenium-maven)](http://13.53.170.81:8080/job/selenium-maven/)  [This build badge is set automatically by my Jenkins server. The CI/CD pipeline is triggered with every commit to this repo.]

## Introduction
I've been learning Selenium and JavaSE and wanted to both deepen my understanding and showcase what I've learned by writing a test automation project for which the system under test was humanlegion.com, my own publishing website. It's been a lot of fun to write. In fact, it still is, as I'm actively extending this.

## Some highlights:

### Page object model
Inside the Junit classes, I keep the tests clean and precise so that the intent of the tests is always clear. Assertions live inside the junit classes and nowhere else. The implementation of the tests is abstracted away to the page objects.

### Element abstraction layer 
The system under test has some complex elements or test requirements, such as book tabs, YouTube video player, and the need to verify Amazon sales links are live and my books are on sale. Where appropriate, these are abstracted into packages of the element abstraction layer. The Junit tests have little or no direct interaction with these element objects because that is handled at the page object level.

### Command-line driven
Tests can be run from the command line using Maven/ Surefire and I've added my own comand line options to communicate to the TestSession object such things as which webdriver to use. In my previous project, I found it very useful to be able to initiate not just the tests but set the test configuration from the command line, so we could easily do such things as drive regression and smoke testing across multiple test configurations from bash scripts.

### Driver abstraction
I use a TestSession class that, amongst other things, handles the selection of driver and driver options, using defaults, coded parameters, or command-line parameters as appropriate.

### OOP design 
Constructor overloading, abstract page class, encapsulation, abstraction, separation of concerns.

## How to use
The folder structure follows the Maven standard. 
- Test code in general is: src\test\java\com\humanlegion\pagetests
- Element abstraction layer: src\test\java\com\humanlegion\elementabstractionlayer
- Page object model classes: src\test\java\com\humanlegion\pageobjectmodel
- Test Session class and other helper classes: src\test\java\com\humanlegion\utils
- There's not a lot of interest at the moment in the following folders, but there will be soon: src\main\java\com\humanlegion\ && src\it

Obviously, we can launch tests from the IDE, but we can also run tests from the command line using the Maven Surefire plugin, or run them through other tools, such as the CI/CD pipeline I have running on Jenkins (though with my simple Jenkins setup -- a single built-in node on my AWS EC2 linux server), we need to run Selenium tests in headless mode if we don't want to crash the Edge driver).

For example, to run all tests in the TestSystemUnitTests class with the Microsoft Edge driver:

```
mvn -Ddriver=EDGE  -Dtest=TestSystemUnitTests test
```

To get a concise output when running all tests, I like to do:
```
mvn -Ddriver=CHROME test | findstr /C:"Running " /C:"Tests run

```

But I'm running Windows. If I were on Unix, I guess that would be

```
mvn -Ddriver=CHROME test | grep -E "Running |Tests run"
```

> [!NOTE]
> **You can pick any Selenium driver you like... so long as it's Chrome or Edge.**
> Yeah, about that. The code's in the TestSession class to handle all the drivers, but it's commented out other than for Chrome and Edge. The reason is simply that I don't have enough space on my main drive to download any more drivers.
> Seriously. I've put the drivers themselves on my D: drive and changed the Maven setting.xml to put everything I can onto D:, but the footprint on C: is still high. Maybe it's time I bought a new computer?



