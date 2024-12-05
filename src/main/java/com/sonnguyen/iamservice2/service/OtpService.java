package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.exception.OtpException;
import com.sonnguyen.iamservice2.model.Otp;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class OtpService {
    static final Random RANDOM = new SecureRandom();
    RedisTemplate<String, Object> redisTemplate;
    PasswordEncoder passwordEncoder;

    public static String generateOtpCode() {
        return String.valueOf(RANDOM.nextInt(100000, 999999));
    }

    public void save(Otp otp) {
        String encodedOtp = passwordEncoder.encode(otp.getOtp());
        redisTemplate.opsForValue().set(otp.getEmail(), encodedOtp, Duration.ofSeconds(otp.getTimeToLiveSeconds()));
    }

    public String findByEmail(String email) {
        return (String) redisTemplate.opsForValue().get(email);
    }

    public void validateOtp(String email, String otp) {
        String encodedOtp = findByEmail(email);
        if (!passwordEncoder.matches(otp, encodedOtp)) {
            throw new OtpException("Invalid OTP");
        }
        ;
    }
}
