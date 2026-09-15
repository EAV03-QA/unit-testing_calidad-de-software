package com.saucedemo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Deshabilitado: el alcance del entregable corresponde estrictamente a pruebas unitarias aisladas sin contexto de base de datos")
public class SaucedemoApplicationTests {

    @Test 
    void contextLoads() {
    }
}
