// This class needs to be removed as an entity

package com.commerce.webapp.commerceclonewebapp.model.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@RedisHash(value = "otp_request")
public class OtpRequest  implements Serializable {

    private static final long serialVersionUID = 1708925807375596799L;

    @TimeToLive
    public final static long expiration = 60L;

    private transient String subject;

    @Id
    private String email;
    private Boolean isActive;

    private int retryAttempt;
    private String otpValue;
    private ZonedDateTime expiryTimestamp;

    public OtpRequest(String email, Boolean isActive, int retryAttempt, String otpValue, ZonedDateTime expiryTimestamp) {
        this.email = email;
        this.isActive = isActive;
        this.retryAttempt = retryAttempt;
        this.otpValue = otpValue;
        this.expiryTimestamp = expiryTimestamp;
    }

    public OtpRequest(String subject, String email){
        this.subject = subject;
        this.email = email;
    }
}
