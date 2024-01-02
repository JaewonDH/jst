package com.jst.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;


/**
 컨트롤러에서  @RequestPart 두개를 사용하여 dto와 MultipartFile 받을 경우 Swagger에서 에러가
 HttpMediaTypeNotSupportedException: Content type 'application/octet-stream' not supported  발생하여
 Post man이나 Front에서 FormData에  직접  Conten type을 명시해 주어야 한다. 예를 들어
 dto 올려주는 Content-Type는 application/json 해주면 octet-stream에러가 발생하지 않으며  AbstractJackson2HttpMessageConverter은
 Swagger에서 에러나는 것을 막기 위해서 사용
 */

@Component
public class MultipartJackson2HttpMessageConverter  extends AbstractJackson2HttpMessageConverter {
    protected MultipartJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        return false;
    }
}
