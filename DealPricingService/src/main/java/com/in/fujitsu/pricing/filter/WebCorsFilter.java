package com.in.fujitsu.pricing.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

@Component
public class WebCorsFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader("X-Powered-By", "FCIPL");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");

		// if the session has expired return NULL
		HttpSession session = request.getSession(false);
		String loginURI1 = request.getContextPath() + "/";
		String loginURI2 = request.getContextPath() + "/login";

		//boolean loggedIn = session != null && session.getAttribute(SessionConstant.SESSION_USER_INFO) != null;
		//boolean loginReq1 = request.getRequestURI().equals(loginURI1);
		//boolean loginReq2 = request.getRequestURI().equals(loginURI2);

		chain.doFilter(req, res);

		/*if (loggedIn || loginReq1 || loginReq2) {
			chain.doFilter(req, res);
		} else {
			response.sendRedirect("/");
		}*/
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}

}