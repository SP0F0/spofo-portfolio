package spofo.auth.domain;

public class MemberInfoHolder {

    private static final ThreadLocal<MemberInfo> memberInfoHolder = new ThreadLocal<>();

    public static MemberInfo get() {
        return memberInfoHolder.get();
    }

    public static void set(Long memberId) {
        MemberInfo memberInfo = MemberInfo.builder()
                .id(memberId)
                .build();

        memberInfoHolder.set(memberInfo);
    }

    public static void clear() {
        memberInfoHolder.remove();
    }
}
