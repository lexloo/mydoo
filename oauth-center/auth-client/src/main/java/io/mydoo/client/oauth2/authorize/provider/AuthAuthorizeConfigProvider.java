package io.mydoo.client.oauth2.authorize.provider;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import com.open.capacity.client.oauth2.authorize.AuthorizeConfigProvider;

@Component
@Order(Integer.MAX_VALUE - 1)
public class AuthAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Override
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.anyRequest().authenticated();

        // 前后分离时需要带上
        config.antMatchers(HttpMethod.OPTIONS).permitAll();

        return true;
    }

}
