package com.example.shopmebe.setting.country;

import com.example.shopmebe.exception.CountryNotFoundException;
import com.example.shopmebe.fileutil.FileUtil;
import com.example.shopmebe.fileutil.MapperUtil;
import com.example.shopmebe.testcontainers.AbstractIntegrationTest;
import com.shopme.common.entity.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CountryRestIntTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    private final String loginUrl = "/ShopmeAdmin/login";
    private final String secureEndpoint = "/ShopmeAdmin/v1/countries";

    @BeforeEach
    void setup() {

    }

    @Test
    void shouldFindAllCountries() {
        // Create a new TestRestTemplate with redirect and cookie support enabled
        TestRestTemplate restTemplate = new TestRestTemplate(TestRestTemplate.HttpClientOption.ENABLE_REDIRECTS, TestRestTemplate.HttpClientOption.ENABLE_COOKIES);

        // Build the absolute login URL using the captured port
        String loginAbsoluteUrl = "http://localhost:" + port + loginUrl;
        String secureEndpointAbsoluteUrl = "http://localhost:" + port + secureEndpoint;
        // Step 1: Retrieve CSRF token and session cookie from login page
        ResponseEntity<String> loginPageResponse = restTemplate.getForEntity(loginAbsoluteUrl, String.class);
        String csrfToken = extractCsrfToken(loginPageResponse.getBody()); // Extract token from HTML if necessary
        String sessionCookie = loginPageResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        assertThat(csrfToken).isNotNull();
        assertThat(sessionCookie).isNotNull();

        // Step 2: Prepare headers and form data with CSRF token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.COOKIE, sessionCookie); // Include session cookie
        headers.add("X-CSRF-TOKEN", csrfToken); // Include CSRF token in headers (if your app requires it here)

        MultiValueMap<String, String> loginForm = new LinkedMultiValueMap<>();
        loginForm.add("_csrf", csrfToken); // Add CSRF token to form data
        loginForm.add("email", "nam@codejava.net");
        loginForm.add("password", "nam2020");

        HttpEntity<MultiValueMap<String, String>> loginRequest = new HttpEntity<>(loginForm, headers);

        // Step 3: Execute login request with form data
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(loginAbsoluteUrl, loginRequest, String.class);
        String authCookie = loginResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertThat(authCookie).isNotNull();

        // Step 4: Access the secured endpoint with session cookie and CSRF token if needed
        headers.set(HttpHeaders.COOKIE, authCookie); // Use authenticated session cookie

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<CountryResponse[]> response = restTemplate.exchange(
                secureEndpointAbsoluteUrl, HttpMethod.GET, request, CountryResponse[].class);

        // Validate the response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    // Helper method to extract CSRF token from HTML body if necessary
    private String extractCsrfToken(String htmlBody) {
        // Simplified regex to extract the CSRF token value from HTML (update the regex to match your HTML structure)
        Pattern pattern = Pattern.compile("name=\"_csrf\" value=\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(htmlBody);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalStateException("CSRF token not found");
    }
}

