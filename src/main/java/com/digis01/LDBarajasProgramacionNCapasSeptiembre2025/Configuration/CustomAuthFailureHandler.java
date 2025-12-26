//package com.digis01.LDBarajasProgramacionNCapasSeptiembre2025.Configuration;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import javax.naming.AuthenticationException;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CustomAuthFailureHandler extends SimpleUrlAuhtenticationFailureHandler{
//    
//    @Override
//    public void onAuthenticationFailure(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            AuthenticationException exception)
//            throws IOException, ServletException {
//      if (exception instanceof DisabledException) {
//            setDefaultFailureUrl(
//                "/login?error=not_verified&username=" +
//                request.getParameter("username")
//            );
//        } else {
//            setDefaultFailureUrl("/login?error=true");
//        }
//
//        super.onAuthenticationFailure(request, response, exception);
//    }
//}
