# ApiGateway
ApiGateway is a simple gateway server for RESTful APIs. It proxies the calls from client(browser/mobile app), 
passes on to the proxied API(twitter APIs in this case) and returns the results back. Additionally it offers the following features,
1. Security(Oauth 2.0 authorization)
2. Rate Limiting 
3. Caching
4. Load balancing


1. Security
Only authorized users can access the gateway. Upon launcing the app, the user is taken through the complete Oauth authorization flow.
Once the user signs in to twitter and authorized the app, a token is received by the app using which all future API calls are performed.

2. Rate Limiting
Guava's RateLimiter is used to limit the API call rate to 1 request per second. RateLimiter offers throttling to handle the rate of API calling.
It protects the app from DoS attacks.

3. Caching
GemFire’s data fabric is use to cache API calls.Spring's caching abstraction intercepts the calls to API having the annotation
@Cacheable and checks if it has already been called. If so it returns the cached copy or else it invokes the API and stores the
response in the cache and sends the result to caller. Caching eliminates the expensive call to the proxied API.

Instructions for running the gateway:
This is a Spring boot project. After running the application, the app can be launched at http://localhost:8080/
The user first has to authorize the app for accessing twitter. The API end points can be tested by clicking 'Load Tweets' and 
"Post tweet' buttons in the timeline page.
