package hello;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.json.simple.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.annotation.WebFilter;

import org.json.simple.*;

import com.google.common.util.concurrent.RateLimiter;
@Controller
@RequestMapping("/")
@WebFilter(urlPatterns=("/*"))
public class HelloController  {

    private Twitter twitter;

    private ConnectionRepository connectionRepository;
    
    private RateLimiter limiter = RateLimiter.create(1);
    @Inject
    public HelloController(Twitter twitter, ConnectionRepository connectionRepository) {
    	
        this.twitter = twitter;
        this.connectionRepository = connectionRepository;
    }
    
    @RequestMapping(method=RequestMethod.GET)
    public String helloTwitter(Model model,HttpServletRequest request) {
    	 if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
       		
                return "redirect:/connect/twitter";
            }
    	
        return "redirect:/hello";
    }
    
    @Cacheable("hello")
    @RequestMapping(value="/hello", method=RequestMethod.GET)
	  public String loadTimeline( Model model,ServletRequest request , ServletResponse response ) {
		
    	
    	
    	System.out.println("Aquiring lock");
    	if(limiter.tryAcquire()) {

       	 if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
       		 System.out.println("Redirecting");
                return "redirect:/connect/twitter";
            }
    
         
    try{
    model.addAttribute(twitter.userOperations().getUserProfile());
           
            List<Tweet> tweets = twitter.timelineOperations().getHomeTimeline();
            model.addAttribute("tweets", tweets);
    }
    catch(Exception e)
    {System.out.println(e);
    
    }
    
       	
   	    return "hello";
    	} 
    	else {

System.out.println("Too many requests");
return "twitterReConnect";

    	}
    	
    	
    	
	  }
    
    
    @RequestMapping(value="/post", method = RequestMethod.POST)
	public @ResponseBody String postTweet(@RequestBody JSONObject o,ServletRequest request , ServletResponse response) throws Exception {
	  
    	
    	
    	if(limiter.tryAcquire()) {
    	
    	String tweet = o.get("tweet").toString();
    	twitter.timelineOperations().updateStatus(tweet);
    	return "hello";
    	}
    	
    	else
    	{
    		return "twitterReConnect";
    	}
    	
    }
    void init(FilterConfig filterConfig ) {}
    
    
    void destroy() {}
    
  
    
}
