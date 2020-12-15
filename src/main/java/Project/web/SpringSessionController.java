package Project.web;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class SpringSessionController {

    /**
     * @return list of all messages
     */
    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public List<String> getmessages(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

        if (messages == null) {
            messages = new ArrayList<>();
        }

        return messages;
    }

    /**
     * @return list contain one session
     */
    @RequestMapping(value = "/sessionid", method = RequestMethod.GET)
    public List<String> getsessionid(HttpSession session) {
        List<String> sessions = new ArrayList<String>();
        sessions.add(session.getId());
        return sessions;
    }


    @RequestMapping(value = "/addmessage", method = RequestMethod.POST)
    public void persistMessage(@RequestParam("msg") String msg, HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        List<String> msgs = (List<String>) request.getSession().getAttribute("MY_SESSION_MESSAGES");
        if (msgs == null) {
            msgs = new ArrayList<>();
            request.getSession().setAttribute("MY_SESSION_MESSAGES", msgs);
        }
        msgs.add(msg);
        request.getSession().setAttribute("MY_SESSION_MESSAGES", msgs);
    }

    @RequestMapping(value = "/destroysession", method = RequestMethod.DELETE)
    public void destroySession(HttpServletRequest request) {
        request.getSession().invalidate();
    }
}