package daw.spring.component;

public interface CurrentUserInfo {
    default long getIdFromPrincipalName(String nameAsId) {
        return Long.valueOf(nameAsId);
    }
}
