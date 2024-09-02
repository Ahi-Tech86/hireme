package testFactories;

import com.schizoscrypt.factories.RefreshTokenEntityFactory;
import com.schizoscrypt.storage.entities.RefreshTokenEntity;
import com.schizoscrypt.storage.entities.UserEntity;
import com.schizoscrypt.storage.enums.AppRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class RefreshTokenEntityFactoryTest {

    @InjectMocks
    private RefreshTokenEntityFactory factory;

    private String token;
    private UserEntity user;
    private Date expirationTime;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = UserEntity.builder()
                .id(1L)
                .firstname("Billy")
                .lastname("Harrington")
                .email("harrington@gmail.com")
                .password("300bucks")
                .role(AppRole.WORKER_ROLE)
                .build();

        token = "new-Refresh-Token";
        expirationTime = new Date(System.currentTimeMillis() + 100000);
    }

    @Test
    public void testMakeRefreshTokenEntity() {
        RefreshTokenEntity refreshToken = factory.makeRefreshTokenEntity(user, token, expirationTime);

        assertNotNull(refreshToken);
        assertEquals(token, refreshToken.getToken());
        assertEquals(user, refreshToken.getUser());
        assertNotNull(refreshToken.getCreateAt());
        assertEquals(expirationTime, refreshToken.getExpiresAt());
    }
}
