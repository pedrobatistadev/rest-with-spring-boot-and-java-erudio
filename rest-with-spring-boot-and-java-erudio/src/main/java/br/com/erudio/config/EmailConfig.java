package br.com.erudio.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class EmailConfig {

    private String host;
    private int port;
    private String username;
    private String password;
    private String from;
    private boolean ssl;

    public EmailConfig() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EmailConfig that)) return false;
        return port == that.port && ssl == that.ssl && Objects.equals(host, that.host) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(from, that.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, username, password, from, ssl);
    }
}
