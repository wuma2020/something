package cn.kkcoder.domain;

/**
 * 用户类
 */
public class User {
    private String username;
    private String password;
    private String t_roles;
    private String t_permission;
    private boolean rememberMe;

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getT_roles() {
        return t_roles;
    }

    public void setT_roles(String t_roles) {
        this.t_roles = t_roles;
    }

    public String getT_permission() {
        return t_permission;
    }

    public void setT_permission(String t_permission) {
        this.t_permission = t_permission;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
