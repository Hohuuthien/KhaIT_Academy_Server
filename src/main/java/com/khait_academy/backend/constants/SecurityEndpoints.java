package com.khait_academy.backend.constants;

public final class SecurityEndpoints {

    // ================= PUBLIC =================

    public static final String[] PUBLIC = {
            "/api/auth/**"
    };

    public static final String[] PUBLIC_GET = {
            "/api/courses/**",
            "/api/categories/**",
            "/api/posts/**"
    };

    // ================= AUTHENTICATED =================

    public static final String[] ENROLLMENT = {
            "/api/enrollments/**"
    };

    public static final String[] STUDENT = {
            "/api/students/**"
    };

    public static final String[] LESSON_PROGRESS = {
            "/api/lesson-progress/**"
    };

    // ================= CONTENT MANAGEMENT =================

    public static final String[] POST = {
            "/api/posts/**"
    };

    public static final String[] LESSON = {
            "/api/lessons/**"
    };

    public static final String[] COURSE = {
            "/api/courses/**"
    };

    // ================= ADMIN =================

    public static final String ADMIN = "/api/admin/**";

    private SecurityEndpoints() {
        throw new UnsupportedOperationException(
                "Utility class"
        );
    }
}