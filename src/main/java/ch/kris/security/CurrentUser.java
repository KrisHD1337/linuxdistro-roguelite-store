package ch.kris.security;

import ch.kris.model.Role;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import org.eclipse.microprofile.jwt.JsonWebToken;

@RequestScoped
public class CurrentUser {

    @Inject
    JsonWebToken jwt;

    public boolean isAdmin() {
        return jwt.getGroups() != null && jwt.getGroups().contains(Role.ADMIN.name());
    }

    public Long getUid() {
        String uidClaim = jwt.getClaim("uid");
        return uidClaim == null ? null : Long.valueOf(uidClaim);
    }

    public void requireOwnerOrAdmin(Long uid) {
        if (isAdmin()) {
            return;
        }
        Long ownUid = getUid();
        if (ownUid == null || !ownUid.equals(uid)) {
            throw new ForbiddenException("You may only access your own account.");
        }
    }
}
