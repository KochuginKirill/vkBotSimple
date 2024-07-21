package myVkBot.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import myVkBot.Constants;
import myVkBot.entites.Event;
import myVkBot.enums.MethodApi;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.util.List;

@RequiredArgsConstructor
public abstract class BasicResponse {
    private static final Logger LOG = Logger.getLogger(BasicResponse.class);

    @Getter
    private final Event event;

    @Getter
    private final String accessToken;

    protected abstract List<NameValuePair> getQueryParameters();

    public void processResponse(MethodApi method) {
        try (CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build())
                .build()) {
            HttpGet httpGet = new HttpGet(Constants.VK_API_ENDPOINT + method.getMethodPath());
            httpGet.setURI(new URIBuilder(httpGet.getURI()).addParameters(getQueryParameters()).build());
            processResponse(client, httpGet);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void processResponse(CloseableHttpClient client, HttpGet httpGet) {
        try (CloseableHttpResponse response = client.execute(httpGet)) {
            LOG.debug(httpGet.toString());

            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            JsonNode jsonNode = new ObjectMapper().readTree(responseString);

            LOG.debug("Received: " + responseString);
            checkErrors(jsonNode.path("error"));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void checkErrors(JsonNode jsonNode) {
        if (!jsonNode.isEmpty()) {
            LOG.error("Received an error: '" + jsonNode.path("error_msg").asText() +
                    "' with code [" + jsonNode.path("error_code").asText() + "]\n" +
                    "The following request parameters were passed:\n" +
                    jsonNode.path("request_params").toPrettyString());
        }
    }
}
