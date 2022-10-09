//package com.epam.common.swagger;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import io.swagger.v3.core.util.Json;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.models.OpenAPI;
//import org.springdoc.core.SpringDocConfigProperties;
//import org.springdoc.core.customizers.OpenApiCustomiser;
//import org.springdoc.core.providers.ActuatorProvider;
//import org.springdoc.core.providers.SecurityOAuth2Provider;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//import java.util.Optional;
//
//import static org.springdoc.core.Constants.API_DOCS_URL;
//
//@Component
//public class OpenApiResource extends org.springdoc.webmvc.api.OpenApiResource {
//
//    public OpenApiResource(OpenAPIBuilder openAPIBuilder, AbstractRequestBuilder requestBuilder, GenericResponseBuilder responseBuilder, OperationBuilder operationParser, RequestMappingInfoHandlerMapping requestMappingHandlerMapping, Optional<ActuatorProvider> servletContextProvider, Optional<List<OpenApiCustomiser>> openApiCustomisers, SpringDocConfigProperties springDocConfigProperties, Optional<SecurityOAuth2Provider> springSecurityOAuth2Provider) {
//        super(openAPIBuilder, requestBuilder, responseBuilder, operationParser, requestMappingHandlerMapping, servletContextProvider, openApiCustomisers, springDocConfigProperties, springSecurityOAuth2Provider);
//    }
//
//    @Operation(hidden = true)
//    @GetMapping(value = API_DOCS_URL, produces = MediaType.TEXT_PLAIN_VALUE)
//    public String openapiJson(HttpServletRequest request, @Value(API_DOCS_URL) String apiDocsUrl)
//            throws JsonProcessingException {
//        calculateServerUrl(request, apiDocsUrl);
//        OpenAPI openAPI = this.getOpenApi();
//        return Json.mapper().writeValueAsString(openAPI);
//    }
//
//    private void calculateServerUrl(HttpServletRequest request, String apiDocsUrl) {
//        String requestUrl = decode(request.getRequestURL().toString());
//        String calculatedUrl = requestUrl.substring(0, requestUrl.length() - apiDocsUrl.length());
//        openAPIBuilder.setServerBaseUrl(calculatedUrl);
//    }
//}
