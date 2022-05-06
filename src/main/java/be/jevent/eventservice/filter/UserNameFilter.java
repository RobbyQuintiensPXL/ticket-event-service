package be.jevent.eventservice.filter;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserNameFilter{

    private static final Logger logger = LoggerFactory.getLogger(UserNameFilter.class);
    public static final String AUTH_TOKEN     = "Authorization";

    public String getUsername(HttpHeaders requestHeaders){
        String username = "";

        if (requestHeaders.get(AUTH_TOKEN) !=null) {
            List<String> header = requestHeaders.get(AUTH_TOKEN);
            String head = header.stream().findFirst().get();
            String authToken = head.replace("Bearer ","");
            JSONObject jsonObj = decodeJWT(authToken);
            try {
                username = jsonObj.getString("preferred_username");
            }catch(Exception e) {logger.debug(e.getMessage());}
        }
        return username;
    }

    private JSONObject decodeJWT(String JWTToken) {
        String[] split_string = JWTToken.split("\\.");
        String base64EncodedBody = split_string[1];
        Base64 base64Url = new Base64(true);
        String body = new String(base64Url.decode(base64EncodedBody));
        JSONObject jsonObj = new JSONObject(body);
        return jsonObj;
    }
}
