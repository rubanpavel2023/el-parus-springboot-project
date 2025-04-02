package com.example.el_parus_springboot_project.Service.Admin;

import com.example.el_parus_springboot_project.Entity.Admin;
import com.example.el_parus_springboot_project.Repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Service
public class CustomAdminDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String adminName) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByAdminName(adminName);
        if (admin == null) {
            throw new UsernameNotFoundException("Admin not found: " + adminName);
        }
        return new User(
                admin.getAdminName(),
                admin.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(admin.getRole()))
        );
    }
}

