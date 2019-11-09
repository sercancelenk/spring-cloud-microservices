package byzas.io.microservice.authservice.config;

import byzas.io.microservice.authservice.model.PermissionType;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, PermissionType> {
    @Override
    public PermissionType convert(String source) {
        return PermissionType.valueOf(source.toUpperCase());
    }
}