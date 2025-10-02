package org.example.zarp_back.service;

import com.mercadopago.client.oauth.OauthClient;
import com.mercadopago.resources.oauth.CreateOauthCredential;
import org.example.zarp_back.model.entity.Cliente;
import org.example.zarp_back.model.entity.CredencialesMP;
import org.example.zarp_back.repository.ClienteRepository;
import org.example.zarp_back.service.utils.CryptoUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MercadoPagoServiceTest {

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @MockitoBean
    private ClienteRepository clienteRepository;

    @MockitoBean
    private OauthClient oauthClient;

    @MockitoBean
    private CryptoUtils cryptoUtils;

    @Test
    void refrescarToken_exitoso() throws Exception {
        // Setup
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        CredencialesMP cred = new CredencialesMP();
        cred.setRefreshToken("refreshTokenEncriptado");
        cliente.setCredencialesMP(cred);

        CreateOauthCredential newCreds = mock(CreateOauthCredential.class);
        when(newCreds.getAccessToken()).thenReturn("nuevoAccessToken");
        when(newCreds.getRefreshToken()).thenReturn("nuevoRefreshToken");
        when(newCreds.getExpiresIn()).thenReturn(3600L);

        when(cryptoUtils.decrypt("refreshTokenEncriptado")).thenReturn("refreshTokenPlano");
        when(cryptoUtils.encrypt("nuevoAccessToken")).thenReturn("accessTokenEncriptado");
        when(cryptoUtils.encrypt("nuevoRefreshToken")).thenReturn("refreshTokenEncriptadoNuevo");

        when(oauthClient.createCredential("refreshTokenPlano", null)).thenReturn(newCreds);

        // Execute
        mercadoPagoService.refrescarToken(cliente);

        // Verify
        verify(clienteRepository).save(cliente);
        assertNotNull(cliente.getCredencialesMP());
        assertEquals("accessTokenEncriptado", cliente.getCredencialesMP().getAccessToken());
        assertEquals("refreshTokenEncriptadoNuevo", cliente.getCredencialesMP().getRefreshToken());
        assertTrue(cliente.getCredencialesMP().getTokenExpiration().isAfter(LocalDateTime.now()));
    }

    @Test
    void refrescarToken_fallaPorOauth() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(2L);
        CredencialesMP cred = new CredencialesMP();
        cred.setRefreshToken("refreshTokenEncriptado");
        cliente.setCredencialesMP(cred);

        when(cryptoUtils.decrypt("refreshTokenEncriptado")).thenReturn("refreshTokenPlano");
        when(oauthClient.createCredential("refreshTokenPlano", null))
                .thenThrow(new RuntimeException("Falla de OAuth"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            mercadoPagoService.refrescarToken(cliente);
        });

        assertEquals("Error al refrescar token de Mercado Pago", ex.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void refrescarToken_sinCredenciales_noHaceNada() {
        Cliente cliente = new Cliente();
        cliente.setId(3L);
        cliente.setCredencialesMP(null);

        mercadoPagoService.refrescarToken(cliente);

        verifyNoInteractions(oauthClient);
        verifyNoInteractions(cryptoUtils);
        verifyNoInteractions(clienteRepository);
    }
}
