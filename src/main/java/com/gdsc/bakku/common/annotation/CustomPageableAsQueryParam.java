package com.gdsc.bakku.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Parameter(in = ParameterIn.QUERY
        , description = "0부터 시작하는 페이지 인덱스(0..N)"
        , name = "page"
        , schema = @Schema(type = "integer", defaultValue = "0"))
@Parameter(in = ParameterIn.QUERY
        , description = "반환할 페이지의 크기"
        , name = "size"
        , schema = @Schema(type = "integer", defaultValue = "5"))
@Parameter(in = ParameterIn.QUERY
        , description = "속성,(asc|desc) 형식의 정렬 기준. 기본 정렬 순서는 오름차순입니다. 여러 정렬 기준이 지원됩니다."
        , name = "sort"
        , array = @ArraySchema(schema = @Schema(type = "string")))
public @interface
CustomPageableAsQueryParam {

}