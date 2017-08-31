import com.binara.controller.grpc.*;
import com.binara.entities.AuthTokenInfo;
import io.grpc.Channel;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
//import net.devh.springboot.autoconfigure.grpc.client.GrpcClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import java.io.File;

/**
 * Created by binara on 7/2/17.
 */
//@SpringBootApplication
public class SpringGrpcClient {

//    @GrpcClient(value = "ssl-server", interceptors = {AuthInterceptor.class})
//    @GrpcClient("ssl-server")
    private Channel channel;

    public SpringGrpcClient() {
    }

    private void setupChannel(boolean secured) {
        if (secured) {
            channel = getSecuredChannel();
        } else {
            channel = getUnsecuredChannel();
        }

        AuthTokenInfo authTokenInfo = new AuthToken(secured).sendTokenRequest();

//        channel = ClientInterceptors.intercept(channel, new AuthInterceptor(null, authTokenInfo.getAccessToken()));
        AuthInterceptor.authToken = authTokenInfo.getAccessToken();
        channel = ClientInterceptors.intercept(channel, new AuthInterceptor());

        System.out.println(">>> channel.authority = " + channel.authority());
    }

    private Channel getUnsecuredChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext(true)
                .build();
    }

    private Channel getSecuredChannel() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession sslSession) {
                    return hostname.equals("localhost");
                }
            });

            System.out.println(">>> cert exists : " + new File("src/main/resources/localhost-cert.crt").exists());

            return NettyChannelBuilder.forAddress("localhost", 9095)
                    .sslContext(GrpcSslContexts.forClient().trustManager(new File("src/main/resources/localhost-cert.crt")).build())
                    .build();

        } catch (SSLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void fetchStudent(int id) {
        StudentGrpc.StudentBlockingStub blockingStub = StudentGrpc.newBlockingStub(channel);
        StudentRequest request = StudentRequest.newBuilder().setId(id).build();
        try {
            StudentResponse response = blockingStub.getStudent(request);
            System.out.println(">>> Student [" + response.getId() + ", " + response.getName() + ", " + response.getAge() + "]");
        } catch (StatusRuntimeException e) {
            System.out.println(e.getStatus());
        }
    }

    private void fetchUser(long id) {
        UserGrpc.UserBlockingStub blockingStub = UserGrpc.newBlockingStub(channel);
        UserRequest request = UserRequest.newBuilder().setId(id).build();
        try {
            UserResponse response = blockingStub.getUser(request);
            System.out.println(">>> User [" + response.getId() + ", " + response.getUsername() + ", " + response.getPassword() + "]");
        } catch (StatusRuntimeException e) {
            System.out.println(e.getStatus());
        }
    }

    public static void main(String[] args) {
        SpringGrpcClient client = new SpringGrpcClient();

        client.setupChannel(true);

        System.out.println();
        System.out.println(">>> Testing Grpc: Student");
        client.fetchStudent(3);

//        System.out.println();
//        System.out.println(">>> Testing Grpc: User");
//        client.fetchUser(2);
    }

}
