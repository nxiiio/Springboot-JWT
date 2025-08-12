package cl.duoc.worellana.authjwtspring_security.service;

import cl.duoc.worellana.authjwtspring_security.dto.AuthLoginRequest;
import cl.duoc.worellana.authjwtspring_security.dto.AuthRegisterRequest;
import cl.duoc.worellana.authjwtspring_security.dto.AuthResponse;
import cl.duoc.worellana.authjwtspring_security.entity.RoleEntity;
import cl.duoc.worellana.authjwtspring_security.entity.UserEntity;
import cl.duoc.worellana.authjwtspring_security.repository.RoleRepository;
import cl.duoc.worellana.authjwtspring_security.repository.UserRepository;
import cl.duoc.worellana.authjwtspring_security.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        user.getRoles().forEach(role -> authorityList.add(
                        new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));
        return new User(user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonExpired(),
                authorityList);
    }

    public AuthResponse loginUser(AuthLoginRequest authLoginRequest){
        String username = authLoginRequest.getUsername();
        String password = authLoginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.generateToken(authentication);
        return new AuthResponse(
                username,
                "User loged succesfuly",
                accessToken,
                true
        );
    }

    public Authentication authenticate(String username, String password){
        UserDetails userDetails = loadUserByUsername(username);
        if (userDetails == null){
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public AuthResponse registerUser(AuthRegisterRequest authRegisterRequest){
        String username = authRegisterRequest.getUsername();
        String password = authRegisterRequest.getPassword();
        List<String> roles = authRegisterRequest.getRoleRequest().getRoles();
        Set<RoleEntity> roleEntitySet = new HashSet<>(roleRepository.findRoleEntitiesByRoleEnumIn(roles));
        
        if (roleEntitySet.isEmpty()){
            throw new IllegalArgumentException("The roles specified does not exits");
        }

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roleEntitySet)
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonExpired(true)
                .isCredentialsNonExpired(true)
                .build();
        UserEntity created = userRepository.save(userEntity);
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        created.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));
        created.getRoles().stream()
                .flatMap(roleEntity -> roleEntity.getPermissions().stream())
                .forEach(permissionEntity -> authorities.add(new SimpleGrantedAuthority(permissionEntity.getName())));
        Authentication authentication = new UsernamePasswordAuthenticationToken(created.getUsername(), created.getPassword(), authorities);
        String accessToken = jwtUtils.generateToken(authentication);

        return AuthResponse.builder()
                .username(created.getUsername())
                .message("User created succesfuly")
                .token(accessToken)
                .status(true)
                .build();
    }
}
