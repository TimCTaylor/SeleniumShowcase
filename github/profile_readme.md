# Hello, I'm Tim C. Taylor 👋

Welcome to my profile on GitHub. I have decades of experience throughout the SDLC as a coder, architect, agile advocate, project manager, quality assurance manager, and I am currently focussed on software testing.

I took a career sabbatical to spend a decade as a professional science fiction author, during which I wrote 25 novels and sold 400,000 copies of my books. Since I came back from the stars, I have been busy as a software development engineer in test (API testing / Python / Pytest/ JSON/ golang) and test automation engineer (Selenium / Java/ Junit).

## About Me

- 📈 I’m currently looking for work in test automation/ software testing.
- 📫 How to reach me: [editors@greyhartpress.com](mailto:editors@greyhartpress.com)
- ⚡ Fun fact: I share a name with the most award-winning British brewery.
- 🦖 Were there dinosaurs when you started coding?: My first coding project was a D&D character generator in 1979 (I was 9). My trickiest was Microsoft C 5.1 (easy) driving Windows 2.11 SDK without a debugger (absolute nightmare).

## Skills

- Python, Java, SQL. A bit of go. And a host of languages I've forgotten or never encountered, but I pick them up easily.
- Pytest, JUnit, Selenium.
- API testing, web test automation, unit testing.
- Test planning, test design, test scripting, test execution, defect and issue tracking.
- Excellent communicator in writing (obviously), but also training delivery, teamworking, and mentoring.


## Projects

### [Selenium Showcase](https://github.com/TimCTaylor/SeleniumShowcase)
I've been learning Selenium and JavaSE and wanted to both deepen my understanding and showcase what I've learned by writing a test automation project for which the system under test was humanlegion.com, my own publishing website. It's been a lot of fun to write.

***Here are a few highlights:***

##### Page object model
Inside the Junit classes, I keep the tests clean and precise so that the intent of the tests is always clear. Assertions live inside the junit classes and nowhere else. The implementation of the tests is abstracted away to the page objects.

##### Element abstraction layer 
The system under test has some complex elements or test requirements, such as book tabs, YouTube video player, and the need to verify Amazon sales links are live and my books are on sale. Where appropriate, these are abstracted into packages of the element abstraction layer. The Junit tests have little or no direct interaction with these element objects because that is handled at the page object level.

##### Command-line driven
Tests can be run from the command line using Maven/ Surefire and I've added my own comand line options to communicate to the TestSession object such things as which webdriver to use. In my previous project, I found it very useful to be able to initiate not just the tests but set the test configuration from the command line, so we could easily do such things as drive regression and smoke testing across multiple test configurations from bash scripts.

##### Driver abstraction
I use a TestSession class that, amongst other things, handles the selection of driver and driver options, using defaults, coded parameters, or command-line parameters as appropriate.

##### OOP design 
Constructor overloading, abstract page class, encapsulation, abstraction, separation of concerns.

## Connect with Me

- [LinkedIn](https://www.linkedin.com/in/tim-c-taylor/)
- [Twitter](https://twitter.com/TimCTaylor)
- [mail](mailto:editors@greyhartpress.com)

Thanks for visiting.