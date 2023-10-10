package spofo.auth.service;

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
        /*
        todo 추후에 인증서버에서 id 가져와야 됨
        restClient.get()
                .uri(Server.AUTHSERVER.getUri("/auth/members/search"))
                .retrieve()
                .body(String.class);
         */
        System.out.println("restClient로 authserver에 idToken 검증~");
        return Optional.ofNullable(1L);
    }
}
