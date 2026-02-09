package org.example.cuenta.Service;


import org.example.cuenta.feignClients.MercadoPagoClient;
import org.springframework.stereotype.Service;

@Service
public class MercadoPagoMock implements MercadoPagoClient {

    @Override
    public boolean validarPago(double monto, String tokenPago) {
        if ("TOKEN_FALLA".equalsIgnoreCase(tokenPago)) {
            System.out.println("Pago de $" + monto + " RECHAZADO .");
            return false;
        }
        // Simular el éxito: cualquier otro caso.
        System.out.println("Pago de $" + monto + " APROBADO.");
        return true;
    }
}
