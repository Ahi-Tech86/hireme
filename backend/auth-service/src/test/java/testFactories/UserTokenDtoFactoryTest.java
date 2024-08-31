package testFactories;

import com.schizoscrypt.dtos.UserDto;
import com.schizoscrypt.dtos.UserTokenDto;
import com.schizoscrypt.factories.UserTokenDtoFactory;
import com.schizoscrypt.storage.entities.UserEntity;
import com.schizoscrypt.storage.enums.AppRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTokenDtoFactoryTest {

    @Test
    public void testMakeUserTokenDto() {
        UserDto userDto = new UserDto();
        userDto.setFirstname("Billy");
        userDto.setLastname("Harrington");
        userDto.setEmail("harrington@gmail.com");
        userDto.setRole(AppRole.WORKER_ROLE);

        String accessToken = new String("accessToken");
        String refreshToken = new String("refreshToken");

        UserTokenDtoFactory factory = new UserTokenDtoFactory();

        UserTokenDto userTokenDto = factory.makeUserTokenDto(userDto, accessToken, refreshToken);

        assertEquals("Billy", userTokenDto.getFirstname());
        assertEquals("Harrington", userTokenDto.getLastname());
        assertEquals("harrington@gmail.com", userTokenDto.getEmail());
        assertEquals("accessToken", userTokenDto.getAccessToken());
        assertEquals("refreshToken", userTokenDto.getRefreshToken());
    }
}
