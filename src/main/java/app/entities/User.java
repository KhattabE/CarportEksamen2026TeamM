package app.entities;
import java.time.LocalDateTime;

public class User {
    private int user_id;
    private String first_name;
    private String last_name;
    private String email;
    private String password_hash;
    private String phone;
    private String address;
    private String postal_code;
    private String city;
    private String role;
    private LocalDateTime created_at;

    public User(int user_id, String first_name, String last_name, String email, String password_hash, String phone, String address, String postal_code, String city, String role, LocalDateTime created_at){
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password_hash = password_hash;
        this.phone = phone;
        this.address = address;
        this.postal_code = postal_code;
        this.city = city;
        this.role = role;
        this.created_at = created_at;
    }
}
