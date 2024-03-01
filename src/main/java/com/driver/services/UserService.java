package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        User savedUser=userRepository.save(user);

        return savedUser.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository

        Optional<User> optionalUser=userRepository.findById(userId);

        if(!optionalUser.isPresent())
        {
           return -1;
        }
        User user=optionalUser.get();
        int userAgeLimit=user.getAge();
        SubscriptionType userSubscriptionType=user.getSubscription().getSubscriptionType();
        List<WebSeries> WebSeriesNames=webSeriesRepository.findAll();

//        long countOfViewableWebSeries=WebSeriesNames.stream()
//                .filter(webSeries -> webSeries.getAgeLimit() < userAgeLimit &&
//                        webSeries.getSubscriptionType() == userSubscriptionType).count();
        long countOfViewableWebSeries = 0;

        for (WebSeries webSeries : WebSeriesNames) {
            // Check if the user can watch the web series based on age limit and subscription type
            if (webSeries.getAgeLimit() <= userAgeLimit &&
                    webSeries.getSubscriptionType() == userSubscriptionType) {
                // Increment the count if the user can watch the web series
                countOfViewableWebSeries++;
            }
        }

        return (int) countOfViewableWebSeries;
    }


}
