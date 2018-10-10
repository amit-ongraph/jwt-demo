package com.demo.jwtsecurity;

import com.demo.jwtsecurity.config.TokenAuthenticationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletRequest;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JwtDemoApplicationTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    TokenAuthenticationService service;

    @Test
    public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/products")).andExpect(status().isForbidden());
    }

    @Test
    public void shouldProvideAccessWithoutAuthentication() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/")).andExpect(status().isOk());
    }

    @Test
    public void shouldGenerateAuthToken() throws Exception {

        String username = "user";
        String password = "demouser";

        String body = "{\"username\":\""+username+"\",\"password\":\""+password+"\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/login")
                .content(body))
                .andExpect(status().isOk()).andReturn();

        String token = result.getResponse().getHeader("Authorization");

        mvc.perform(MockMvcRequestBuilders.get("/products")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotGenerateAuthTokenForUnauthorizedUser() throws Exception {

        String username = "unauthorizedUser";
        String password = "password";

        String body = "{\"username\":\""+username+"\",\"password\":\""+password+"\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/login")
                .content(body))
                .andExpect(status().isUnauthorized()).andReturn();
    }
}
