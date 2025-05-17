package kirisame.rush_solver;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RouteLoggingFilter extends OncePerRequestFilter {
    /**
     * Golang-style logging filter for HTTP requests.
     * Logs the HTTP request method and URI before passing the request and response
     * to the next filter in the chain.
     *
     * @param request the {@link HttpServletRequest} object that contains the client's request
     * @param response the {@link HttpServletResponse} object that contains the filter's response
     * @param filterChain the {@link FilterChain} for invoking the next filter or the resource
     * @throws ServletException if an exception occurs that interferes with the filter's normal operation
     * @throws IOException if an I/O error occurs during the processing of the request
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("[Route] " + request.getMethod() + " " + request.getRequestURI());
        filterChain.doFilter(request, response);
    }
}
