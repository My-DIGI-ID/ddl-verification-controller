/*
 * Copyright 2021 Bundesrepublik Deutschland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.esatus.ssi.bkamt.controller.verification.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Unit tests for the {@link WebConfigurer} class.
 */
public class WebConfigurerTest {

    private WebConfigurer webConfigurer;

    private MockServletContext servletContext;

    private MockEnvironment env;

    @BeforeEach
    public void setup() {
        servletContext = spy(new MockServletContext());
        doReturn(mock(FilterRegistration.Dynamic.class))
            .when(servletContext).addFilter(anyString(), any(Filter.class));
        doReturn(mock(ServletRegistration.Dynamic.class))
            .when(servletContext).addServlet(anyString(), any(Servlet.class));

        env = new MockEnvironment();

        webConfigurer = new WebConfigurer(env);
    }

    @Test
    public void testCorsFilterOnOtherPath() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new WebConfigurerTestController())
            .addFilters(webConfigurer.corsFilter())
            .build();

        mockMvc.perform(
                get("/test/test-cors")
                    .header(HttpHeaders.ORIGIN, "other.domain.com"))
            .andExpect(status().isOk())
            .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    @Test
    public void testCorsFilterDeactivated() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new WebConfigurerTestController())
            .addFilters(webConfigurer.corsFilter())
            .build();

        mockMvc.perform(
                get("/api/test-cors")
                    .header(HttpHeaders.ORIGIN, "other.domain.com"))
            .andExpect(status().isOk())
            .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    @Test
    public void testCorsFilterDeactivated2() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new WebConfigurerTestController())
            .addFilters(webConfigurer.corsFilter())
            .build();

        mockMvc.perform(
                get("/api/test-cors")
                    .header(HttpHeaders.ORIGIN, "other.domain.com"))
            .andExpect(status().isOk())
            .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }
}
