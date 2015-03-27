package io.tednology;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.webflow.config.AbstractFlowConfiguration;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.mvc.builder.MvcViewFactoryCreator;
import org.springframework.webflow.mvc.servlet.FlowHandlerAdapter;
import org.springframework.webflow.mvc.servlet.FlowHandlerMapping;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class WebFlowApplication {

	public static void main(String[] args) {
		new SpringApplication(WebFlowApplication.class).run();
	}

	@Configuration
	static class WebFlowConfiguration extends AbstractFlowConfiguration {

		@Bean
		@Autowired
		public FlowHandlerMapping flowHandlerMapping(ViewResolver viewResolver) {
			FlowHandlerMapping handlerMapping = new FlowHandlerMapping();
			handlerMapping.setOrder(-1);
			handlerMapping.setFlowRegistry(flowRegistry(viewResolver));
			return handlerMapping;
		}

		@Bean
		@Autowired
		public FlowHandlerAdapter flowHandlerAdapter(ViewResolver viewResolver) {
			FlowHandlerAdapter handlerAdapter = new FlowHandlerAdapter();
			handlerAdapter.setFlowExecutor(flowExecutor(viewResolver));
			handlerAdapter.setSaveOutputToFlashScopeOnRedirect(true);
			return handlerAdapter;
		}

		@Bean
		public FlowExecutor flowExecutor(ViewResolver viewResolver) {
			return getFlowExecutorBuilder(flowRegistry(viewResolver)).build();
		}

		@Bean
		public FlowDefinitionRegistry flowRegistry(ViewResolver viewResolver) {
			return getFlowDefinitionRegistryBuilder(flowBuilderServices(viewResolver))
					.setBasePath("classpath:flows")
					.addFlowLocationPattern("/**/*_flow.xml").build();
		}

		@Bean
		public FlowBuilderServices flowBuilderServices(ViewResolver viewResolver) {
			return getFlowBuilderServicesBuilder()
					.setViewFactoryCreator(mvcViewFactoryCreator(viewResolver))
					.setValidator(validator())
			        .setDevelopmentMode(true).build();
		}

		@Bean
		public MvcViewFactoryCreator mvcViewFactoryCreator(ViewResolver viewResolver) {
			MvcViewFactoryCreator factoryCreator = new MvcViewFactoryCreator();
			factoryCreator.setViewResolvers(Arrays.<ViewResolver> asList(viewResolver));
			factoryCreator.setUseSpringBeanBinding(true);
			return factoryCreator;
		}

		@Bean
		public LocalValidatorFactoryBean validator() {
			return new LocalValidatorFactoryBean();
		}
	}
}
