package netlife.devmasters.booking.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MINUTES;

@Service
public class LoginTryService {
    private static final int TAMAÑO_CACHE = 1000;
	private static final int MAXIMO_INTENTOS = 5;
    private static final int INCREMENTO = 1;
    private LoadingCache<String, Integer> intentoLoginCache;

    public LoginTryService() {
        super();
        intentoLoginCache = CacheBuilder.newBuilder().expireAfterWrite(15, MINUTES)
                .maximumSize(TAMAÑO_CACHE).build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void retirarUsuarioDeCache(String username) {
        intentoLoginCache.invalidate(username);
    }

    public void agregarUsuarioACache(String username) {
        int attempts = 0;
        try {
            attempts = INCREMENTO + intentoLoginCache.get(username);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        intentoLoginCache.put(username, attempts);
    }

    public boolean excedeMaximoIntentos(String username) {
        try {
            return intentoLoginCache.get(username) >= MAXIMO_INTENTOS;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

}
