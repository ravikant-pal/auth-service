package com.triton.auth.utils;

import com.triton.auth.dto.enums.ApplicationEnvironment;
import com.triton.auth.model.Permission;
import com.triton.auth.model.Role;
import com.triton.auth.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.triton.auth.utils.Constants.LOGGED_IN_USER_KEY;

@Slf4j
public class AuthUtils {

    public static boolean isTargetEnvironment(Environment environment, ApplicationEnvironment... environments) {
        String[] activeProfiles = environment.getActiveProfiles();
        return Arrays.stream(environments)
                .map(ApplicationEnvironment::getCode)
                .anyMatch(code -> Arrays.asList(activeProfiles).contains(code));
    }

    public static String getCurrentDomain(HttpServletRequest request) {
        return request.getServerName();
    }

    public static String getRandomOTP(String email) {
        return String.format("%06d", new Random().nextInt(999999));
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<T> processCommaSeparatedNumbers(String ids, Class<T> dataType) {
        if (StringUtils.isBlank(ids)) return new HashSet<>();
        String[] urlDecodedIds = null;
        urlDecodedIds = StringUtils.split(URLDecoder.decode(ids, StandardCharsets.UTF_8), Constants.COMMA);

        if (urlDecodedIds == null)
            return null;

        Stream<String> stream = Arrays.stream(urlDecodedIds);

        if (dataType == String.class) {
            return (Set<T>) stream.collect(Collectors.toSet());
        }
        if (dataType == Long.class) {
            return (Set<T>) stream.map(Long::valueOf).collect(Collectors.toSet());
        }
        if (dataType == Integer.class) {
            return (Set<T>) stream.map(Integer::valueOf).collect(Collectors.toSet());
        }
        if (dataType == Double.class) {
            return (Set<T>) stream.map(Double::valueOf).collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    private static Stream<String> processRoles(User user) {
        return user.getRoles()
                .stream()
                .filter(Role::isActive)
                .map(Role::getName);
    }

    public static String[] getUserRoleNamesAsArray(User user) {
        return processRoles(user).toArray(String[]::new);
    }

    public static List<String> getUserRoleNamesAsList(User user) {
        return processRoles(user).collect(Collectors.toList());
    }

    public static Set<String> getUserRoleNamesAsSet(User user) {
        return processRoles(user).collect(Collectors.toSet());
    }


    private static Stream<String> processPermissions(User user) {
        return user.getRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .filter(Permission::isActive)
                .map(Permission::getName);
    }

    public static String[] getPermissionNames(User user) {
        return user.getRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .filter(Permission::isActive)
                .map(Permission::getName)
                .toArray(String[]::new);
    }

    public static String[] getUsePermissionNamesAsArray(User user) {
        return processPermissions(user).toArray(String[]::new);
    }

    public static List<String> getUserPermissionNamesAsList(User user) {
        return processPermissions(user).collect(Collectors.toList());
    }

    public static Set<String> getUserPermissionNamesAsSet(User user) {
        return processPermissions(user).collect(Collectors.toSet());
    }

    public static User getLoggedInUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return (User) request.getAttribute(LOGGED_IN_USER_KEY);
    }
}
