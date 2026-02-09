package org.example.viaje.service;

import org.example.viaje.dto.*;
import org.example.viaje.entity.Pausa;
import org.example.viaje.entity.Viaje;
import org.example.viaje.feignClients.*;
import org.example.viaje.repository.ViajeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ViajeService {

    ViajeRepository viajeRepository;

    // Clientes de otros microservicios
    MonopatinFeignClient monopatinFeignClient;
    ParadaFeingClient paradaFeignClient;
    TarifaFeignClient tarifaFeignClient;
    FacturacionFeignClient facturacionFeignClient;
    CuentaFeignClient cuentaFeignClient;
    UsuarioFeignClient usuarioFeignClient;

    // Servicios locales (para Pausa)
      PausaService pausaService;

    public ViajeService(ViajeRepository viajeRepository, MonopatinFeignClient monopatinFeignClient, ParadaFeingClient paradaFeignClient, TarifaFeignClient tarifaFeignClient, FacturacionFeignClient facturacionFeignClient, CuentaFeignClient cuentaFeignClient, UsuarioFeignClient usuarioFeignClient, PausaService pausaService) {
        this.viajeRepository = viajeRepository;
        this.monopatinFeignClient = monopatinFeignClient;
        this.paradaFeignClient = paradaFeignClient;
        this.tarifaFeignClient = tarifaFeignClient;
        this.facturacionFeignClient = facturacionFeignClient;
        this.cuentaFeignClient = cuentaFeignClient;
        this.usuarioFeignClient = usuarioFeignClient;
        this.pausaService = pausaService;
    }

    public List<Viaje> getAll(){
        return viajeRepository.findAll();
    }

    public Viaje save(Viaje viaje) {

        MonopatinDTO monopatinActual = monopatinFeignClient.getMonopatin(viaje.getIdMonopatin());
        MonopatinDTO monopatinUpdate = new MonopatinDTO();
        monopatinUpdate.setId(viaje.getIdMonopatin());
        monopatinUpdate.setEstado("en_uso");

        MonopatinDTO monopatinConfirmado = monopatinFeignClient.updateMonopatin(monopatinUpdate.getId(), monopatinUpdate);

        if (monopatinConfirmado != null && "en_uso".equals(monopatinConfirmado.getEstado())) {
            return viajeRepository.save(viaje);
        } else {
            throw new RuntimeException("No se pudo iniciar el viaje: Monopatín no disponible.");
        }
    }
    /**
     * Inicia un nuevo viaje.
     * @param viaje El objeto Viaje con idMonopatin, idUsuario, idParadaInicio, idTarifa.
     * @return El Viaje guardado.
     */
    public Viaje iniciarViaje(Viaje viaje) {

        MonopatinDTO monopatin = monopatinFeignClient.getMonopatin(viaje.getIdMonopatin());
        ParadaDTO paradaInicio = paradaFeignClient.getParada(viaje.getIdParadaInicio());

        if (monopatin == null || paradaInicio == null || !"disponible".equalsIgnoreCase(monopatin.getEstado())) {
            throw new RuntimeException("No se puede iniciar el viaje. Monopatín no disponible o parada inválida.");
        }

        //Marcar Monopatín como "en_uso"
        monopatin.setEstado("en_uso");
        monopatinFeignClient.updateMonopatin(monopatin.getId(), monopatin);

        //Registrar Viaje
        viaje.setInicio(new Date());
        viaje.setKmRecorridos(0); // Inicia en cero
        viaje.setFin(null); // Aún no finaliza

        return viajeRepository.save(viaje);
    }

    /**
     * Finaliza un viaje.
     * @param idViaje ID del viaje a finalizar.
     * @param idParadaFin ID de la parada donde se deja el monopatín.
     * @param kmRecorridosFinal Kilómetros reportados por el monopatín.
     * @return El Viaje finalizado.
     */
    public Viaje finalizarViaje(String idViaje, String idParadaFin, float kmRecorridosFinal) {
        Viaje viaje = viajeRepository.findById(idViaje).orElseThrow(() -> new RuntimeException("Viaje no encontrado."));

        // Validar Parada de destino
        ParadaDTO paradaFin = paradaFeignClient.getParada(idParadaFin);

        // Verificar que el monopatin se encuentra en una parada permitida
        if (paradaFin == null) {
            throw new RuntimeException("No se puede finalizar el viaje. El monopatín no está en una parada permitida.");
        }

        // Finalizar el registro del Viaje
        viaje.setFin(new Date());
        viaje.setKmRecorridos(kmRecorridosFinal);
        viaje.setIdParadaFin(idParadaFin);
        Viaje viajeFinalizado = viajeRepository.save(viaje);

        //Marcar Monopatín como "disponible"
        MonopatinDTO monopatin = new MonopatinDTO();
        monopatin.setId(viaje.getIdMonopatin());
        monopatin.setEstado("Disponible");
        monopatinFeignClient.updateMonopatin(monopatin.getId(), monopatin);

        calcularYCobrarViaje(viajeFinalizado);

        return viajeFinalizado;
    }
    public List<Viaje> byMonopatinId(String idMonopatin){
        return viajeRepository.findByIdMonopatin(idMonopatin);
    }

    /**
     * Calcula el costo final del viaje y notifica al MS Facturación.
     * @param viajeFinalizado La entidad Viaje con inicio/fin y IDs.
     */
    public void calcularYCobrarViaje(Viaje viajeFinalizado) {

        // Calcular la duracion del viaje
        long tiempoEnSegundos = viajeFinalizado.getFin().getTime() - viajeFinalizado.getInicio().getTime();
        long tiempoTotalMinutos = TimeUnit.MILLISECONDS.toMinutes(tiempoEnSegundos); // Incluye pausas

        List<Pausa> pausas = pausaService.findByViajeId(viajeFinalizado.getId());

        boolean aplicaRecargoExtraPausa = pausas.stream()
                .anyMatch(p -> p.getFin() != null &&
                        TimeUnit.MILLISECONDS.toMinutes(p.getFin().getTime() - p.getInicio().getTime()) > 15);

        LocalDate fechaInicioViaje = viajeFinalizado.getInicio()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();


        TarifaDTO tarifa = tarifaFeignClient.getTarifaVigente(fechaInicioViaje);
        CuentaDTO cuenta = cuentaFeignClient.getCuenta(viajeFinalizado.getIdCuenta());

        if (tarifa == null) {
            throw new RuntimeException("No se puede facturar: Tarifa no encontrada.");
        }
        if (cuenta == null || !cuenta.getEstado()) {
            throw new RuntimeException("No se puede facturar: Cuenta inválida o anulada.");
        }


        double costoPorMinutoBase = "PREMIUM".equalsIgnoreCase(cuenta.getTipoCuenta()) ?
                tarifa.getValorPremium() : tarifa.getValorComun();


        double costoTotal = costoPorMinutoBase * tiempoTotalMinutos;
        String detalle = "BÁSICA";
        if ("PREMIUM".equalsIgnoreCase(cuenta.getTipoCuenta())) {
            detalle = "PREMIUM";
        }


        if (aplicaRecargoExtraPausa) {
            double costoRecargoExtra = tarifa.getValorExtrapausa() * tiempoTotalMinutos;
            costoTotal += costoRecargoExtra;

        }
        //Notificar facturacion
        FacturacionDTO factura = new FacturacionDTO(
                new Date(),
                viajeFinalizado.getId(),
                viajeFinalizado.getIdUsuario(),
                costoTotal
        );
        facturacionFeignClient.registrarCobro(factura);
    }

    /**
     * Realiza la agregación de viajes, contando cuántos viajes tuvo cada monopatín en un año dado.
     * @param year Año a filtrar.
     * @return Lista de ReporteMonopatinContadorViajes.
     */
    public List<ReporteMonopatinContadorViajes> getConteoViajesPorMonopatinYAnio(int year) {
        return viajeRepository.contadorViajesXAnio(year);
    }

    public List<UsuarioUsoDTO> obtenerUsuariosMasActivos(Date inicio, Date fin, List<String> userIds) {

        List<Viaje> viajes = viajeRepository.findViajesEntreFechas(inicio, fin);

        // Filtrar viajes SOLO de esos usuarios
        Map<String, Double> acumuladoPorUsuario = viajes.stream()
                .filter(v -> userIds.contains(v.getIdUsuario()))
                .collect(Collectors.groupingBy(
                        Viaje::getIdUsuario,
                        Collectors.summingDouble(Viaje::getKmRecorridos)
                ));

        List<UsuarioUsoDTO> resultado = new ArrayList<>();
        for (String userId : acumuladoPorUsuario.keySet()) {
            resultado.add(new UsuarioUsoDTO(userId, acumuladoPorUsuario.get(userId)));
        }

        // Ordenar por km recorridos DESC
        resultado.sort(Comparator.comparingDouble(UsuarioUsoDTO::getKmRecorridos).reversed());

        return resultado;
    }


        //punto h
    public ReporteUsoDTO getReporteUsoDto(List<String> userIds, LocalDate fechaInicio, LocalDate fechaFin) {
        return viajeRepository.tiempoUsoUsuario(userIds, fechaInicio, fechaFin);
    }

    public void delete(Viaje viaje){
        viajeRepository.delete(viaje);
    }

    public Viaje findById(String id){
        return viajeRepository.findById(id).orElse(null);
    }

    public Viaje update(Viaje viaje){
        return viajeRepository.save(viaje);
    }

    public List<Viaje> byUserId(String userid){
        return viajeRepository.findByIdUsuario(userid);
    }
}
