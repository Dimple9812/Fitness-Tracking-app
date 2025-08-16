////filter that intercepts every request that passes through it., decode jwt token read the info and query the user's MS
////see if user exists, if not its gonna create,if exists it takes the req forward
//package com.fitness.gateway;
//
//import com.fitness.gateway.user.RegisterRequest;
//import com.fitness.gateway.user.UserService;
//import com.nimbusds.jwt.JWTClaimsSet;
//import com.nimbusds.jwt.SignedJWT;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class KeyCloakUserSyncFilter implements WebFilter {  // we are creating a webflux filter thats gonna intercept every req
//    private final UserService userService;
//
//    //this method is executed for every req, that is passing through the gateway and the job of extracting user id,authorization header
//    //that can happen over here
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
//        String userId = exchange.getRequest().getHeaders().getFirst("X-User-ID");
//        RegisterRequest registerRequest = getUserDetails(token);
//
//        if (userId == null) {
//            userId = registerRequest.getKeyCloakId();
//        }
//
//        if (userId != null && token != null){
//            String finalUserId = userId;
//            return userService.validateUser(userId)
//                    .flatMap(exist -> {
//                        if (!exist) {
//                            // Register User
//
//                            if (registerRequest != null) {
//                                return userService.registerUser(registerRequest)
//                                        .then(Mono.empty());
//                            } else {
//                                return Mono.empty();
//                            }
//                        } else {
//                            log.info("User already exist, Skipping sync.");
//                            return Mono.empty();
//                        }
//                    })
//                    .then(Mono.defer(() -> {
//                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
//                                .header("X-User-ID", finalUserId)
//                                .build();
//                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
//                    }));
//        }
//        return chain.filter(exchange);
//    }
//
//    private RegisterRequest getUserDetails(String token) {
//        try {
//            String tokenWithoutBearer = token.replace("Bearer ", "").trim();
//            SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
//            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
//
//            RegisterRequest registerRequest = new RegisterRequest();
//            registerRequest.setEmail(claims.getStringClaim("email"));
//            registerRequest.setKeyCloakId(claims.getStringClaim("sub"));
//            registerRequest.setPassword("dummy@123123");
//            registerRequest.setFirstName(claims.getStringClaim("given_name"));
//            registerRequest.setLastName(claims.getStringClaim("family_name"));
//            return registerRequest;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}

package com.fitness.gateway;

import com.fitness.gateway.user.RegisterRequest;
import com.fitness.gateway.user.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeyCloakUserSyncFilter implements WebFilter {
    private final UserService userService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        String userId = exchange.getRequest().getHeaders().getFirst("X-User-ID");
        RegisterRequest registerRequest = getUserDetails(token);

        if (userId == null) {
            userId = registerRequest.getKeyCloakId();
        }

        if (userId != null && token != null){
            String finalUserId = userId;
            return userService.validateUser(userId)
                    .flatMap(exist -> {
                        if (!exist) {
                            // Register User

                            if (registerRequest != null) {
                                return userService.registerUser(registerRequest)
                                        .then(Mono.empty());
                            } else {
                                return Mono.empty();
                            }
                        } else {
                            log.info("User already exist, Skipping sync.");
                            return Mono.empty();
                        }
                    })
                    .then(Mono.defer(() -> {
                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("X-User-ID", finalUserId)
                                .build();
                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    }));
        }
        return chain.filter(exchange);
    }

    private RegisterRequest getUserDetails(String token) {
        try {
            String tokenWithoutBearer = token.replace("Bearer ", "").trim();
            SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail(claims.getStringClaim("email"));
            registerRequest.setKeyCloakId(claims.getStringClaim("sub"));
            registerRequest.setPassword("dummy@123123");
            registerRequest.setFirstName(claims.getStringClaim("given_name"));
            registerRequest.setLastName(claims.getStringClaim("family_name"));
            return registerRequest;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}