package spofo.global.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Server {

    AUTHSERVER("http://auth.spofo.net:8080"),
    STOCKSERVER("http://stock.spofo.net:8080");

    private String url;

    public String getUri(String uri) {
        return this.url + uri;
    }
}
