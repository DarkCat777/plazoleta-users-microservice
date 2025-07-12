package com.pragma.users.infrastructure.config.init;

import com.pragma.users.domain.model.RoleName;
import com.pragma.users.infrastructure.adapter.output.model.JpaRoleEntity;
import com.pragma.users.infrastructure.adapter.output.model.JpaUserEntity;
import com.pragma.users.infrastructure.adapter.output.repository.JpaRoleRepository;
import com.pragma.users.infrastructure.adapter.output.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class UserDataInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final JpaRoleRepository roleRepository;
    private final JpaUserRepository userRepository;

    @Override
    public void run(String... args) {
        insertRolesIfMissing();
        insertAdminIfMissing();
    }

    private void insertRolesIfMissing() {
        if (roleRepository.count() == 0) {
            List<JpaRoleEntity> roles = List.of(
                    createRole(RoleName.ADMINISTRATOR.name(), "Usuario con acceso total al sistema."),
                    createRole(RoleName.OWNER.name(), "Propietario de un centro u organización."),
                    createRole(RoleName.EMPLOYEE.name(), "Empleado que trabaja para un propietario."),
                    createRole(RoleName.CUSTOMER.name(), "Usuario cliente del sistema.")
            );
            roleRepository.saveAll(roles);
        }
    }

    private JpaRoleEntity createRole(String name, String description) {
        JpaRoleEntity role = new JpaRoleEntity();
        role.setName(name);
        role.setDescription(description);
        return role;
    }

    private void insertAdminIfMissing() {
        if (userRepository.findByEmail("admin@pragma.com").isPresent()) return;

        JpaRoleEntity adminRole = roleRepository.findByName(RoleName.ADMINISTRATOR.name())
                .orElseThrow(() -> new IllegalStateException("Role ADMINISTRATOR no encontrado"));

        JpaUserEntity admin = new JpaUserEntity();
        admin.setFirstname("Admin");
        admin.setLastname("Principal");
        admin.setIdentityDocument("00000000");
        admin.setPhoneNumber("999999999");
        admin.setBirthdate(LocalDate.of(1990, 1, 1));
        admin.setEmail("admin@pragma.com");
        admin.setPassword(passwordEncoder.encode("adminPassword123")); // adminPassword123
        admin.setRole(adminRole);

        userRepository.save(admin);
    }
}
