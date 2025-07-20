package org.springframework.grpc.sample.infrastructure.in.rest.interceptor;

import lombok.Getter;
import lombok.Setter;

public class RequestContextHolder {
    // ThreadLocal để lưu trữ request ID
    private static final ThreadLocal<RequestContext> contextHolder = new InheritableThreadLocal<>();

    // Class lưu trữ dữ liệu ngữ cảnh
    @Getter
    @Setter
    public static class RequestContext {
        private String requestPath;
        // Thêm các trường khác nếu cần (ví dụ: userId, tenantId)
    }

    // Gán ngữ cảnh
    public static void setContext(RequestContext context) {
        contextHolder.set(context);
    }

    // Lấy ngữ cảnh
    public static RequestContext getContext() {
        RequestContext context = contextHolder.get();
        if (context == null) {
            context = new RequestContext();
            contextHolder.set(context);
        }
        return context;
    }

    // Xóa ngữ cảnh
    public static void clearContext() {
        contextHolder.remove();
    }
}
