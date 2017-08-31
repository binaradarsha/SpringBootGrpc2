import io.grpc.*;

import java.util.logging.Logger;

public class AuthInterceptor implements ClientInterceptor {
//    private final String apiKey;
//    private final String authToken;
    public static String apiKey;
    public static String authToken;

    private static Logger LOGGER = Logger.getLogger("InfoLogging");

    private static Metadata.Key<String> API_KEY_HEADER =
            Metadata.Key.of("x-api-key", Metadata.ASCII_STRING_MARSHALLER);
    private static Metadata.Key<String> AUTHORIZATION_HEADER =
            Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);

    public AuthInterceptor() {
    }

//    public AuthInterceptor(String apiKey, String authToken) {
//        this.apiKey = apiKey;
//        this.authToken = authToken;
//    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT,RespT> method, CallOptions callOptions, Channel next) {
        LOGGER.info("Intercepted " + method.getFullMethodName());
        ClientCall<ReqT, RespT> call = next.newCall(method, callOptions);

        call = new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(call) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                if (apiKey != null && !apiKey.isEmpty()) {
                    LOGGER.info("Attaching API Key: " + apiKey);
                    headers.put(API_KEY_HEADER, apiKey);
                }
                if (authToken != null && !authToken.isEmpty()) {
                    LOGGER.info("Attaching auth token");
                    headers.put(AUTHORIZATION_HEADER, "Bearer " + authToken);
                }
                super.start(responseListener, headers);
            }
        };
        return call;
    }
}
