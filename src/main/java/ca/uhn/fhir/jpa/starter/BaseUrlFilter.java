package ca.uhn.fhir.jpa.starter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Component
public class BaseUrlFilter implements Filter {
	private static final List<String> REQUIRED_HOST_SEGMENT = Arrays.asList("host.docker.internal", "localhost", "SYMP-LT1121");

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		StringBuffer requestURL = httpRequest.getRequestURL();
		System.out.println("requestURL: " + requestURL);
		URL url = new URL(requestURL.toString());

		// Check if the host is in the list
		String host = url.getHost();
		boolean isAllowedHost = REQUIRED_HOST_SEGMENT.contains(host);
		System.out.println("Extracted Host: " + host);

		if (isAllowedHost) {
			System.out.println("Host is allowed: " + host);
			chain.doFilter(request, response);
		} else {
			System.out.println("Forbidden host: " + host);
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden request.");
		}
	}

	@Override
	public void destroy() {
	}
}
