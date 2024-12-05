package com.sonnguyen.iamservice2.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.antlr.v4.runtime.CharStreams;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.Reader;

@Component
@Order
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Log4j2
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logRequest(request);
        filterChain.doFilter(request, response);
        logResponse(response);
    }

    private void logRequest(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String agent = request.getHeader("User-Agent");
        String path = request.getServletPath();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getPrincipal().toString() : "Anonymous";
        log.info("ip {} agent {} path: {} user {}", ip, agent, path, username);
        ObjectMapper mapper = new ObjectMapper();
        if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {
            try (Reader reader = request.getReader()) {
                String object = mapper.writeValueAsString(CharStreams.fromReader(request.getReader()));
                log.info("Request body {}", object);
            } catch (IOException e) {
                log.error("Can't read request body", e);
            }
        }
    }

    private void logResponse(HttpServletResponse response) {
        log.info("Response status: {}", response.getStatus());
//        try (ServletOutputStream out = response.getOutputStream()) {
//
//            ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response,
//                    ContentCachingResponseWrapper.class);
//            if (wrapper != null) {
//                byte[] buf = wrapper.getContentAsByteArray();
//                if (buf.length > 0) {
//                    String payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
//                    logger.debug("Response body :" + payload);
//                    wrapper.copyBodyToResponse();
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }
}
