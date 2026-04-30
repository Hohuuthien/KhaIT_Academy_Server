package com.khait_academy.backend.constants;

public class SecurityEndpoints {

    // ===== PUBLIC =====
    public static final String[] PUBLIC = {
        "/api/auth/**"
    };

    public static final String[] PUBLIC_GET = {
        "/api/courses/**",
        "/api/categories/**",
        "/api/lessons/**"
    };

    // ===== AUTH =====
    public static final String[] ENROLLMENT = {
        "/api/enrollments/**"
    };
    public static final String[] STUDENT ={
        "/api/students/**"
    };
    // ===== ADMIN =====
    public static final String ADMIN = "/api/admin/**";

    // ===== COURSE =====
    public static final String COURSE = "/api/courses/**";

    // ===== LESSON =====
    public static final String LESSON = "/api/lessons/**";

    private SecurityEndpoints() {}
}