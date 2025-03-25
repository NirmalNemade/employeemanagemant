package com.example.demo.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.demo.enums.Permission;
import com.example.demo.enums.Role;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PermissionMapping {

    private static final Map<Role, Set<Permission>> map = Map.of(
            Role.STUDENT, Set.of(Permission.USER_VIEW, Permission.POST_VIEW),
            Role.INSTRUCTOR, Set.of(Permission.POST_CREATE, Permission.USER_UPDATE, Permission.POST_UPDATE),
            Role.ADMIN, Set.of(Permission.POST_CREATE, Permission.USER_UPDATE, Permission.POST_UPDATE,
                    Permission.USER_DELETE, Permission.USER_CREATE, Permission.POST_DELETE));

    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(Role role) {
        return map.get(role).stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }

}
