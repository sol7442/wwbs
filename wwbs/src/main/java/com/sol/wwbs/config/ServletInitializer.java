package com.sol.wwbs.config;


import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class ServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer implements WebApplicationInitializer{
    
	Logger logger = LoggerFactory.getLogger(ServletInitializer.class);
	
   @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        initWebapp(servletContext);
        super.onStartup(servletContext);
    }
	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[]{RepositoryConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[]{WebServletConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		System.out.println("servlet mappings : /");
		return new String[]{"/"};
	}
	
	private void initWebapp(ServletContext servletContext) {
		String logger_file = servletContext.getRealPath("WEB-INF");
		initLogger(logger_file + "\\logger.xml");
	}

	private void initLogger(String path) {
		File config_file = new File(path);
		if(config_file.exists()){
			try {
				LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
				lc.setPackagingDataEnabled(true);
				
				JoranConfigurator configurator = new JoranConfigurator();
				configurator.setContext(lc);
				lc.reset();
				configurator.doConfigure(path);
			} catch (JoranException e) {
				e.printStackTrace();
			}
		}
		
		logger.info("LOGGER SETTING : {}",path);
	}
}
