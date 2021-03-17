package Project.firebase;

import Project.domain.Car;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class PushNotificationService {

    @Value("#{${app.notifications.defaults}}")
    private Map<String, String> defaults;

    private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);
    private FCMService fcmService;

    public PushNotificationService(FCMService fcmService) {
        this.fcmService = fcmService;
    }


    public void sendPushNotificationWithoutData(PushNotificationRequest request) {
        try {
            fcmService.sendMessageWithoutData(request);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }


    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToToken(request);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }

    public void sendCarPushNotification(Car c, PushNotificationRequest request) {
        try {
            fcmService.sendMessage(getPayloadDataFromRequest(request), getCarPushNotificationRequest(c, request));
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }

    private Map<String, String> getPayloadDataFromRequest(PushNotificationRequest request) {
        Map<String, String> pushData = new HashMap<>();
        pushData.put("id", request.getId().toString());
        pushData.put("click_action", !StringUtils.isEmpty(request.getClick_action()) ? request.getClick_action(): defaults.get("click_action"));
        pushData.put("route", !StringUtils.isEmpty(request.getRoute())? request.getRoute():defaults.get("route"));
        return pushData;
    }

    private PushNotificationRequest getCarPushNotificationRequest(Car c, PushNotificationRequest request) {
        return new PushNotificationRequest(
                !StringUtils.isEmpty(request.getTitle()) ? request.getTitle() : "We've got new car for you !",
                !StringUtils.isEmpty(request.getBody()) ? request.getBody() : "The new " + c.getBrand() + " " + c.getModel() + " " + c.getYear() + " is now here! Click to see details",
                !StringUtils.isEmpty(request.getImage()) ? request.getImage() : c.getBrandlogo(),
                !StringUtils.isEmpty(request.getTopic()) ? request.getTopic() : defaults.get("topic") );
    }

    public void sendCustomPushNotification(PushNotificationRequest pushRequest) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("id", pushRequest.getId().toString());
            map.put("click_action", pushRequest.getClick_action());
            map.put("route", pushRequest.getRoute());
            map.put("tag", pushRequest.getTag());
            fcmService.sendMessage(map, pushRequest);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }

    public void sendSamplePushNotification() {
        try {
            fcmService.sendMessage(getSamplePayloadData(), getSamplePushNotificationRequest());
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }

    private Map<String, String> getSamplePayloadData() {
        Map<String, String> pushData = new HashMap<>();
        pushData.put("id", defaults.get("id"));
        pushData.put("click_action", defaults.get("click_action"));
        pushData.put("route", defaults.get("route"));
        return pushData;
    }


    private PushNotificationRequest getSamplePushNotificationRequest() {
        PushNotificationRequest request = new PushNotificationRequest(
                defaults.get("title"),
                defaults.get("body"),
                defaults.get("image"),
                defaults.get("topic"));
        return request;
    }


}