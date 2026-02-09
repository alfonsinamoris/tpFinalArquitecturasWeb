package org.example.cuenta.Service;

import org.example.cuenta.Repository.CuentaRepository;
import org.example.cuenta.entity.Cuenta;

import java.util.List;

import org.example.cuenta.feignClients.MercadoPagoClient;
import org.springframework.stereotype.Service;


@Service
public class CuentaService {

    CuentaRepository cuentaRepository;
    MercadoPagoClient mercadoPagoClient;

    public CuentaService(CuentaRepository cuentaRepository, MercadoPagoClient mercadoPagoClient) {
        this.cuentaRepository = cuentaRepository;
        this.mercadoPagoClient = mercadoPagoClient;
    }

    public List<Cuenta> getAll(){
        return cuentaRepository.findAll();
    }

    public Cuenta save(Cuenta cuenta){
        Cuenta nuevaCuenta;
        nuevaCuenta = cuentaRepository.save(cuenta);
        return nuevaCuenta;
    }


    public Cuenta getByUserId(String id){
        return cuentaRepository.findById(id).orElse(null);
    }

    public Cuenta update(String id ,Cuenta cuenta){
        if (cuentaRepository.findById(id).isEmpty()) {
            return null;
        }
        cuenta.setNroCuenta(id);
        return cuentaRepository.save(cuenta);
    }



    public Cuenta findById(String id){
        return cuentaRepository.findById(id).orElse(null);
    }

    // MOCK DE MERCADO PAGO.
    public Cuenta cargarSaldo(String id, double saldo, String tokenPago){
        Cuenta cuenta = cuentaRepository.findById(id).orElse(null);
        if (!mercadoPagoClient.validarPago(saldo, tokenPago)) {
            throw new RuntimeException("El pago fue rechazado.");
        }
        if (cuenta == null) {
            return null;
        }
        double montoActual = cuenta.getMonto();
        cuenta.setMonto(montoActual + saldo);
        return cuentaRepository.save(cuenta);
    }

    public List<String> getUsuariosAsociados(String id) {
        Cuenta cuenta = cuentaRepository.findById(id).orElse(null);
        return cuenta.getUsuarios();
    }

    /**
     * Busca la cuenta a la que pertenece el usuario.
     * Si un usuario puede tener varias cuentas, retorna la lista.
     */
    public List<Cuenta> getCuentasByUserId(String userId){
        return cuentaRepository.findByUsuariosContaining(userId);
    }

    public Cuenta anularCuenta(String id) {
        Cuenta cuenta = cuentaRepository.findById(id).orElse(null);
        if (cuenta == null) {
            return null;
        }
        // Cambia el estado a false (anulada)
        cuenta.setEstado(false);
        return cuentaRepository.save(cuenta);
    }
}
