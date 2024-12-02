package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.constant.IDTokenType;
import com.sonnguyen.iamservice2.exception.TokenException;
import com.sonnguyen.iamservice2.model.Otp;
import com.sonnguyen.iamservice2.model.UserDetails;
import com.sonnguyen.iamservice2.utils.JWTUtilsImpl;
import com.sonnguyen.iamservice2.viewmodel.AcceptedLoginRequestVm;
import com.sonnguyen.iamservice2.viewmodel.LoginPostVm;
import com.sonnguyen.iamservice2.viewmodel.RequestTokenVm;
import com.sonnguyen.iamservice2.viewmodel.ResponseTokenVm;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Order
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    public static final long ACCESS_TOKEN_EXPIRATION_SECOND = (long) (60 * 60 * 3);
    public static final long REFRESH_TOKEN_EXPIRATION_SECOND = (long) (60 * 60 * 12*7);
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AccountServiceImpl accountService;
    @Autowired
    JWTUtilsImpl jwtUtils;
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

    public ResponseEntity<?> requestVerifyAccount(String email){
        try {
            String token=generateIdToken(email,IDTokenType.VERIFY_ACCOUNT);
            String mailContent="http://localhost:8085/api/v1/auth/verify/"+token;
            emailService.sendEmail(email,"Verify Email",mailContent);
            log.info("Verify Email sent {}", mailContent);
            return ResponseEntity.ok("An verification mail was sent");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public ResponseEntity<?> acceptVerifyAccount(String idToken){
        try {
            Claims claims=jwtUtils.validateToken(idToken);
            String email=claims.get("id",String.class);
            String type=claims.get("type",String.class);
            if(email!=null&&type!=null&&type.equalsIgnoreCase(IDTokenType.VERIFY_ACCOUNT.value)){
                accountService.verifyAccountByEmail(email);
            }
            return ResponseEntity.ok("Verify Ok");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String generateIdToken(String email,IDTokenType type) throws Exception {
        Map<String,Object> claims=Map.of(
                "id",email,
                "type", type.value
        );
        return jwtUtils.generateToken(claims,"",Instant.now().plus(Duration.ofSeconds(verifyAccountTokenLiveTimeSecs)));
    }
    @Override
    public ResponseEntity<?> login(LoginPostVm loginPostVm) {
        Authentication usernamePasswordAuth=new UsernamePasswordAuthenticationToken(loginPostVm.email(), loginPostVm.password());
        authenticationManager.authenticate(usernamePasswordAuth);
        try {
            return handleLoginSuccessRequest(loginPostVm.email());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public ResponseEntity<?> handleLoginSuccessRequest(String email) throws Exception {
        String otpCode=OtpService.generateOtpCode();
        Otp otp=new Otp(otpCode,email,60*3); //3 minutes
        otpService.save(otp);
        emailService.sendEmail(email,"OTP Code",otpCode);
        log.info("OTP Code sent {}", otpCode);
        String idToken= generateIdToken(email, IDTokenType.ACCEPT_LOGIN);
        return ResponseEntity.ok(Map.of(
                "token",idToken,
                "message","An otp code was sent to your email"
        ));
    }

    public ResponseTokenVm acceptLoginRequest(AcceptedLoginRequestVm acceptedLoginRequestVm)  {
        Claims claims= null;
        try {
            claims = jwtUtils.validateToken(acceptedLoginRequestVm.token());
        } catch (Exception e) {
            throw new TokenException("Invalid token");
        }
        String email=claims.get("id",String.class);
        String type=claims.get("type",String.class);
        if(email!=null&&type!=null&&type.equalsIgnoreCase(IDTokenType.ACCEPT_LOGIN.value)){
            otpService.validateOtp(email,acceptedLoginRequestVm.otp());
            UserDetails userDetails=userDetailsService.loadUserByUsername(email);
            return generateResponseToken(email,userDetails.getAuthorities());
        }
        throw new TokenException("Invalid token");
    }

    public ResponseTokenVm refreshToken(String refreshToken){
        if(forbiddenTokenService.findToken(refreshToken)!=null) throw new TokenException("Invalid token");
        try {
            Claims claims=jwtUtils.validateToken(refreshToken);
            String scope=claims.get("scope").toString();
            String principal=claims.get("principal").toString();
            Collection<? extends GrantedAuthority> authorities=extractAuthoritiesFromString(scope);
            return generateResponseToken(principal,authorities);
        } catch (Exception e) {
            throw new TokenException(e.getMessage());
        }
    }


    public void logout(RequestTokenVm requestTokenVm){
        forbiddenTokenService.saveToken(requestTokenVm.access_token());
        forbiddenTokenService.saveToken(requestTokenVm.refresh_token());
    }
    public ResponseTokenVm generateResponseToken(String subject,Collection<? extends GrantedAuthority> authorities){
        String access_token=generateAccessToken(subject,authorities);
        String refresh_token=generateRefreshToken(subject,authorities);
        return new ResponseTokenVm(access_token,refresh_token);
    }
    public String generateRefreshToken(String subject,Collection<? extends GrantedAuthority> authorities){
        Map<String, Object> claims = Map.of(
                "scope", mapAuthoritiesToString(authorities),
                "principal",subject
        );
        try {
            return jwtUtils.builder()
                    .subject(null)
                    .claims(claims)
                    .expiration(Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRATION_SECOND))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String generateAccessToken(String subject, Collection<? extends GrantedAuthority> authorities) {
        Map<String, Object> claims = Map.of(
                "scope", mapAuthoritiesToString(authorities)
        );
        try {
            return jwtUtils.builder()
                    .subject(subject)
                    .claims(claims)
                    .expiration(Instant.now().plusSeconds(ACCESS_TOKEN_EXPIRATION_SECOND))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Collection<? extends GrantedAuthority> extractAuthoritiesFromString(String authorities) {
        if (authorities.isEmpty()) return List.of();
        List<String> scopes = Arrays.stream(authorities.split(" ")).toList();
        return scopes.stream().map(SimpleGrantedAuthority::new).toList();
    }
    public static String mapAuthoritiesToString(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
    }
}
