package com.gasber.appaddle.security;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

import java.security.Key;


@Component
public class JwtUtil {

// Clave secreta para firmar el token (idealmente ponerla en application.properties)
    private final String secret = "MiClaveUltraSecretaQueDebeSerMuyLargaParaHS256";

    private final Key key = Keys.hmacShaKeyFor(secret.getBytes());

    // Tiempo de expiración: 1 día (en milisegundos)
    private final long expiration = 1000 * 60 * 60 * 24;

    public String generarToken(String usuario) {
        return Jwts.builder()
                .setSubject(usuario)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String obtenerUsuarioDelToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean esValido(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
}
