package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.exception.SubscriptionNotFoundException;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.driver.model.SubscriptionType.*;

@Service
public class

SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay

        Optional<User> optionalUser = userRepository.findById(subscriptionEntryDto.getUserId());
        if(!optionalUser.isPresent())
        {
           return -1;
        }
        User user = optionalUser.get();
        int totalAmountPaid = calculateSubscriptionCost(subscriptionEntryDto);
        Subscription subscription=new Subscription();
        subscription.setUser(user);
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        subscription.setStartSubscriptionDate(new Date());
        subscription.setTotalAmountPaid(totalAmountPaid);

        subscriptionRepository.save(subscription);

        return totalAmountPaid;

    }
//subscriptionCost


    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new Exception("User not found");
//            return -1;
        }
        User user = optionalUser.get();

        Subscription userSubscription = user.getSubscription();

        if(userSubscription==null)
        {
//            throw new SubscriptionNotFoundException("User doesn't buy any subscription");
            return -1;
        }

        SubscriptionType nextSubscriptionType;


        if (userSubscription.getSubscriptionType() == ELITE) {
            throw new Exception("Already the best Subscription");
        }
        else if (userSubscription.getSubscriptionType() == null) {
            // Handle case where user does not have a subscription
//            nextSubscriptionType = BASIC;
            return -1;
        }
        else {
            // Determine the next subscription level
            SubscriptionType currentSubscriptionType = userSubscription.getSubscriptionType();
            nextSubscriptionType = getNextSubscriptionType(currentSubscriptionType);
        }

        // Calculate price difference
        int currentPrice = userSubscription.getTotalAmountPaid();
        int nextPrice = calculateNewSubscriptionCost(nextSubscriptionType, userSubscription.getNoOfScreensSubscribed());
        int priceDifference = nextPrice - currentPrice;

        // Update the user's subscription
        userSubscription.setSubscriptionType(nextSubscriptionType);
        userSubscription.setTotalAmountPaid(nextPrice);
        subscriptionRepository.save(userSubscription);
        userRepository.save(user);


        return priceDifference;

//        Optional<User> optionalUser=userRepository.findById(userId);
//        if(!optionalUser.isPresent())
//        {
//            throw new Exception("user not found");
//        }
//        User user=optionalUser.get();
//
//        Subscription subscription=user.getSubscription();
//        Integer newAmount;
//
//        if(subscription.getSubscriptionType()== ELITE)
//        {
//            throw new Exception("Already the best Subscription");
//        }
//        else if (subscription.getSubscriptionType() == null) {
////            // Handle case where user does not have a subscription
//////            nextSubscriptionType = BASIC;
//            return -1;
//        }
//        else if(subscription.getSubscriptionType()== BASIC)
//        {
//            subscription.setSubscriptionType(PRO);
//            newAmount=getAmoutToPay(PRO,subscription.getNoOfScreensSubscribed());
//        }
//        else {
//            subscription.setSubscriptionType(ELITE);
//            newAmount=getAmoutToPay(ELITE,subscription.getNoOfScreensSubscribed());
//        }
//        Integer amountDifference=newAmount-subscription.getTotalAmountPaid();
//        subscription.setTotalAmountPaid(newAmount);
//        subscriptionRepository.save(subscription);
//        userRepository.save(user);
//        return amountDifference;


    }

//    private Integer getAmoutToPay(SubscriptionType subscriptionType, int noOfScreensSubscribed) {
//        int pricePlanss,var;
//        if(subscriptionType==BASIC)
//        {
//            pricePlanss=500;
//            var=200;
//        } else if (subscriptionType== PRO) {
//            pricePlanss=800;
//            var=250;
//
//        }
//        else
//        {
//            pricePlanss=1000;
//            var=350;
//        }
//        return pricePlanss+(var*noOfScreensSubscribed);
//    }


    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        List<Subscription> subscriptions=subscriptionRepository.findAll();
        int totalRevenue=0;
        for(Subscription subscription:subscriptions)
        {
            totalRevenue+=subscription.getTotalAmountPaid();
        }
        return totalRevenue;
    }

    private int calculateSubscriptionCost(SubscriptionEntryDto subscriptionEntryDto) {
        int baseCost;
        switch (subscriptionEntryDto.getSubscriptionType()) {
            case BASIC:
                baseCost = 500;
                break;
            case PRO:
                baseCost = 800;
                break;
            case ELITE:
                baseCost = 1000;
                break;
            default:
                baseCost=0;
        }
        return baseCost + subscriptionEntryDto.getNoOfScreensRequired() * getScreenCost(subscriptionEntryDto.getSubscriptionType());
    }

    private int getScreenCost(SubscriptionType subscriptionType) {
        switch (subscriptionType) {
            case BASIC:
                return 200;
            case PRO:
                return 250;
            case ELITE:
                return 350;
            default:
                return 0;
        }
    }

    private SubscriptionType getNextSubscriptionType(SubscriptionType currentSubscriptionType) {
        switch (currentSubscriptionType) {
            case BASIC:
                return PRO;
            case PRO:
                return ELITE;
            default:
                throw new IllegalArgumentException("Cannot upgrade from ELITE subscription");

        }
    }

    private int calculateNewSubscriptionCost(SubscriptionType nextSubscriptionType, int noOfScreensSubscribed) {

        int baseCost;

        switch (nextSubscriptionType) {
            case BASIC:
                baseCost = 500;
                break;
            case PRO:
                baseCost = 800;
                break;
            case ELITE:
                baseCost = 1000;
                break;
            default:
                throw new IllegalArgumentException("Invalid subscription type");
        }
        return baseCost + noOfScreensSubscribed * getScreenCost(nextSubscriptionType);
    }


}

