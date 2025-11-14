package edu.espe.springlab.interceptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.*;
@Disabled("Este test se desactiva temporalmente")

class RequestLoggingInterceptorTest {

    private RequestLoggingInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        interceptor = new RequestLoggingInterceptor();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    // ============================================================
    // 1. preHandle debe guardar startTime (válido)
    // ============================================================
    @Disabled("Este test se desactiva temporalmente")

    @Test
    void shouldStoreStartTimeOnPreHandle() {
        boolean result = interceptor.preHandle(request, response, new Object());

        assertThat(result).isTrue();
        assertThat(request.getAttribute("startTime")).isNotNull();
    }

    /*
    @Test
    void invalid_preHandleShouldFailIfNoRequest() {
        assertThatThrownBy(() -> interceptor.preHandle(null, response, new Object()))
                .isInstanceOf(Exception.class);
    }
    */


    // 2. afterCompletion debe agregar header X-Elapsed-Time

    @Disabled("Este test se desactiva temporalmente")

    @Test
    void shouldAddElapsedTimeHeaderAfterCompletion() {
        interceptor.preHandle(request, response, new Object());

        interceptor.afterCompletion(request, response, new Object(), null);

        String header = response.getHeader("X-Elapsed-Time");
        assertThat(header).isNotNull();
        assertThat(header).contains("ms");
    }

    /*
    @Test
    void invalid_afterCompletionWithoutStartTimeShouldFail() {
        assertThatThrownBy(() -> interceptor.afterCompletion(request, response, new Object(), null))
                .isInstanceOf(Exception.class);
    }
    */

    // ============================================================
    // 3. elapsed time debe ser >= 0
    // ============================================================
    @Disabled("Este test se desactiva temporalmente")

    @Test
    void shouldHaveNonNegativeElapsedTime() {
        interceptor.preHandle(request, response, new Object());

        interceptor.afterCompletion(request, response, new Object(), null);

        String header = response.getHeader("X-Elapsed-Time");

        long elapsed = Long.parseLong(header.replace("ms", ""));
        assertThat(elapsed).isGreaterThanOrEqualTo(0);
    }

    /*
    @Test
    void invalid_elapsedTimeShouldNotBeNegative() {
        request.setAttribute("startTime", System.currentTimeMillis() + 999999);
        assertThatThrownBy(() -> interceptor.afterCompletion(request, response, new Object(), null))
                .isInstanceOf(Exception.class);
    }
    */

    // ============================================================
    // 4. afterCompletion no debe lanzar excepción aunque ex != null
    // ============================================================
    @Disabled("Este test se desactiva temporalmente")

    @Test
    void shouldHandleExceptionParameterSafely() {
        interceptor.preHandle(request, response, new Object());

        assertThatNoException()
                .isThrownBy(() ->
                        interceptor.afterCompletion(request, response, new Object(), new RuntimeException("Error"))
                );
    }

    /*
    @Test
    void invalid_nullResponseShouldFail() {
        interceptor.preHandle(request, response, new Object());
        assertThatThrownBy(() -> interceptor.afterCompletion(request, null, new Object(), null))
                .isInstanceOf(Exception.class);
    }
    */

    // ============================================================
    // 5. Debe imprimir log (no validamos el texto, solo que no falle)
    // ============================================================
    @Disabled("Este test se desactiva temporalmente")

    @Test
    void shouldNotFailWhenLogging() {
        interceptor.preHandle(request, response, new Object());

        assertThatNoException()
                .isThrownBy(() ->
                        interceptor.afterCompletion(request, response, new Object(), null)
                );
    }

    /*
    @Test
    void invalid_loggingShouldFailIfRequestNull() {
        assertThatThrownBy(() -> interceptor.afterCompletion(null, response, new Object(), null))
                .isInstanceOf(Exception.class);
    }
    */

    // ============================================================
    // 6. preHandle con handler = null
    // ============================================================
    @Disabled("Este test se desactiva temporalmente")

    @Test
    void shouldHandleNullHandlerInPreHandle() {
        boolean result = interceptor.preHandle(request, response, null);
        assertThat(result).isTrue();
        assertThat(request.getAttribute("startTime")).isNotNull();
    }

    // ============================================================
    // 7. afterCompletion con handler = null
    // ============================================================
    @Disabled("Este test se desactiva temporalmente")

    @Test
    void shouldHandleNullHandlerInAfterCompletion() {
        interceptor.preHandle(request, response, new Object());

        assertThatNoException()
                .isThrownBy(() ->
                        interceptor.afterCompletion(request, response, null, null)
                );
    }

    // ============================================================
    // 8. afterCompletion cuando el header ya existe
    // ============================================================
    @Disabled("Este test se desactiva temporalmente")

    @Test
    void shouldOverrideExistingElapsedTimeHeader() {
        interceptor.preHandle(request, response, new Object());

        response.addHeader("X-Elapsed-Time", "999ms");

        interceptor.afterCompletion(request, response, new Object(), null);

        String header = response.getHeader("X-Elapsed-Time");

        assertThat(header).isNotEqualTo("999ms");
        assertThat(header).contains("ms");
    }

    // ============================================================
    // 9. startTime con valor extraño (muy antiguo)

    @Disabled("Este test se desactiva temporalmente")
// ============================================================
    @Test
    void shouldHandleWeirdStartTimeValue() {
        request.setAttribute("startTime", System.currentTimeMillis() - 99999999);

        assertThatNoException()
                .isThrownBy(() ->
                        interceptor.afterCompletion(request, response, new Object(), null)
                );

        assertThat(response.getHeader("X-Elapsed-Time")).contains("ms");
    }

    // ============================================================
    // 10. startTime como String
    // ============================================================
    @Test
    void shouldHandleStartTimeAsStringGracefully() {
        request.setAttribute("startTime", "12345"); // no es Long

        assertThatNoException()
                .isThrownBy(() ->
                        interceptor.afterCompletion(request, response, new Object(), null)
                );
    }

    // ============================================================
    // 11. método HTTP extraño (PROPFIND)
    // ============================================================
    @Test
    void shouldSupportWeirdHttpMethods() {
        request.setMethod("PROPFIND");
        request.setRequestURI("/api/test");

        interceptor.preHandle(request, response, new Object());

        assertThatNoException()
                .isThrownBy(() ->
                        interceptor.afterCompletion(request, response, new Object(), null)
                );
    }

    // ============================================================
    // 12. URI vacío
    // ============================================================
    @Test
    void shouldSupportEmptyURI() {
        request.setRequestURI("");

        interceptor.preHandle(request, response, new Object());

        assertThatNoException()
                .isThrownBy(() ->
                        interceptor.afterCompletion(request, response, null, null)
                );
    }

    // ============================================================
    // 13. URI nulo
    // ============================================================
    @Test
    void shouldHandleNullURI() {
        request.setRequestURI(null);

        interceptor.preHandle(request, response, new Object());

        assertThatNoException()
                .isThrownBy(() ->
                        interceptor.afterCompletion(request, response, null, null)
                );
    }

    // ============================================================
    // 14. Múltiples llamadas consecutivas
    // ============================================================
    @Test
    void shouldHandleMultipleAfterCompletionCalls() {
        interceptor.preHandle(request, response, new Object());

        interceptor.afterCompletion(request, response, new Object(), null);
        interceptor.afterCompletion(request, response, new Object(), null);

        assertThat(response.getHeader("X-Elapsed-Time")).contains("ms");
    }

    // ============================================================
    // 15. Simulación de concurrencia
    // ============================================================
    @Test
    void shouldHandleConcurrentRequests() {
        for (int i = 0; i < 20; i++) {
            MockHttpServletRequest r = new MockHttpServletRequest();
            MockHttpServletResponse s = new MockHttpServletResponse();

            interceptor.preHandle(r, s, new Object());
            interceptor.afterCompletion(r, s, new Object(), null);

            assertThat(s.getHeader("X-Elapsed-Time")).isNotNull();
        }
    }

    // ============================================================
    // 16. Header X-Elapsed-Time siempre termina con "ms"
    // ============================================================
    @Test
    void elapsedTimeShouldAlwaysEndWithMs() {
        interceptor.preHandle(request, response, new Object());
        interceptor.afterCompletion(request, response, new Object(), null);

        assertThat(response.getHeader("X-Elapsed-Time")).endsWith("ms");
    }

    // ============================================================
    // 17. El valor antes del "ms" es numérico
    // ============================================================
    @Test
    void elapsedTimeValueShouldBeNumeric() {
        interceptor.preHandle(request, response, new Object());
        interceptor.afterCompletion(request, response, new Object(), null);

        String header = response.getHeader("X-Elapsed-Time");
        String number = header.replace("ms", "");

        assertThat(number).matches("\\d+");
    }


}
