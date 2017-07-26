import com.binara.controller.grpc.*;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import net.devh.springboot.autoconfigure.grpc.client.GrpcClient;

/**
 * Created by binara on 7/2/17.
 */
public class SpringGrpcClient {

//    @GrpcClient("http://127.0.0.1:9090")
    private Channel channel;

    public SpringGrpcClient() {
        this.channel = ManagedChannelBuilder.forAddress("localhost", /*8085*/ 9090).usePlaintext(true).build();
    }

    public static void main(String[] args) {
        SpringGrpcClient client = new SpringGrpcClient();

        System.out.println();
        System.out.println(">>> Testing Grpc: Student");
        client.fetchStudent(1);


        System.out.println();
        System.out.println(">>> Testing Grpc: User");
        client.fetchUser(2);
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

}
