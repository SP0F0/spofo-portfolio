package spofo.auth.service;

import static java.util.Optional.ofNullable;
import static spofo.global.domain.enums.Server.AUTHSERVER;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class AuthServerServiceImpl implements AuthServerService {

    private final RestClient restClient;

    @Override
    public Optional<Long> verify(String idToken) {
        Long memberId = restClient.get()
                .uri(AUTHSERVER.getUri("/auth/members/search"))
                .retrieve()
                .body(Long.class);

        return ofNullable(memberId);
    }
}
