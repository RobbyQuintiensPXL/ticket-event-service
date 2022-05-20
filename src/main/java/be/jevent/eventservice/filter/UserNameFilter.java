package be.jevent.eventservice.filter;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserNameFilter {

    public static final String AUTH_TOKEN = "Authorization";
    private static final Logger logger = LoggerFactory.getLogger(UserNameFilter.class);

    public String getTicketOffice(HttpHeaders requestHeaders) {
        String ticketOffice = "";

        if (requestHeaders.get(AUTH_TOKEN) != null) {
            List<String> header = requestHeaders.get(AUTH_TOKEN);
            assert header != null;
            String head = header.stream().findFirst().orElse("test");
            String authToken = head.replace("Bearer ", "");
            JSONObject jsonObj = decodeJWT(authToken);
            try {
                ticketOffice = jsonObj.getString("Organisation");
            } catch (Exception e) {
                logger.debug(e.getMessage());
            }
        }
        return ticketOffice;
    }

    private JSONObject decodeJWT(String JWTToken) {
        String[] splitString = JWTToken.split("\\.");
        String base64EncodedBody = splitString[1];
        Base64 base64Url = new Base64(true);
        String body = new String(base64Url.decode(base64EncodedBody));
        return new JSONObject(body);
    }
}
