import org.mindrot.jbcrypt.BCrypt;

public class GerarHash {

    public static void main(String[] args) {

        String senha = "12345";

        String hash = BCrypt.hashpw(
            senha,
            BCrypt.gensalt(10)
        );

        System.out.println(hash);
    }
}