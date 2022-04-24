/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.quickstart.springboot.web;

import cn.com.svfactory.common.secure.annotation.Resource;
import cn.com.svfactory.common.secure.interceptor.AuthzInterceptor;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.quickstart.springboot.service.ProductService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
public class ProductServiceController implements ApplicationContextAware {


	private ApplicationContext ctx;

	@Autowired
	private ProductService productService;

	@Autowired
    private  HttpServletRequest request;

	@GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
	@Resource("handle:customers:request")
	public List<String> handleCustomersRequest(Principal principal) {
		AuthzInterceptor bean = ctx.getBean(AuthzInterceptor.class);
		System.out.println(bean);
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();

		//InterceptorRegistry beanInWeb = wac.getBean(InterceptorRegistry.class);
		//System.out.println(beanInWeb);

		String header = request.getHeader(KeycloakClientRequestFactory.AUTHORIZATION_HEADER);
		System.out.println(header);
		return productService.getProducts();
	}

	@GetMapping(value = "/public", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, String> handlePublicRequest() {
		return Collections.singletonMap("message", productService.getPublic());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx=applicationContext;
	}
}
