package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.constant.ActivityType;
import com.sonnguyen.iamservice2.constant.IDTokenType;
import com.sonnguyen.iamservice2.exception.TokenException;
import com.sonnguyen.iamservice2.model.Otp;
import com.sonnguyen.iamservice2.model.UserActivityLog;
import com.sonnguyen.iamservice2.model.UserDetails;
import com.sonnguyen.iamservice2.utils.JWTTokenUtils;
import com.sonnguyen.iamservice2.utils.JWTUtilsImpl;
import com.sonnguyen.iamservice2.viewmodel.*;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Order
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AccountServiceImpl accountService;
    @Autowired
    JWTUtilsImpl jwtUtils;
    @Autowired
    JWTTokenUtils jwtTokenUtils;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    ForbiddenTokenService forbiddenTokenService;
    @Autowired
    AbstractEmailService emailService;
    @Autowired
    OtpService otpService;
    @Value("${application.token.verify_account.live-time-secs}")
    private long verifyAccountTokenLiveTimeSecs;
    UserActivityLogService logService;

    public ResponseEntity<?> oauth2Login(){
        OAuth2User oAuth2User=(OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(oAuth2User.getAttribute("email"));
        ResponseTokenVm responseTokenVm=jwtTokenUtils.generateResponseToken(userDetails.getUsername(), userDetails.getAuthorities());
        return ResponseEntity.ok(responseTokenVm);
    }
    public ResponseEntity<?> requestVerifyAccount(String email) {
        try {
            String token = generateIdToken(email, IDTokenType.VERIFY_ACCOUNT);
            String mailContent = "http://localhost:8085/api/v1/auth/verify/" + token;
            emailService.sendEmail(email, "Verify Email", mailContent);
            log.info("Verify Email sent {}", mailContent);
            return ResponseEntity.ok("An verification mail was sent");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> acceptVerifyAccount(String idToken) {
        try {
            Claims claims = jwtUtils.validateToken(idToken);
            String email = claims.get("id", String.class);
            String type = claims.get("type", String.class);
            if (email != null && type != null && type.equalsIgnoreCase(IDTokenType.VERIFY_ACCOUNT.value)) {
                accountService.verifyAccountByEmail(email);
            }
            return ResponseEntity.ok("Verify Ok");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String generateIdToken(String email, IDTokenType type) throws Exception {
        Map<String, Object> claims = Map.of(
                "id", email,
                "type", type.value
        );
        return jwtUtils.generateToken(claims, "", Instant.now().plus(Duration.ofSeconds(verifyAccountTokenLiveTimeSecs)));
    }

    @Override
    public ResponseEntity<?> login(LoginPostVm loginPostVm) {
        Authentication usernamePasswordAuth = new UsernamePasswordAuthenticationToken(loginPostVm.email(), loginPostVm.password());
        authenticationManager.authenticate(usernamePasswordAuth);
        try {
            logService.saveActivityLog(UserActivityLog.builder().activityType(ActivityType.LOGIN).build());
            return handleLoginSuccessRequest(loginPostVm.email());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> handleLoginSuccessRequest(String email) throws Exception {
        String otpCode = OtpService.generateOtpCode();
        Otp otp = new Otp(otpCode, email, 60 * 3); //3 minutes
        otpService.save(otp);
        emailService.sendEmail(email, "OTP Code", otpCode);
        log.info("OTP Code sent {}", otpCode);
        String idToken = generateIdToken(email, IDTokenType.ACCEPT_LOGIN);
        return ResponseEntity.ok(Map.of(
                "token", idToken,
                "message", "An otp code was sent to your email"
        ));
    }

    public ResponseTokenVm acceptLoginRequest(AcceptedLoginRequestVm acceptedLoginRequestVm) {
        Claims claims = null;
        try {
            claims = jwtUtils.validateToken(acceptedLoginRequestVm.token());
        } catch (Exception e) {
            throw new TokenException("Invalid token");
        }
        String email = claims.get("id", String.class);
        String type = claims.get("type", String.class);
        if (email != null && type != null && type.equalsIgnoreCase(IDTokenType.ACCEPT_LOGIN.value)) {
            otpService.validateOtp(email, acceptedLoginRequestVm.otp());
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            logService.saveActivityLog(UserActivityLog.builder().activityType(ActivityType.LOGIN).build());
            return jwtTokenUtils.generateResponseToken(email, userDetails.getAuthorities());
        }
        throw new TokenException("Invalid token");
    }

    public ResponseTokenVm refreshToken(String refreshToken) {
        if (forbiddenTokenService.findToken(refreshToken) != null) throw new TokenException("Invalid token");
        try {
            Claims claims = jwtUtils.validateToken(refreshToken);
            String scope = claims.get("scope").toString();
            String principal = claims.get("principal").toString();
            Collection<? extends GrantedAuthority> authorities = JWTTokenUtils.extractAuthoritiesFromString(scope);
            return jwtTokenUtils.generateResponseToken(principal, authorities);
        } catch (Exception e) {
            throw new TokenException(e.getMessage());
        }
    }


    public void logout(RequestTokenVm requestTokenVm) {
        logService.saveActivityLog(UserActivityLog.builder().activityType(ActivityType.LOGOUT).build());
        forbiddenTokenService.saveToken(requestTokenVm.access_token());
        forbiddenTokenService.saveToken(requestTokenVm.refresh_token());
    }



    public ResponseEntity<?> changePassword(ChangePasswordPostVm changePasswordPostVm) {
        logService.saveActivityLog(UserActivityLog.builder().activityType(ActivityType.MODIFY_PASSWORD).build());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(changePasswordPostVm.email(), changePasswordPostVm.oldPassword());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        accountService.updatePasswordByEmail(changePasswordPostVm);
        return ResponseEntity.ok("Change password successfully");
    }


}
