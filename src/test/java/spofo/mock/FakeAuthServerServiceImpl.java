package spofo.mock;

import java.util.Optional;
import spofo.auth.service.AuthServerService;

public class FakeAuthServerServiceImpl implements AuthServerService {

    @Override
    public Optional<Long> verify(String idToken) {
        return Optional.of(1L);
    }
}
