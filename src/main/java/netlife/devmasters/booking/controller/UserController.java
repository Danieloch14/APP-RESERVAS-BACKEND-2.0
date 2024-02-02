//TODO: revisar

package netlife.devmasters.booking.controller;

import jakarta.mail.MessagingException;
import netlife.devmasters.booking.domain.Rol;
import netlife.devmasters.booking.domain.RolUser;
import netlife.devmasters.booking.domain.dto.UserLoginDto;
import netlife.devmasters.booking.service.RolService;
import netlife.devmasters.booking.service.RolUserService;
import netlife.devmasters.booking.util.HttpResponse;
import netlife.devmasters.booking.domain.User;
import netlife.devmasters.booking.domain.dto.UserPrincipal;
import netlife.devmasters.booking.exception.ExcepcionsManagment;
import netlife.devmasters.booking.exception.domain.*;
import netlife.devmasters.booking.service.UserService;
import netlife.devmasters.booking.util.JWTTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static netlife.devmasters.booking.constant.EmailConst.EMAIL_SEND;
import static netlife.devmasters.booking.constant.FileConst.*;
import static netlife.devmasters.booking.constant.SecurityConst.HEADER_TOKEN_JWT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping("/api/v1/users")
public class UserController extends ExcepcionsManagment {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    private AuthenticationManager authenticationManager;
    private UserService service;
    private RolUserService rolUserService;
    private RolService rolService;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(
            AuthenticationManager authenticationManager,
            UserService userService,
            JWTTokenProvider jwtTokenProvider,
            RolUserService rolUserService, RolService rolService) {
        this.authenticationManager = authenticationManager;
        this.service = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.rolUserService = rolUserService;
        this.rolService = rolService;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserLoginDto user) throws DataException {
        ResponseEntity<User> response = null;
        User loginUser = service.findUserByUsername(user.getUsername());
        List<RolUser> rolUser = rolUserService.getAllByUsuario(Long.valueOf(loginUser.getIdUser()));
        for (RolUser rolUser1 : rolUser) {
            Optional<Rol> rolName = rolService.getById(rolUser1.getRolUserId().getIdRol());
            if (user.getRol().equals(rolName.get().getNombre())) {

                authenticate(user.getUsername(), user.getPassword());
                
                UserPrincipal userPrincipal = new UserPrincipal(loginUser);
                HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
                response = new ResponseEntity<>(loginUser, jwtHeader, OK);
            } else {
                throw new DataException("El usuario no tiene el rol indicado");
            }
        }

        return response;

    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException,
            UsernameExistExcepcion, EmailExistExcepcion, MessagingException, IOException, DataException {
        User newUser = service.register(user);
        return new ResponseEntity<>(newUser, OK);
    }

    @PutMapping("activeLock/{id}")
    public ResponseEntity<User> actualizarDatos(@PathVariable("id") Integer code,
            @RequestParam(name = "isActive", required = false) Boolean active,
            @RequestParam(name = "isNotLocked", required = false) Boolean isNotLocked) throws DataException {
        return service.getById(code).map(SavedData -> {
            Optional.ofNullable(isNotLocked).ifPresent(SavedData::setNotLocked);
            Optional.ofNullable(active).ifPresent(SavedData::setActive);
            User datasUpdated = null;

            try {
                datasUpdated = service.updatedUser(SavedData);
            } catch (UserNotFoundException | UsernameExistExcepcion | EmailExistExcepcion | IOException
                    | NotFileImageExcepcion e) {
                e.printStackTrace();
            } catch (DataException e) {
                throw new RuntimeException(e);
            }
            return new ResponseEntity<>(datasUpdated, HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/active")
    public ResponseEntity<Void> updateActive(@RequestParam("valide") Boolean valide,
            @RequestParam("username") String username)
            throws UserNotFoundException, UsernameExistExcepcion, EmailExistExcepcion, IOException,
            NotFileImageExcepcion {
        int updatedRegisters = service.UpdatedActive(valide, username);
        if (updatedRegisters == 1) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/not-locked")
    public ResponseEntity<Void> updateNotLocked(@RequestParam("valide") Boolean notLocked,
            @RequestParam("username") String username)
            throws UserNotFoundException, UsernameExistExcepcion, EmailExistExcepcion, IOException,
            NotFileImageExcepcion {
        int registrosActualizados = service.UpdatedNotLock(notLocked, username);
        if (registrosActualizados == 1) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable("id") Integer id, @RequestBody User user)
            throws UserNotFoundException, UsernameExistExcepcion, EmailExistExcepcion, IOException,
            NotFileImageExcepcion, DataException {
        User updatedUser = service.updatedUser(user);
        return new ResponseEntity<>(updatedUser, OK);
    }

    @GetMapping("/buscar/{username}")
    public ResponseEntity<User> getUser(@PathVariable("username") String username) {
        User user = service.findUserByUsername(username);
        return new ResponseEntity<>(user, OK);
    }

    // por query params
    @PostMapping("/buscarNombresApellidos")
    public ResponseEntity<List<User>> buscarUsuarios(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "lastname", required = false) String lastname) {

        List<User> usuarios = new ArrayList<>();

        if (name != null && !name.trim().isEmpty() && lastname != null && !lastname.trim().isEmpty()) {
            usuarios = service.findUsersByFullyName(name, lastname);
        } else if (name != null && !name.trim().isEmpty()) {
            usuarios = service.findUsersByName(name);
        } else if (lastname != null && !lastname.trim().isEmpty()) {
            usuarios = service.findUsersByLastname(lastname);
        }
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/buscarCorreo")
    public ResponseEntity<List<User>> getCorreo(@RequestParam String email) {
        List<User> users = service.findUsersByEmail(email);
        return new ResponseEntity<>(users, OK);
    }

    @PostMapping("/buscarUsuario")
    public ResponseEntity<User> getUserII(@RequestParam String username) {
        User users = service.findUserByUsername(username);
        return new ResponseEntity<>(users, OK);
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = service.getUsers();
        return new ResponseEntity<>(users, OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<HttpResponse> resetPassword(@RequestParam("username") String username,
            @RequestParam("password") String password)
            throws MessagingException, EmailNotFoundExcepcion, UserNotFoundException, IOException {
        service.resetPassword(username, password);
        return response(OK, EMAIL_SEND + " la dirección de email registrada para el usuario " + username);
    }

    @PutMapping("/password-changed")
    public ResponseEntity<HttpResponse> changePassword(@RequestParam("username") String username,
            @RequestParam("lastPassword") String lastPassword, @RequestParam("password") String password)
            throws MessagingException, EmailNotFoundExcepcion, UserNotFoundException, IOException {
        service.changePassword(username, lastPassword, password);
        return response(OK, "Contraseña cambiada correctamente");
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable("username") String username) throws Exception {
        service.deleteUser(username);
    }

    @GetMapping(path = "/image/{username}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("fileName") String fileName)
            throws IOException {
        return Files.readAllBytes(Paths.get(USER_FILE + username + FORWARD_SLASH + fileName));
    }

    @GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
        URL url = new URL(URL_TEMPORAL_PICTURE + username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int bytesRead;
            byte[] chunk = new byte[1024];
            while ((bytesRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    @PutMapping("/time-locked")
    public ResponseEntity<HttpResponse> lockUser(@RequestParam("username") String username,
            @RequestParam("days") int days)
            throws UserNotFoundException, IOException, NotFileImageExcepcion, UsernameExistExcepcion,
            EmailExistExcepcion {
        service.UpdatedLockTime(username, days);
        return response(OK, "Usuario bloqueado correctamente");
    }

    @GetMapping("/byDate")
    public ResponseEntity<List<User>> getUser(@RequestParam("year") int year, @RequestParam("month") int month,
            @RequestParam("day") int day) {
        List<User> users = service.findUserByTimeLockDate(day, month, year);
        return new ResponseEntity<>(users, OK);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(
                new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                        message),
                httpStatus);
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_TOKEN_JWT, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
