package men.doku.donation.web.rest;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import men.doku.donation.security.AuthoritiesConstants;

@RestControllerAdvice
public class SecurityJsonViewControllerAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    @Override
    protected void beforeBodyWriteInternal(
      MappingJacksonValue bodyContainer,
      MediaType contentType,
      MethodParameter returnType,
      ServerHttpRequest request,
      ServerHttpResponse response) {
        if (SecurityContextHolder.getContext().getAuthentication() != null
          && SecurityContextHolder.getContext().getAuthentication().getAuthorities() != null) {
            Collection<? extends GrantedAuthority> authorities
              = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            List<Class<?>> jsonViews = authorities.stream()
              .map(GrantedAuthority::getAuthority)
              .map(AuthoritiesConstants.Role::valueOf)
              .map(AuthoritiesConstants.MAPPING::get)
              .collect(Collectors.toList());
            if (jsonViews.contains(AuthoritiesConstants.Admin.class)) {
              bodyContainer.setSerializationView(AuthoritiesConstants.Admin.class);
              return;
            } else if (jsonViews.contains(AuthoritiesConstants.User.class)) {
              bodyContainer.setSerializationView(AuthoritiesConstants.User.class);
              return;
            } else {
              bodyContainer.setSerializationView(AuthoritiesConstants.Anonymous.class);
              return;
            }
        }
    }
}