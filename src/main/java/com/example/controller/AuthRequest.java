package com.example.controller;

public class AuthRequest {

    private String user;
    private String password;

    // Constructor không tham số (optional nhưng tốt khi có)
    public AuthRequest() {}

    // Constructor có tham số (optional nếu muốn tạo đối tượng dễ dàng)
    public AuthRequest(String user, String password) {
        this.user = user;
        this.password = password;
    }

    // Getter và Setter cho `user`
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    // Getter và Setter cho `password`
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Optional: Override phương thức `toString` để kiểm tra dễ dàng hơn
    @Override
    public String toString() {
        return "AuthRequest{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
