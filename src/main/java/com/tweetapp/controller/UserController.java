package com.tweetapp.controller;

import com.tweetapp.kafka.Producer;
import com.tweetapp.model.Reply;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepo;
import com.tweetapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1.0/tweets")
public class UserController {

    private final Producer producer;
    
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    
    //Register as a New User
    @PostMapping("/register")
    public User registration(@RequestBody User user){
    	LOGGER.debug("User Registration");
        userService.registerUser(user);
        return user;
    }

    //LOGIN
    @PostMapping("/login")
    public String login(@RequestBody Map<String,Object> user){
    	LOGGER.debug("User Login => request : {}", user);
        User user1 = userService.login((String)user.get("username"),(String)user.get("password"));
        if(user1!= null){
            String username = user1.getUserName();
            return "Hai "+username+"! Welcome Back";
        }
        else{
            return "User not Found";
        }
    }

    //FORGOT PASSWORD
    @PostMapping("/forgot")
    public String forgotPassword(@RequestBody Map<String,String> credentials){
    	LOGGER.debug("Forgot Password :: Credentials => request : {}", credentials);
        return userService.forgotPassword(credentials.get("userid"),credentials.get("password"));
    }
    
    //GET ALL TWEETS
    @GetMapping("/all")
    public List<Tweet> listTweets(){
    	LOGGER.debug("Tweets of all the Users");
        return userService.getAllTweets();
    }
    
    //GET ALL USERS
    @GetMapping("/users/all")
    public List<User> getUsers(){
    	LOGGER.debug("Registered Users");
        return userService.listUsers();
    }

    //SEARCH by USERNAME
    @GetMapping("/user/search/{username}")
    public List<User> findByUsername(@PathVariable String username){
    	LOGGER.debug("User Info of {}", username);
        return userService.findByUsername(username);
    }
    
  //get all tweets of a user
    @PostMapping("/allTweets")
    public List<Tweet> listTweetsOfaUser(@RequestBody String username){
    	LOGGER.debug("Tweet Info of {}", username);
        return userService.getAllTweetsOfUser(username);
    }
    
    //POST new TWEET
    @PostMapping("/add")
    public Tweet newTweet(@RequestBody Map<String,String> addTweet){
    	LOGGER.debug("POST Tweet => request : {}", addTweet);
        return userService.postTweet(addTweet.get("tweet"),addTweet.get("username"));
    }  

    //UPDATE TWEET
    @PutMapping("/update")
    public String updateTweet(@RequestBody Map<String,String> newTweet){
    	LOGGER.debug("Update Tweet => request : {}", newTweet);
        return userService.updateTweet(newTweet.get("tweetId"),newTweet.get("content"));
    }

    //DELETE TWEET
    @DeleteMapping ("/delete")
    public String deleteTweet(@RequestBody Map<String,String> deleteTweet){
    	LOGGER.debug("Delete Tweet => request : {}", deleteTweet);
        return userService.deleteTweet(deleteTweet.get("tweetId"));
    }

    //LIKE TWEET
    @PutMapping("/like")
    public List<String> likeTweet(@RequestBody Map<String,String> details){
    	LOGGER.debug("Like Tweet => request : {}", details);
        return userService.likeTweet(details.get("id"),details.get("username"));
    }

    //REPLY TWEET
    @PostMapping("/reply")
    public Reply replyTweet(@RequestBody Map<String,String> reply){
    	LOGGER.debug("Reply Tweet => request : {}", reply);
        return userService.postTweetReply(reply.get("tweetid"),reply.get("username"),reply.get("tweetreply"));
    }
    
  //Get all Replies
    @GetMapping("/getAllReplies")
    public List<Reply> listReplies(){
    	LOGGER.debug("Listing all the replies");
        return userService.getAllReplies();
    }
    
  //Get user by UserId
    @PostMapping("/user/searchid")
    public User findByUserId(@RequestBody String userid){
    	LOGGER.debug("Search by Userid");
    	return userService.findByUserId(userid);
    	}

    @Autowired
    UserController(Producer producer){
        this.producer = producer;
    }

    @PostMapping("/publish")
    public String sendMessage(@RequestBody String message){
        this.producer.sendMessage(message);
        return "Connection established";
    }
}
