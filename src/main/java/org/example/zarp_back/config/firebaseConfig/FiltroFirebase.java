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
        String method = request.getMethod();
        log.info("Evaluando ruta: {} - método: {}", path, method);

        // Dejar pasar siempre las preflight OPTIONS (no las filtramos)
        if ("OPTIONS".equalsIgnoreCase(method)) {
            log.debug("Preflight OPTIONS - no se filtra");
            return true;
        }

        // rutas protegidas
        boolean isProtectedRoute =
                path.contains("/save") ||                                          // GenericoControllerImpl
                        path.contains("/update") ||
                        path.contains("/delete") ||
                        path.contains("/toggleActivo") ||
                        path.contains("/activos") ||
                        path.startsWith("/api/clientes/verificacion-correo") ||           // ClienteControlle
                        path.startsWith("/api/clientes/verificacion-documento") ||
                        path.startsWith("/api/verificacionClientes/activas") ||            // VerificacionClienteController
                        path.startsWith("/api/mercadoPago/create-preference") ||           // MercadoPagoController
                        path.startsWith("/api/mercadoPago/guardarCredenciales") ||
                        path.startsWith("/api/paypal/guardarDireccionPaypal") ||          // PaypalController
                        path.startsWith("/api/paypal/crearOrdenPago") ||
                        path.startsWith("/api/conversaciones/agregar-mensaje") ||         // ConversacionController
                        path.startsWith("/api/conversaciones/cliente") ||
                        path.startsWith("/api/propiedades/reservas") ||                   // PropiedadController
                        path.startsWith("/api/propiedades/verificacion") ||
                        path.startsWith("/api/propiedades/aVerificar") ||
                        path.startsWith("/api/reservas/cliente") ||                       // ReservaController
                        path.startsWith("/api/reservas/propiedad") ||
                        path.startsWith("/api/reservas/propietario")||
                        path.startsWith("/api/pagosPendientes");                        // PagoPendienteController

        if (path.startsWith("/api/clientes/save")) {
            isProtectedRoute = false;
        }

        log.info("¿Ruta protegida?: {}", isProtectedRoute);

        // shouldNotFilter -> true significa "NO ejecutar el filtro"
        return !isProtectedRoute;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Dejar pasar OPTIONS en la implementación defensiva también
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

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
