package com.jst.filter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Log4j2
public class TestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("TestFilter");

        HttpServletRequest  req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;

        String auth=req.getHeader("Authorization");
        log.debug("TestFilter:"+auth);
        if(StringUtils.hasText(auth)){
            chain.doFilter(request,response);
        }
        chain.doFilter(request,response);
    }


}
