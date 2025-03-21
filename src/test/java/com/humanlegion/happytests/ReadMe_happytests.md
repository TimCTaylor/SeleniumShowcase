# Happy tests

I used these to test some happy paths at the start of the test automation design. I like to do this to explore an area under test
from multiple perpectives, but especially to give a shakedown test of my understanding of the system under test and my understanding
of the class libraries / architecture I'm going to use. (I'm talking test code in gernal, not just website test automation)

In a real project, this will mostly be used to inform future test design, enhance my understanding of the problem area, and then be deleted.

Not always, though. Sometimes I keep them in if they are robust and easily maintained because with the work already done they can be a slight
mitigation against the potential over emphasis on test isolation. (i.e. test isolation is a good principle, but some issues are only revealed
by tracing longer paths through the system that might not be found by isolated tests).