package com.cuckoo.pomelo.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPublicHello() throws Exception {
        mockMvc.perform(get("/public/hello"))
               .andExpect(status().isOk())
               .andExpect(content().string("匿名访问成功"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUserInfo() throws Exception {
        mockMvc.perform(get("/user/info"))
               .andExpect(status().isOk())
               .andExpect(content().string("用户信息访问成功"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminInfo() throws Exception {
        mockMvc.perform(get("/admin/info"))
               .andExpect(status().isOk())
               .andExpect(content().string("管理员信息访问成功"));
    }

    @Test
    public void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/user/info"))
               .andExpect(status().isUnauthorized());
    }
}