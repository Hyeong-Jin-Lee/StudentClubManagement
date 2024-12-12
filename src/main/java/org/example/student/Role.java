package org.example.student;

public enum Role {
    LEADER("회장"),
    MANAGER("관리자"),
    MEMBER("회원"),
    BMEMBER("예비 회원");

    private final String role;

    Role(String role){
        this.role = role;
    }

    public String getRoleName(){
        return role;
    }

    // int 값을 Role로 변환하는 메서드
    public static Role fromInt(int i) {
        for (Role r : Role.values()) {
            if (r.ordinal() == i) {
                return r;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + i);
    }
}
