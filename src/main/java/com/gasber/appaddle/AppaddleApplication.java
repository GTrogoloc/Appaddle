package com.gasber.appaddle;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.gasber.appaddle.models.Administrador;
import com.gasber.appaddle.repositories.AdministradorRepository;

import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class AppaddleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppaddleApplication.class, args);
	}

	@Bean
public CommandLineRunner crearAdminInicial(ApplicationContext context) {
    return args -> {
        if (context.containsBean("administradorRepository")) {
            AdministradorRepository adminRepo = context.getBean(AdministradorRepository.class);

            if (adminRepo.count() == 0) {
                Administrador admin = new Administrador();
                admin.setUsuario("admin");
                admin.setContraseña("admin123");
                adminRepo.save(admin);
                System.out.println("Administrador por defecto creado: admin / admin123");
            }
        } else {
            System.out.println("AdministradorRepository no disponible (modo test probablemente), se salta el administrador por defecto.");
        }
    };
}
}