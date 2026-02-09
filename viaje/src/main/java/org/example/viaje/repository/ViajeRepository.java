package org.example.viaje.repository;

import org.example.viaje.dto.ReporteMonopatinContadorViajes;
import org.example.viaje.dto.ReporteUsoDTO;
import org.example.viaje.entity.Viaje;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface ViajeRepository extends MongoRepository<Viaje,String> {
    List<Viaje> findByIdUsuario(String idUsuario);

    // Buscar todos los viajes de un monopatin
    List<Viaje> findByIdMonopatin(String idMonopatin);
    /**
     * Realiza una agregación para contar los viajes finalizados por monopatín en un año específico.
     *
     * @param year Año a filtrar.
     */
    @Aggregation(pipeline = {
            // FILTRAR POR AÑO (Usando el campo 'inicio')
            "{ $match: { " +
                    // Compara el año extraído del campo 'inicio' con el parámetro 'year'
                    "$expr: { $eq: [ { $year: '$inicio' }, ?0 ] } " +
                    "} }",

            //Agrupa por el ID del monopatín (idMonopatin)
            "{ $group: { " +
                    "'_id': '$idMonopatin', " + // Agrupa por el campo idMonopatin
                    "'cantidadViajes': { $sum: 1 } " + // Suma 1 por cada documento en el grupo
                    "} }",

            // Mapea los campos del grupo al DTO
            "{ $project: { " +
                    "'idMonopatin': '$_id', " + // Renombra _id (campo de agrupación) a idMonopatin (campo del DTO)
                    "'cantidadViajes': 1, " +
                    "'_id': 0 " + // Oculta el campo _id por defecto
                    "} }"
    })
    List<ReporteMonopatinContadorViajes> contadorViajesXAnio(int year);


    //punto h
    @Aggregation(pipeline = {
            "{ '$match': { " +
                    "'idUsuario': { $in: ?0 }, " +
                    "'inicio': { $gte: ?1, $lte: ?2 } " +
                    "} }",
            "{ '$group': { " +
                    "_id: null, " +
                    "totalTiempoMinutos: { $sum: '$tiempoUso' }, " +
                    "totalTiempoConPausaMinutos: { $sum: '$tiempoConPausa' }, " +
                    "totalKilometros: { $sum: '$kmRecorridos' } " +
                    "} }",
            "{ '$project': { " +
                    "_id: 0, " +
                    "totalTiempoMinutos: 1, " +
                    "totalTiempoConPausaMinutos: 1, " +
                    "totalKilometros: 1 " +
                    "} }"
    })
    ReporteUsoDTO tiempoUsoUsuario(List<String> userIds, LocalDate fechaInicio, LocalDate fechaFin);


    @Query("{ 'inicio': { $gte: ?0 }, 'fin': { $lte: ?1 } }")
    List<Viaje> findViajesEntreFechas(Date inicio, Date fin);
}