package Project.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PushNotificationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Long carid;
    @Column
    private String title;
    @Column
    private String body;
    @Column
    private String image;
    @Column
    private String topic;
    @Column
    private String token;
    @Column
    private String click_action;
    @Column
    private String route;
    @Column
    private String tag;

    public PushNotificationRequest() {
    }

    public PushNotificationRequest(Long carid, String title, String body, String image, String topicName, String click_action, String route, String tag) {
        this.id = carid;
        this.title = title;
        this.body = body;
        this.image = image;
        this.topic = topicName;
        this.click_action = click_action;
        this.route = route;
        this.tag = tag;
    }

    public PushNotificationRequest(String title, String body, String image, String topic) {
        this.title = title;
        this.body = body;
        this.image = image;
        this.topic = topic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getCarid() {
        return carid;
    }

    public void setCarid(Long carid) {
        this.carid = carid;
    }

    public String getClick_action() {
        return click_action;
    }

    public void setClick_action(String click_action) {
        this.click_action = click_action;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}