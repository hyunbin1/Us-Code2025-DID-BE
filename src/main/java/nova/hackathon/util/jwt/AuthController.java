package nova.hackathon.util.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nova.hackathon.util.response.ApiResponse;
import nova.hackathon.util.security.AuthDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtil jwtUtil;

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<AuthDTO.TokenResponseDTO>> reissueAccessToken(
            @RequestHeader("Authorization") String refreshToken) {
        log.info("토큰 재발급 요청");

        AuthDTO.TokenResponseDTO newAccessToken = jwtUtil.reissueToken(refreshToken);

        log.info("새로운 Access Token 발급 완료");

        return ResponseEntity
                .ok(ApiResponse.success(newAccessToken));
    }
}
