package testFactories;

import com.schizoscrypt.dtos.UserDto;
import com.schizoscrypt.factories.UserDtoFactory;
import com.schizoscrypt.storage.entities.UserEntity;
import com.schizoscrypt.storage.enums.AppRole;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDtoFactoryTest {

    @Test
    public void testMakeUserDto() {
        // creating entity
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstname("Billy");
        userEntity.setLastname("Harrington");
        userEntity.setEmail("harrington@gmail.com");
        userEntity.setRole(AppRole.WORKER_ROLE);

        UserDtoFactory factory = new UserDtoFactory();

        UserDto user = factory.makeUserDto(userEntity);

        assertEquals("Billy", user.getFirstname());
        assertEquals("Harrington", user.getLastname());
        assertEquals("harrington@gmail.com", user.getEmail());
        assertEquals(AppRole.WORKER_ROLE, user.getRole());
    }
}
