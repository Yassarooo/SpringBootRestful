package Project.web;

import Project.domain.Car;
import Project.domain.PushNotificationRequest;
import Project.firebase.PushNotificationResponse;
import Project.firebase.PushNotificationService;
import Project.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PushNotificationController {

    @Autowired
    private PushNotificationService pushNotificationService;
    @Autowired
    private CarService carService;

    public PushNotificationController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @PostMapping("/notification/topic")
    public ResponseEntity sendNotification(@RequestBody PushNotificationRequest request) {
        pushNotificationService.sendPushNotificationWithoutData(request);
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }

    @PostMapping("/notification/token")
    public ResponseEntity sendTokenNotification(@RequestBody PushNotificationRequest request) {
        pushNotificationService.sendPushNotificationToToken(request);
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }

    @PostMapping("/notification/car")
    public ResponseEntity sendCarNotification(@RequestBody PushNotificationRequest pushRequest) {
        Car c = carService.getCarById(pushRequest.getId());
        if (c != null) {
            pushNotificationService.sendCarPushNotification(c, pushRequest);
            return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.NO_CONTENT.value(), "Cannot send notification. Car not found"), HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/notification/data")
    public ResponseEntity sendDataNotification(@RequestBody PushNotificationRequest pushRequest) {
        pushNotificationService.sendCustomPushNotification(pushRequest);
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }

    @GetMapping("/notification")
    public ResponseEntity sendSampleNotification() {
        try {
            pushNotificationService.sendSamplePushNotification();
            return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    public List<PushNotificationRequest> getAllNotifications() {
        List<PushNotificationRequest> nots = pushNotificationService.getAll();
        return (List<PushNotificationRequest>) nots;
    }

    @RequestMapping(value = "/notifications", method = RequestMethod.DELETE)
    public ResponseEntity deleteAllNotifications() {
        pushNotificationService.deleteAllNotifications();
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/notifications", method = RequestMethod.POST)
    public ResponseEntity addNotification(@RequestBody PushNotificationRequest r) {
        pushNotificationService.createOrUpdateNotification(r, false);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/notifications", method = RequestMethod.PUT)
    public ResponseEntity editNotification(@RequestBody PushNotificationRequest r) {
        pushNotificationService.createOrUpdateNotification(r, true);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/notifications/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteNotification(@PathVariable("id") Long id) {
        pushNotificationService.deleteAllNotifications();
        return new ResponseEntity(HttpStatus.OK);
    }

}