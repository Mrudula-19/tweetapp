package com.tweetapp.service;

import com.tweetapp.model.Reply;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.repository.ReplyRepo;
import com.tweetapp.repository.TweetRepo;
import com.tweetapp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TweetRepo tweetRepo;

    @Autowired
    private ReplyRepo replyRepo;

    //User Registration
    @Override
    public User registerUser(User user) {
        User newUser = new User();
        try{
            if(user!=null){
                newUser = userRepo.save(user);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return newUser;
    }

    //Display all the users
    public List<User> listUsers(){
        return userRepo.findAll();
    }

    //Search By UserName
    @Override
    public List<User> findByUsername(String username) {
        List<User> users = userRepo.findAll();
        List<User> usersByUname = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            if ((users.get(i).getUserName()).equals(username)) {
                User reqUser=users.get(i);
                usersByUname.add(reqUser);
            }
        }
        return usersByUname;
    }

    //User Login
    @Override
    public User login(String username, String password) {
        User user;
        try{
            user = userRepo.findUserByUserNameAndPassword(username,password);
            //System.out.println(user.getEmail());
            return user;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //Forgot Password
    @Override
    public String forgotPassword(String userid,String password) {
        if(userid!=null && password!=null) {
            try {
                User user = userRepo.findUserByUserId(userid);
                user.setPassword(password);
                System.out.println(user.getUserName()+" "+user.getPassword());
                userRepo.save(user);
                return "Password updated successfully";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "Enter correct Credentials";
    }

    //Post Tweet
    @Override
    public Tweet postTweet(String tweet, String username) {
        Tweet newTweet = new Tweet();
        newTweet.setTweetContent(tweet);
        newTweet.setUsername(username);
        int max=10000;
        int min = 100;
        String tweetid = "T" + Integer.toString((int)(Math.random()*(max-min+1)+min));
        //String tweetid = Integer.toString((int)(Math.random()*(max-min+1)+min));
        newTweet.setTweetId(tweetid);
        LocalDateTime current =  LocalDateTime.now();
        newTweet.setPostTime(current);
        List<String> likedUsers = new ArrayList<>();
        newTweet.setLikedUsers(likedUsers);
        tweetRepo.save(newTweet);
        return newTweet;
    }

    //Display all the Tweets
    @Override
    public List<Tweet> getAllTweets() {
        return tweetRepo.findAll();
    }

    // Display Tweets of Particular User
    @Override
    public List<Tweet> getAllTweetsOfUser(String username) {
        List<Tweet> newTweet = tweetRepo.getTweetsByUsername(username);
        return newTweet;
    }

    //Update the Tweet
    @Override
    public String updateTweet(String id, String content)
    {
    	if(tweetRepo.findTweetById(id) == null){
    		return "Tweet not found with the id: "+id;
    	}
    	else{
        Tweet updateTweet = tweetRepo.findTweetById(id);
        updateTweet.setTweetContent(content);
        tweetRepo.save(updateTweet);
        return "Tweet Updated";
    	}
    }

    //Delete the Tweet
    @Override
    public String deleteTweet(String id) {
        Tweet deleteTweet = tweetRepo.findTweetById(id);
        String ret_str;
        if(deleteTweet!=null){
            tweetRepo.deleteById(id);
            ret_str = "Deleted Tweet with id "+id;
        }
        else
            ret_str = "Tweet not found with id "+id;
        return ret_str;
    }

    //Like the Tweet
    @Override
    public List<String> likeTweet(String id, String username) {
    	if(tweetRepo.findTweetById(id) == null){
    		return null;
    	}
        Tweet tweet = tweetRepo.findTweetById(id);
        List<String> likedUsers  = tweet.getLikedUsers();
        if(id!=null && username!=null){
            likedUsers.add(username);
        }
        System.out.println(likedUsers);
        tweetRepo.save(tweet);
        return tweet.getLikedUsers();
    }

    //Reply to Tweet
    @Override
    public Reply postTweetReply(String tweetid,String username, String tweetreply) {
        Reply newReply = new Reply();
        newReply.setTweetId(tweetid);
        newReply.setUsername(username);
        newReply.setReplyContent(tweetreply);
        LocalDateTime current =  LocalDateTime.now();
        newReply.setReplyPostTime(current);
        int max=10000;
        int min = 100;
        String replyid = "Rep" + Integer.toString((int)(Math.random()*(max-min+1)+min));
        newReply.setReplyId(replyid);
        replyRepo.save(newReply);
        return newReply;
    }

	@Override
	public List<Reply> getAllReplies() {
		return replyRepo.findAll();
	}

	// returns user specific to the id
    @Override
    public User findByUserId(String userId) {
        User userById = new User();
        userById = userRepo.findUserByUserId(userId);
        if(userById!=null)
            return userById;
        return null;
    }

}
