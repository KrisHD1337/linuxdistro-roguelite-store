package ch.kris.service;

import ch.kris.dto.LoginDto;
import ch.kris.dto.RegisterDto;
import ch.kris.model.Role;
import ch.kris.model.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class AuthService {
    private static final Duration TOKEN_LIFESPAN = Duration.ofHours(1);

    private final AccountService accountService;

    public AuthService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Transactional
    public User register(RegisterDto registerDto) {
        if (registerDto.getEmail() == null || registerDto.getEmail().isBlank()) {
            throw new BadRequestException("Email is required.");
        }
        if (registerDto.getPassword() == null || registerDto.getPassword().isBlank()) {
            throw new BadRequestException("Password is required.");
        }
        if (User.find("email", registerDto.getEmail()).firstResult() != null) {
            throw new BadRequestException("Email is already registered.");
        }

        Long uid = registerDto.getUid();
        if (uid != null) {
            accountService.findAccountByUid(uid);
            if (User.count("uid", uid) > 0) {
                throw new BadRequestException("Account is already linked to another user.");
            }
        }

        User user = new User(null, registerDto.getEmail(), BcryptUtil.bcryptHash(registerDto.getPassword()), Role.USER, uid);
        user.persist();
        return user;
    }

    public String login(LoginDto loginDto) {
        User user = User.find("email", loginDto.getEmail()).firstResult();
        if (user == null || !BcryptUtil.matches(loginDto.getPassword(), user.getPassword())) {
            throw new WebApplicationException("Invalid email or password.", Response.Status.UNAUTHORIZED);
        }
        return issueToken(user);
    }

    private String issueToken(User user) {
        var builder = Jwt.upn(user.getEmail())
                .subject(String.valueOf(user.getId()))
                .groups(Set.of(user.getRole().name()))
                .expiresIn(TOKEN_LIFESPAN);

        if (user.getUid() != null) {
            builder.claim("uid", String.valueOf(user.getUid()));
        }

        return builder.sign();
    }
}
