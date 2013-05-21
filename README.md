YATSQUO
=======

An object-oriented TeamSpeak3 library

"Yet Another TeamSpeak Query" (The O stands for Object Oriented)

There are already plenty libraries out there, but none of them really fulfilled
my needs. Some were everything but object oriented, resulting in some kind of
library for the library, others didn't implement the protocol in a very nice way.

So I decided to write my own library and this is what I have come up with so far.

To use the library just include it in your own program. It is not fully documented
yet, but there will be a documentation when I have the time to write one.

By now, just scroll through the code or implement the library in your program and
test a bit. I wrote some example applications, too.

**Feature list:**
- +      Connection establishing
- +      Logging in
- +      Sending commands
- +      Receiving answer and stuffing everything in a QueryResponse class
- +      Communication logging
- +      Event Listener
- +      Error handling
- o      Server management (nearly done)
- o      Channel management (nearly done)
- o      Client management
- -      Permission management
- -      Everything else I didn't know it's missing


**Legend:**
- +      Implemented
- o      Somewhere between Missing and Implemented
- -      Missing
