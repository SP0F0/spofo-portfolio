package spofo.portfolio.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

public enum IncludeType {

    Y, N;

    @JsonCreator
    public static IncludeType from(String s) {
        return Objects.equals(s, "true") ? IncludeType.Y : IncludeType.N;
    }
}
