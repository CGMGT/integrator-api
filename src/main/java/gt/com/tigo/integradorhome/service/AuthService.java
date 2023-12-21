package gt.com.tigo.integradorhome.service;

import com.github.scribejava.apis.MicrosoftAzureActiveDirectoryApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserService userService;

    @Value("${azure.application.id}")
    private String azureApplicationId;

    @Value("${azure.application.callback-url}")
    private String azureApplicationCallbackUrl;

    public String getAuthorizationUrl() {
        LOGGER.debug(String.format("@%s::getAuthorizationUrl()", this.getClass().getName()));

        OAuth20Service service =
                new ServiceBuilder(this.azureApplicationId)
                        //.scope("openid")
                        .callback(this.azureApplicationCallbackUrl)
                        .build(
                                MicrosoftAzureActiveDirectoryApi.customResource("https://graph.microsoft.com")
                        );

        return service.getAuthorizationUrl();
    }

    public String getToken(String code) {
        LOGGER.debug(String.format("@%s::getToken(%s)", this.getClass().getName(), code));

        final OAuth20Service service =
                new ServiceBuilder(this.azureApplicationId)
                        //.scope("openid")
                        .callback(this.azureApplicationCallbackUrl)
                        .build(
                                MicrosoftAzureActiveDirectoryApi.customResource("https://graph.microsoft.com")
                        );

        OAuth2AccessToken accessToken;
        String token = null;

        try {
            accessToken = service.getAccessToken(code);

            token = accessToken.getAccessToken();
        } catch (ExecutionException | InterruptedException e1) {
            LOGGER.error(e1.getMessage());

            Thread.currentThread().interrupt();
        } catch (IOException e2) {
            LOGGER.error(e2.getMessage());
        }

        Map<String, Object> userInfo;

        try {
            userInfo = this.userService.getInfo(token);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            userInfo = new LinkedHashMap<>();
        }

        String photo;

        try {
            photo = this.userService.getPhoto(token);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            photo = null;
        }

        userInfo.put("photo", photo);

        // used var key and collection userInfo.keySet()
        for (Map.Entry<String, Object> entry : userInfo.entrySet()) {
            LOGGER.debug(String.format("UserInfo[%s] = (%s) %s", entry.getKey(), (entry.getValue() == null) ? "N/A" : entry.getValue().getClass().getName(), (entry.getValue() == null) ? "null" : entry.getValue().toString()));
        }

        return token;
    }

    public boolean isValidToken() {
        LOGGER.debug(String.format("@%s::isTokenValid()", this.getClass().getName()));

        return true;
    }

}
