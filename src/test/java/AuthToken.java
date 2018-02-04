import com.binara.entities.AuthTokenInfo;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedHashMap;

public class AuthToken {

    public static final String AUTH_SERVER_URI_UNSECURED_LOCALHOST = "http://localhost:8080/oauth/token";
    public static final String AUTH_SERVER_URI_UNSECURED_GOOGLE_SERVER = "http://" + SpringGrpcClient.GOOGLE_SERVER + ":8453/oauth/token";
    public static final String AUTH_SERVER_URI_SECURED_LOCALHOST = "https://localhost:8453/oauth/token";
    public static final String AUTH_SERVER_URI_SECURED_GOOGLE_SERVER = "https://" + SpringGrpcClient.GOOGLE_SERVER + ":8453/oauth/token";

    public static final String QPM_PASSWORD_GRANT = "?grant_type=password&username=abc&password=123";

    private boolean secured;

    public AuthToken(boolean secured) {
        this.secured = secured;
    }

    /*
         * Prepare HTTP Headers.
         */
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }

    /*
     * Add HTTP Authorization header, using Basic-Authentication to send client-credentials.
     */
    private HttpHeaders getHeadersWithClientCredentials() {
        String plainClientCredentials = "my-trusted-client:secret";
        String base64ClientCredentials = new String(Base64.encodeBase64(plainClientCredentials.getBytes()));

        HttpHeaders headers = getHeaders();
        headers.add("Authorization", "Basic " + base64ClientCredentials);
        headers.add("Accept", "application/hal+json");
        return headers;
    }

    /*
     * Send a POST request [on /oauth/token] to get an access-token, which will then be send with each request.
     */
    @SuppressWarnings({"unchecked"})
    public AuthTokenInfo sendTokenRequest() {
//        String authServerUri = secured ? AUTH_SERVER_URI_SECURED : AUTH_SERVER_URI_UNSECURED;
        String authServerUri = secured ? AUTH_SERVER_URI_SECURED_GOOGLE_SERVER : AUTH_SERVER_URI_UNSECURED_GOOGLE_SERVER;

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> request = new HttpEntity<String>(getHeadersWithClientCredentials());
        ResponseEntity<Object> response = restTemplate.exchange(authServerUri + QPM_PASSWORD_GRANT, HttpMethod.POST, request, Object.class);
        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) response.getBody();
        AuthTokenInfo tokenInfo = null;

        if (map != null) {
            tokenInfo = new AuthTokenInfo();
            tokenInfo.setAccessToken((String) map.get("access_token"));
            tokenInfo.setTokenType((String) map.get("token_type"));
            tokenInfo.setRefreshToken((String) map.get("refresh_token"));
            tokenInfo.setExpiresIn((int) map.get("expires_in"));
            tokenInfo.setScope((String) map.get("scope"));
            System.out.println(tokenInfo);
            //System.out.println("access_token ="+map.get("access_token")+", token_type="+map.get("token_type")+", refresh_token="+map.get("refresh_token")
            //+", expires_in="+map.get("expires_in")+", scope="+map.get("scope"));;
        } else {
            System.out.println("No user exist----------");

        }
        return tokenInfo;
    }

}
