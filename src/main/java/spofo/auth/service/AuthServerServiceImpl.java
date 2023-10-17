package spofo.auth.service;

import static java.util.Optional.ofNullable;
import static spofo.global.domain.enums.Server.AUTHSERVER;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import spofo.global.domain.exception.TokenNotValid;

@Service
@RequiredArgsConstructor
public class AuthServerServiceImpl implements AuthServerService {

    private final RestClient restClient;

    @Override
    public Optional<Long> verify(String idToken) {

        try {
            Long memberId = restClient.get()
                    .uri(AUTHSERVER.getUri("/auth/members/search"))
                    .retrieve()
                    .body(Long.class);

            return ofNullable(memberId);
        } catch (Exception e) {
            throw new TokenNotValid();
        }
    }
}
