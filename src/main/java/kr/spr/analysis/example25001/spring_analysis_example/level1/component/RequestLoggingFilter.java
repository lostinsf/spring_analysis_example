//package kr.spr.analysis.example25001.spring_analysis_example.level1.component;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.stream.Collectors;
//
//@Component
//public class RequestLoggingFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest request,
//                         ServletResponse response,
//                         FilterChain chain)
//        throws IOException, ServletException {
//
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//
//        // 요청 메서드 및 URL 로그 출력
//        System.out.println("🔍 Filter: Request method = " + req.getMethod());
//        System.out.println("🔍 Filter: Request URL = " + req.getRequestURL());
//
//        // 요청 본문(JSON) 읽기
//        String requestBody = getBody(req);
//        System.out.println("📝 Filter: Request Body = " + requestBody);
//
//        chain.doFilter(request,
//            response);
//    }
//
//    private String getBody(HttpServletRequest request) {
//
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
//            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
//        } catch (IOException e) {
//            return "Failed to read request body: " + e.getMessage();
//        }
//    }
//
//}
