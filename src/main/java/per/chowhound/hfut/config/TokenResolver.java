package per.chowhound.hfut.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import per.chowhound.hfut.annotation.LoginUser;

//@Configuration
//public class TokenResolver implements HandlerMethodArgumentResolver {
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        return parameter.hasParameterAnnotation(LoginUser.class);
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        return null;
//    }
//}
