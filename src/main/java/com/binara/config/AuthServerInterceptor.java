package com.binara.config;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component  
public class AuthServerInterceptor implements ServerInterceptor {

    private static Logger LOGGER = Logger.getLogger("InfoLogging");

    private static Metadata.Key<String> AUTHORIZATION_HEADER =
            Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        LOGGER.info("Headers from client: " + metadata);

        String accessToken = metadata.get(AUTHORIZATION_HEADER).substring("Bearer ".length());
        DefaultTokenServices tokenServices = (DefaultTokenServices) consumerTokenServices;
        OAuth2Authentication authentication = tokenServices.loadAuthentication(accessToken);
        if(authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return serverCallHandler.startCall(serverCall, metadata);
        }

        throw new UnauthorizedClientException("Unauthorized client connection");
    }
}
