package br.com.erudio.controllers.docs;

import br.com.erudio.data.dto.security.AccountCredentialsDTO;
import br.com.erudio.data.dto.v1.PersonDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface AuthControllerDocs {
    @Operation(summary = "Authenticates an user and returns a token", description = "Authenticates an user and returns a token", tags = {"JSON Web Token"})
    ResponseEntity<?> signin(AccountCredentialsDTO credentials);

    @Operation(summary = "Refresh token for authenticated user and returns a token", description = "Refresh token for authenticated user and returns a token", tags = {"JSON Web Token"})
    ResponseEntity<?> refreshToken(String username, String refreshToken);

    @Operation(summary = "Create User", description = "Create User", tags = {"JSON Web Token"})
    AccountCredentialsDTO create(AccountCredentialsDTO credentials);
}
