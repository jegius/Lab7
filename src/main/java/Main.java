
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        String login = "login";
        String password = "password";

        VkTest testOne = new VkTest();
        testOne.doTest(login, password);
    }
}

