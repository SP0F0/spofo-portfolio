package spofo.mock;

import java.util.Optional;
import org.springframework.stereotype.Service;
import spofo.auth.service.AuthServerService;

@Service
public class FakeAuthServerServiceImpl implements AuthServerService {

    @Override
    public Optional<Long> verify(String idToken) {
        return Optional.of(1L);
    }
}
