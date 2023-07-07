package com.boardify.boardify.controller;

import com.boardify.boardify.entities.Subscription;
import com.boardify.boardify.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionRestController {

    @Autowired
    private SubscriptionService subscriptionService;


    @GetMapping("/all")
    public ResponseEntity<List<Subscription>> getAllSubscriptions(){

        List<Subscription> subscriptions = subscriptionService.findAllSubscriptions();
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }


    @PostMapping("/add")
    public ResponseEntity<Long> addNewSubscription(@RequestBody Subscription subscription){

        Subscription insertedSubscription = subscriptionService.createSubscription(subscription);

        if(insertedSubscription == null ){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(insertedSubscription.getSubscriptionID(), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity updateSubscription(@RequestBody Subscription subscription){

        subscriptionService.updateSubscription(subscription);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteSubscription(@PathVariable("subscriptionID") Long id){
        subscriptionService.deleteSubscription(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}