# Notes on my Demonstrate Object Model

### 1. Hand code
Write the basic tests. We will:
* go through the banners on the home page
* follow them to their targets
* check target title
* do some page specific things - including checking amazon live links
* check footer copyright

### 2. HomepageBanner object
Add basic element object layer
banner will
* have an id to find it
* a title for the destination page
* an expected image
* methods to gotoTarget() and returnToHomepage()
Make the point that simplifies. Not much benefit. But...
soon we will design a much more powerful element abstraction to implement the amazon link tests. 

### 3. Page object model
Add page object model

Implement a HumanLegion Page class
* return the title
* return the footer text
* return the image file for the banner (poss. stripping the size info)

Inherit from this to do page specific things.
So with my video on TimeDogz, I could have a constructor that returns the
div with my video in it.
Then have methods that play it and pause it, etc.
Perhaps play for ten seconds and then take a screenshot

### 4. Add final AmazonSalesLinks object
This will do a findelements to locate all the book-related tabs
Then check all the ones I find to verify any anchor with an 
'amazon' in the sales link.

