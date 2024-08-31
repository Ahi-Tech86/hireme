package testFactories;

import com.schizoscrypt.dtos.RegisterRequest;
import com.schizoscrypt.dtos.UserDto;
import com.schizoscrypt.factories.UserDtoFactory;
import com.schizoscrypt.factories.UserEntityFactory;
import com.schizoscrypt.storage.entities.UserEntity;
import com.schizoscrypt.storage.enums.AppRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserEntityFactoryTest {

    @Test
    public void testMakeUserEntity() {
        // creating register request
        RegisterRequest request = new RegisterRequest();
        request.setFirstname("Billy");
        request.setLastname("Harrington");
        request.setEmail("harrington@gmail.com");
        request.setRoleName("worker");

        UserEntityFactory factory = new UserEntityFactory();

        UserEntity user = factory.makeUserEntity(request, AppRole.WORKER_ROLE);

        assertEquals("Billy", user.getFirstname());
        assertEquals("Harrington", user.getLastname());
        assertEquals("harrington@gmail.com", user.getEmail());
        assertEquals(AppRole.WORKER_ROLE, user.getRole());
    }
}
