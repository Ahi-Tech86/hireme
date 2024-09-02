package testFactories;

import com.schizoscrypt.dtos.UserDto;
import com.schizoscrypt.dtos.AuthenticatedUserDto;
import com.schizoscrypt.factories.AuthenticatedUserDtoFactory;
import com.schizoscrypt.storage.enums.AppRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthenticatedUserDtoFactoryTest {

    @Test
    public void testMakeUserTokenDto() {
        UserDto userDto = new UserDto();
        userDto.setFirstname("Billy");
        userDto.setLastname("Harrington");
        userDto.setEmail("harrington@gmail.com");
        userDto.setRole(AppRole.WORKER_ROLE);

        String accessToken = new String("accessToken");
        String refreshToken = new String("refreshToken");

        AuthenticatedUserDtoFactory factory = new AuthenticatedUserDtoFactory();

        AuthenticatedUserDto authenticatedUserDto = factory.makeUserTokenDto(userDto, accessToken, refreshToken);

        assertEquals("Billy", authenticatedUserDto.getFirstname());
        assertEquals("Harrington", authenticatedUserDto.getLastname());
        assertEquals("harrington@gmail.com", authenticatedUserDto.getEmail());
        assertEquals("accessToken", authenticatedUserDto.getAccessToken());
        assertEquals("refreshToken", authenticatedUserDto.getRefreshToken());
    }
}
