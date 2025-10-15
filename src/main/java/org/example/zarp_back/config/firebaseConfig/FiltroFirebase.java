package org.example.zarp_back.config.firebaseConfig;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@Slf4j
public class FiltroFirebase extends OncePerRequestFilter {


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // rutas protegidas
        // rutas protegidas
        // rutas protegidas
        boolean isProtectedRoute =
                        path.contains("/clientes/verificacion-correo/") ||           // ClienteController
                        path.contains("/clientes/verificacion-documento/") ||        // ClienteController
                        path.contains("/save") ||                                    // GenericoControllerImpl
                        path.contains("/update/") ||                                 // GenericoControllerImpl
                        path.contains("/delete/") ||                                 // GenericoControllerImpl
                        path.contains("/toggleActivo/") ||                           // GenericoControllerImpl
                        path.contains("/ambientes/activos") ||                       // AmbienteController
                        path.contains("/verificacionClientes/activas") ||            // VerificacionClienteController
                        path.contains("/create-preference") ||                       // MercadoPagoController
                        path.contains("/createAuthClient/") ||                       // MercadoPagoController
                        path.contains("/guardarDireccionPaypal/") ||                 // PaypalController
                        path.contains("/crearOrdenPago") ||                          // PaypalController
                        path.contains("/agregar-mensaje/") ||                        // ConversacionController
                        path.contains("/conversaciones/cliente/") ||                 // ConversacionController
                        path.contains("/propiedades/reservas/") ||                   // PropiedadController
                        path.contains("/propiedades/verificacion/") ||               // PropiedadController
                        path.contains("/propiedades/aVerificar") ||                  // PropiedadController
                        path.contains("/reservas/cliente/") ||                       // ReservaController
                        path.contains("/reservas/propiedad/") ||                     // ReservaController
                        path.contains("/reservas/propietario/");                     // ReservaController
        // ReservaController


        return !isProtectedRoute;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        log.info("Authorization header received: {}", header);

        if (header != null) {
            String token = header.startsWith("Bearer ") ? header.substring(7) : header;

            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
                String uid = decodedToken.getUid();
                log.info("Firebase token verified successfully for UID: {}", uid);

                // Guardar UID en el request
                request.setAttribute("firebaseUid", uid);

                // Crear Authentication y setear en SecurityContext
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(uid, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (FirebaseAuthException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                log.error("FirebaseAuthException verifying token: {}", e.getMessage());
                return;
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                log.error("Unexpected error verifying Firebase token", e);
                return;
            }

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.error("Authorization header missing");
            return;
        }

        filterChain.doFilter(request, response);
    }
}