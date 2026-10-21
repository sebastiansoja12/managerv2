package com.warehouse.e2e;

import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class TerminalSoapClient {

    private static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";

    private static final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";

    private static final String TERMINAL_SOAP_ACTION = "\"http://warehouse.com/logistics/terminal/processTerminal\"";

    private static final String COLLECT_SOAP_ACTION = "\"http://warehouse.com/logistics/terminal/processCollect\"";

    private final String baseUrl;

    private final CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);

    private final HttpClient httpClient;

    public TerminalSoapClient(final String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .cookieHandler(cookieManager)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public SoapResponse sendTerminalRequest(final String envelope) {
        return sendSoapRequest(TERMINAL_SOAP_ACTION, envelope);
    }

    public SoapResponse sendCollectRequest(final String envelope) {
        return sendSoapRequest(COLLECT_SOAP_ACTION, envelope);
    }

    private SoapResponse sendSoapRequest(final String soapAction, final String envelope) {
        final HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + "/ws"))
                .timeout(Duration.ofSeconds(60))
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("Accept", "text/xml")
                .header("SOAPAction", soapAction)
                .header(CSRF_HEADER_NAME, fetchCsrfToken())
                .POST(HttpRequest.BodyPublishers.ofString(envelope))
                .build();
        final HttpResponse<String> response = send(request);
        return new SoapResponse(response.statusCode(), response.body());
    }

    private String fetchCsrfToken() {
        final HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + "/auth/csrf"))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET()
                .build();
        send(request);
        return cookieManager.getCookieStore().getCookies().stream()
                .filter(cookie -> CSRF_COOKIE_NAME.equals(cookie.getName()))
                .map(HttpCookie::getValue)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing " + CSRF_COOKIE_NAME + " cookie"));
    }

    private HttpResponse<String> send(final HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(exception);
        }
    }

    public record SoapResponse(int statusCode, String body) {

        public String text(final String localPath) {
            final NodeList nodes = nodes(localPath);
            if (nodes.getLength() == 0) {
                throw new AssertionError("Missing " + localPath + " in SOAP response: " + body);
            }
            return nodes.item(0).getTextContent();
        }

        public int count(final String localPath) {
            return nodes(localPath).getLength();
        }

        public List<Map<String, String>> elements(final String localPath) {
            final NodeList nodes = nodes(localPath);
            final List<Map<String, String>> elements = new ArrayList<>();
            for (int index = 0; index < nodes.getLength(); index++) {
                final Map<String, String> children = new LinkedHashMap<>();
                final NodeList childNodes = nodes.item(index).getChildNodes();
                for (int childIndex = 0; childIndex < childNodes.getLength(); childIndex++) {
                    final Node child = childNodes.item(childIndex);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        children.put(child.getLocalName(), child.getTextContent());
                    }
                }
                elements.add(children);
            }
            return elements;
        }

        private NodeList nodes(final String localPath) {
            try {
                final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                final Document document = factory.newDocumentBuilder().parse(new InputSource(new StringReader(body)));
                return (NodeList) XPathFactory.newInstance().newXPath()
                        .evaluate(toLocalNameExpression(localPath), document, XPathConstants.NODESET);
            } catch (final Exception exception) {
                throw new IllegalStateException("Cannot evaluate " + localPath + " on SOAP response: " + body, exception);
            }
        }

        private static String toLocalNameExpression(final String localPath) {
            final StringBuilder expression = new StringBuilder();
            for (final String element : localPath.split("/")) {
                expression.append(expression.isEmpty() ? "//" : "/")
                        .append("*[local-name()='")
                        .append(element)
                        .append("']");
            }
            return expression.toString();
        }
    }
}
