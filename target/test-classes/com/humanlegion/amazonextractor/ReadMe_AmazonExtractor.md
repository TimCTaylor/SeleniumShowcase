## Amazon Extractor Package

### Summary
This package extracts useful information about a title on the Amazon store front. E.g. sales ranking and number of reviews.

As a technique showcase, it is appropriate to be here. However, I am adding it to the existing Java/ Selenium code here as a proof of concept, but will deliver the production version as part of the Author IPA project, which will be use a microservices cloud-native architecture. Probably be written in Python too, just for variety.

📚  *(Author IPA = Author Indie Publishing Assistant)*  📚  

### Web or file?

Data mining Amazon carries technical and ethical issues (see [Ethical issues](#ethical-issues)). As part of this, we are storing HTML-only copies of live Amazon store pages. The point of this is to have the option to point the Selenium code either at real live Amazon store pages or the saved copies.

🌐  🤔  💾 

### Ethical issues

Mining data from Amazon store pages is something that several products in the Amazon independent book publishing  ecosphere already do, and Amazon is fully aware of this and has insisted only that they do not use product names that include any Amazon trademarks. The amazon.com robots.txt does not deny us these urls. 

Nonetheless, to operate the Author IPA project as a commercial product would be against Amazon terms of service, and I shall restrict it to personal research/ training only. As part of this, I will not use attempts to circumvent Amazon's bot security layer by using such unethical tools as hacked webdrivers, false agents and header information, or the use of proxy servers and VPNs. The volume of requests made of the Amazon website will remain tiny.

The Amazon website will be fully aware of what Author IPA is and what it is doing, and can block requests if it so chooses. 

⚖️  🙏


### Architecture and use cases

This particular package will be part of a use case in which an author or small publisher tracks a selection of their books. This is particularly important during the crucial book release period.

A scheduler service will fire at timed intervals and place a job request on the outbound queue (Hourly for new releases, monthly for everything else). The message broker service will take the next job off the queue and send it to the Amazon Extractor service. It will then take the response and use it to place another job in the queue to direct the response to the repo service for persistent storage. 

Again, this here is proof of concept, but messages will eventually be communicated  using RESTful API calls with JSON headers and payloads. To begin with, we will pass JSON as parameters and return values to the method calls.

🎸  🤘  🎶  🚀