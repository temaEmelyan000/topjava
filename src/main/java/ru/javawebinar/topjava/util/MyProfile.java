package ru.javawebinar.topjava.util;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(MyProfileCondition.class)
public @interface MyProfile {
    /**
     * The set of profiles for which the annotated component should be registered.
     */
    String[] value();
}

class MyProfileCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        if (context.getEnvironment() != null) {
            MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(MyProfile.class.getName());
            if (attrs != null) {
                for (Object value : attrs.get("value")) {
                    Set<String> profiles = new HashSet<>();
                    profiles.addAll(Arrays.asList(context.getEnvironment().getActiveProfiles()));
                    for (Object o : (String[]) value) {
                        if (!profiles.contains(o)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return true;
    }
}
