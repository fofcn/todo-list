package com.epam.common.security.jwt;

import com.epam.common.core.constant.SecurityConstants;
import com.epam.common.core.exception.TodoException;
import com.epam.common.security.common.MockRequestAttributes;
import com.epam.todo.common.security.jwt.JwtException;
import com.epam.todo.common.security.jwt.JwtPayload;
import com.epam.todo.common.security.jwt.JwtUtils;
import com.nimbusds.jose.JWSObject;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtUtilsTest {

    private String tokenType = "Bearer ";

    @Test
    void testGetJwtPayloadNormal() {
        String token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0YWxlbnRJZCI6MSwidXNlcl9uYW1lIjoiZXJyb3JmYXRhbDg5QGdtYWlsLmNvbSIsInNjb3BlIjpbIlJPTEVfVVNFUiJdLCJleHAiOjE2NjAxNjA2OTMsInVzZXJJZCI6MiwianRpIjoiVE1ENnZQMlhKQUxQV1dhT2theUEtVjI0VkJJIiwiY2xpZW50X2lkIjoiY2xpZW50SWQiLCJ1c2VybmFtZSI6ImVycm9yZmF0YWw4OUBnbWFpbC5jb20ifQ.H5P4_EBswNgvCH8CmiNemo4ROUARI0gmvtBu6dq7kaK8N-rcoAZcyDPAqqKQJSHn0GRtCYYQjtbsdPGhyBLr0DzdJp0UYLsKK1zWUMHVWhDwmrdgVfwWJ6uJ13r7QFTVqL6HtudVaG0ZeCk22lg0EpjuIrc6vo01dOQFj8ptUkAOFPKSGoleiR1EgHurqLo30XRz98-NAOoQzTsdug90h4aQr2uHm_WwMTQNKdUa4KG67RLGnrChL8NFe2rhot4P7N5nMI7d095KcAgolyypAd2j1xhq_42Ku67zpBydlZeKrl-m-2ssNA7dwmEqdJXv7sabqQzGI_voHE3Choat0g";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(SecurityConstants.AUTHORIZATION_KEY, token);
        RequestAttributes mockedReqAttrs = new MockRequestAttributes(request);
        try (MockedStatic<RequestContextHolder> contextHolder = Mockito.mockStatic(RequestContextHolder.class)) {
            contextHolder.when(RequestContextHolder::getRequestAttributes).thenReturn(mockedReqAttrs);
            JwtPayload jwtPayload = JwtUtils.getJwtPayload();
            assertNotNull(jwtPayload);
        }
    }

    @Test
    void testGetJwtPayloadNullAndNotStartWithBearer() {
        String token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0YWxlbnRJZCI6MSwidXNlcl9uYW1lIjoiZXJyb3JmYXRhbDg5QGdtYWlsLmNvbSIsInNjb3BlIjpbIlJPTEVfVVNFUiJdLCJleHAiOjE2NjAxNTU4ODAsInVzZXJJZCI6MiwianRpIjoiZXRmU0V3TkFEZnVYNmw0eHF3V1NuS2NBeUF3IiwiY2xpZW50X2lkIjoiY2xpZW50SWQiLCJ1c2VybmFtZSI6ImVycm9yZmF0YWw4OUBnbWFpbC5jb20ifQ.3TcMWMUPCfXPuSHwMz4B0JysLkliGHrbKydreBWYI914GaTobcvMirdl5ZZGBmsb_XPLQaOt1WuSTgutgsJK5aeWFMoahMu9To05g94wSkbCJluYKV5zuIaGOwYyAfvKXQ_YApEexwU0ErYsS5GnwhWAWi47C-uQc7ujZ79LWmPSTYMuLxPdWtfMle0kQmaZaMWiNaKSnVsq2Y-HFmdpXZ1ybY9Fx7cS_W0hnc38ca3QTnQQNPPbYFIWesvM7RJ3BF0joWjzZMMtnAPtX9p340nrB7jI1MmLsqoAi_pxs97gJLSBZudAi9okixXOCttn5XOPPbz2JcE9r1tW7tMbeg";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(SecurityConstants.AUTHORIZATION_KEY, StringUtils.EMPTY);
        RequestAttributes mockedReqAttrs = new MockRequestAttributes(request);
        try (MockedStatic<RequestContextHolder> contextHolder = Mockito.mockStatic(RequestContextHolder.class)) {
            contextHolder.when(RequestContextHolder::getRequestAttributes).thenReturn(mockedReqAttrs);
            TodoException todoException = assertThrows(TodoException.class, () -> {
                JwtUtils.getJwtPayload();
            });
            assertNotNull(todoException);
        }
    }

    @Test
    void testGetJwtPayloadOnlyTwoPartsThenThrowJwtException() {
        String token = "eyJ0YWxlbnRJZCI6MSwidXNlcl9uYW1lIjoiZXJyb3JmYXRhbDg5QGdtYWlsLmNvbSIsInNjb3BlIjpbIlJPTEVfVVNFUiJdLCJleHAiOjE2NjAxNTU4ODAsInVzZXJJZCI6MiwianRpIjoiZXRmU0V3TkFEZnVYNmw0eHF3V1NuS2NBeUF3IiwiY2xpZW50X2lkIjoiY2xpZW50SWQiLCJ1c2VybmFtZSI6ImVycm9yZmF0YWw4OUBnbWFpbC5jb20ifQ.3TcMWMUPCfXPuSHwMz4B0JysLkliGHrbKydreBWYI914GaTobcvMirdl5ZZGBmsb_XPLQaOt1WuSTgutgsJK5aeWFMoahMu9To05g94wSkbCJluYKV5zuIaGOwYyAfvKXQ_YApEexwU0ErYsS5GnwhWAWi47C-uQc7ujZ79LWmPSTYMuLxPdWtfMle0kQmaZaMWiNaKSnVsq2Y-HFmdpXZ1ybY9Fx7cS_W0hnc38ca3QTnQQNPPbYFIWesvM7RJ3BF0joWjzZMMtnAPtX9p340nrB7jI1MmLsqoAi_pxs97gJLSBZudAi9okixXOCttn5XOPPbz2JcE9r1tW7tMbeg";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(SecurityConstants.AUTHORIZATION_KEY, tokenType + token);
        RequestAttributes mockedReqAttrs = new MockRequestAttributes(request);
        try (MockedStatic<RequestContextHolder> contextHolder = Mockito.mockStatic(RequestContextHolder.class);
               MockedStatic<JWSObject> jwsObject = Mockito.mockStatic(JWSObject.class)) {
            contextHolder.when(RequestContextHolder::getRequestAttributes).thenReturn(mockedReqAttrs);
            jwsObject.when(() -> JWSObject.parse(token)).thenThrow(new ParseException("aaa", 1));
            assertThrows(JwtException.class, () -> JwtUtils.getJwtPayload());
        }
    }

}
