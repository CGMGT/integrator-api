package gt.com.tigo.integradorhome.security;

import gt.com.tigo.integradorhome.util.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthorizedAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizedAspect.class);

    @Pointcut("execution(@gt.com.tigo.integradorhome.security.Authorized * *(..))")
    public void authorized() {
        // this will be implemented in the next method
    }

    @Before("authorized()")
    public void beforeAuthorized(final JoinPoint joinPoint) {
        LOGGER.debug(String.format("@%s::beforeAuthorized(...)", this.getClass().getName()));

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null) {
            LOGGER.error(String.format("@%s::beforeAuthorized(...) - Missing Authorization header", this.getClass().getName()));

            throw new UnauthorizedException("Access denied");

        } else {
            String token = authorizationHeader.replace("Bearer ", "");

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey("7!G0".getBytes())
                        .parseClaimsJws(token)
                        .getBody();

                LOGGER.debug(String.format("Valid token \"%s\"", claims.getId()));
            } catch (Exception ex) {
                LOGGER.error(String.format("@%s::beforeAuthorized(...) - %s", this.getClass().getName(), ex.getMessage()));

                throw new UnauthorizedException("Access denied");
            }
        }
    }

}
