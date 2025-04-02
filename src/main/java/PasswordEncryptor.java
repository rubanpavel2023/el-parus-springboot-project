import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncryptor {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "Admin123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Мой зашифрованный пароль: " + encodedPassword);
    }
}

