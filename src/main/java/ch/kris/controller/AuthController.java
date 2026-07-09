package ch.kris.controller;

import ch.kris.dto.AuthResponseDto;
import ch.kris.dto.LoginDto;
import ch.kris.dto.RegisterDto;
import ch.kris.model.User;
import ch.kris.service.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @POST
    @Path("/register")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User register(RegisterDto registerDto) {
        return authService.register(registerDto);
    }

    @POST
    @Path("/login")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public AuthResponseDto login(LoginDto loginDto) {
        return new AuthResponseDto(authService.login(loginDto));
    }
}
