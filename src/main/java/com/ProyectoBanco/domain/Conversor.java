

package com.ProyectoBanco.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;


/**
 *
 * @author kcalm
 */
@Data
@Entity
@Table(name = "conversor")
public class Conversor implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conversion")        
    private Long idConversion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "moneda_origen", nullable = false)
    private Moneda monedaOrigen;

    @Enumerated(EnumType.STRING)
    @Column(name = "moneda_destino", nullable = false)
    private Moneda monedaDestino;

    @Column(name = "tasa_conversion", nullable = false, precision = 10, scale = 4)
    private BigDecimal tasaConversion;

    public enum Moneda {
        CRC, USD, EUR
    }
}

//CREATE TABLE conversor (
//    id_conversion INT AUTO_INCREMENT PRIMARY KEY,
//    moneda_origen ENUM('CRC', 'USD', 'EUR') NOT NULL,
//    moneda_destino ENUM('CRC', 'USD', 'EUR') NOT NULL,
//    tasa_conversion DECIMAL(10,4) NOT NULL,
//    monto_origen DECIMAL(15,2) NOT NULL
//); 
  