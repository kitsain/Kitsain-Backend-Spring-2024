package com.myblogbackend.blog.controllers;


import com.google.firebase.messaging.FirebaseMessagingException;
import com.myblogbackend.blog.request.AllDevicesNotificationRequest;
import com.myblogbackend.blog.request.DeviceNotificationRequest;
import com.myblogbackend.blog.request.NotificationSubscriptionRequest;
import com.myblogbackend.blog.request.TopicNotificationRequest;
import com.myblogbackend.blog.services.impl.NotificationService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@Hidden
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send-to-device")
    public ResponseEntity<String> sendNotification(final @RequestBody @Valid DeviceNotificationRequest request) {
        try {
            notificationService.sendNotificationToDevice(request);
            return ResponseEntity.ok("Notification sent successfully.");
        } catch (FirebaseMessagingException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send notification.");
        }
    }

    @PostMapping("/send-to-topic")
    public ResponseEntity<String> sendNotificationToTopic(final @RequestBody @Valid TopicNotificationRequest request) {
        try {
            notificationService.sendPushNotificationToTopic(request);
            return ResponseEntity.ok("Notification sent successfully.");
        } catch (FirebaseMessagingException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send notification.");
        }
    }

    @PostMapping("/send-to-all")
    public ResponseEntity<String> sendNotificationToAll(final @RequestBody @Valid AllDevicesNotificationRequest request) {
        try {
            notificationService.sendMulticastNotification(request);
            return ResponseEntity.ok("Multicast notification sent successfully.");
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send multicast notification.");
        }
    }

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribeToTopic(final @RequestBody @Valid NotificationSubscriptionRequest request) {
        try {
            notificationService.subscribeDeviceToTopic(request);
            return ResponseEntity.ok("Device subscribed to the topic successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to subscribe device to the topic.");
        }
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribeFromTopic(final @RequestBody @Valid NotificationSubscriptionRequest request) {
        try {
            notificationService.unsubscribeDeviceFromTopic(request);
            return ResponseEntity.ok("Device unsubscribed from the topic successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to unsubscribe device from the topic.");
        }
    }
}