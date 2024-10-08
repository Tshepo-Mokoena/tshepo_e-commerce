package com.tshepo.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;



public class JwtAuthorizationFilter extends OncePerRequestFilter{
	
	@Autowired
	private IJwtProvider jwtProvider;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException 
	{
		
		return request.getRequestURI().startsWith("/api/ts-ecommerce/internal-operations");
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException 
	{
		Authentication authentication = jwtProvider.getAuthenticated(request);
		
		if( authentication != null && jwtProvider.validateToken(request))
		{
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		filterChain.doFilter(request, response);
	}

}
