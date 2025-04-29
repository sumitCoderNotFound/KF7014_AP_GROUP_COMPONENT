package com.assessment.monitoringmicroservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;


/**
 * Represents a water quality reading for the Monitoring Microservice.
 * 
 * <p>The model class has various water quality parameters which is stored in
 * the database. Each record has a unique identifier, timestamp, and values 
 * for multiple chemical properties.</p>
 * 
 * @author Prathamesh Belnekar
 * @version 1.0
 */
@Schema(description = "Represents a water quality reading related to river water quality.")
@Entity
@Table(name= "river_water_quality_records")
public class WaterReading {

	/**
	 * Auto-generated UUID which uniquely identifies a water quality record.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Schema(description = "Auto-generated UUID for the record", example = "3ebb9a40-b706-4792-aefb-48fa9ec4fa40")
	private String id;

	/**
	 * Object ID of the water record.
	 */
	@Schema(description = "ObjectId is an id for the respective water record", example = "1115")
	private Integer objectId;

	/**
	 * pH level of the water sample.
	 */
	@Schema(description = "pH of the water sample.", example = "7.7")
	private Double phph;

	/**
	 * Alkalinity of the water in mg/L.
	 */
	@Schema(description = "Alkalinity of the water mg/L.", example = "95.0")
	private Double alkmgl;

	/**
	 *Conductivity of the water.
	 */
	@Schema(description = "Conductivity of water", example = "270.8")
	private Double conduscm;

	/**
	 * BOD of the water in mg/L.
	 */
	@Schema(description = "BOD of the water mg/L.", example = "2.4")
	private Double bodmgl;

	/**
	 * Nitrite concentration in the water in mg/L.
	 */
	@Schema(description = "Nitrite in the water mg/L.", example = "0.011")
	private Double no2nmgl;

	/**
	 * Copper concentration (CUSOL1) in the water in mg/L.
	 */
	@Schema(description = "Copper1 in the water mg/L.", example = "0.002")
	private Double cusol1mgl;

	/**
	 * Copper concentration (CUSOL2) in the water in µg/L.
	 */
	@Schema(description = "Copper2 in the water mg/L.", example = "0.76")
	private Double cusol2ugl;

	/**
	 * iron concentration in the water in µg/L.
	 */
	@Schema(description = "Iron present in water ug/L.", example = "146.5")
	private Double fesol1ugl;

	/**
	 * Zinc concentration in the water in µg/L.
	 */
	@Schema(description = "Zinc in the water ug/L.", example = "5.0")
	private Double znsolugl;

	/**
	 * Timestamp indicating when the water quality reading was recorded.
	 */
	@Schema(description = "Timestamp of the water quality reading.", example = "2025-03-16T12:30:00")
	@Column(name = "timestamp", columnDefinition = "TEXT")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private String timestamp;


	/**
	 * Constructs a new {@code WaterReading} with the default values.
	 */
	public WaterReading() {
	}


	// Getters and setters

	/**
	 * Retrieves the unique identifier of the water reading.
	 * 
	 * @return The {@code id} value.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Retrieves the object ID of the water record.
	 * 
	 * @return The {@code objectId} value.
	 */
	public Integer getObjectId() {
		return objectId;
	}

	/**
	 * Sets the object ID of the water record.
	 * 
	 * @param objectId The {@code objectId} to set.
	 */
	public void setObjectId(Integer objectId) {
		this.objectId = objectId;
	}

	/**
	 * Retrieves the pH level of the water.
	 * 
	 * @return The {@code phph} value.
	 */
	public Double getPhph() {
		return phph;
	}

	/**
	 * Sets the pH level of the water.
	 * 
	 * @param phph The {@code phph} value to set.
	 */
	public void setPhph(Double phph) {
		this.phph = phph;
	}

	/**
	 * Retrieves the alkalinity of the water in mg/L.
	 *
	 * @return The {@code alkmgl} value.
	 */
	public Double getAlkmgl() {
		return alkmgl;
	}

	/**
	 * Sets the alkalinity of the water in mg/L.
	 *
	 * @param alkmgl The alkalinity value to set.
	 */
	public void setAlkmgl(Double alkmgl) {
		this.alkmgl = alkmgl;
	}

	/**
	 * Retrieves the conductivity of the water.
	 *
	 * @return The {@code conduscm} value.
	 */
	public Double getConduscm() {
		return conduscm;
	}


	/**
	 * Sets the conductivity of the water.
	 *
	 * @param conduscm The conductivity value to set.
	 */
	public void setConduscm(Double conduscm) {
		this.conduscm = conduscm;
	}

	/**
	 * Retrieves the BOD of the water.
	 *
	 * @return The {@code bodmgl} value.
	 */
	public Double getBodmgl() {
		return bodmgl;
	}

	/**
	 * Sets the BOD of the water.
	 *
	 * @param bodmgl The BOD value to set.
	 */
	public void setBodmgl(Double bodmgl) {
		this.bodmgl = bodmgl;
	}

	/**
	 * Retrieves the nitrite concentration in the water.
	 *
	 * @return The {@code no2nmgl} value.
	 */
	public Double getNo2nmgl() {
		return no2nmgl;
	}

	/**
	 * Sets the nitrite concentration in the water.
	 *
	 * @param no2nmgl The nitrite concentration to set.
	 */
	public void setNo2nmgl(Double no2nmgl) {
		this.no2nmgl = no2nmgl;
	}

	/**
	 * Retrieves the copper concentration - CUSOL1 in the water.
	 *
	 * @return The {@code cusol1mgl} value.
	 */
	public Double getCusol1mgl() {
		return cusol1mgl;
	}

	/**
	 * Sets the copper concentration - CUSOL1 in the water.
	 *
	 * @param cusol1mgl The concentration of copper to set.
	 */
	public void setCusol1mgl(Double cusol1mgl) {
		this.cusol1mgl = cusol1mgl;
	}

	/**
	 * Retrieves the concentration of copper - CUSOL2 in the water.
	 *
	 * @return The {@code cusol2ugl} value.
	 */
	public Double getCusol2ugl() {
		return cusol2ugl;
	}

	/**
	 * Sets the copper concentration - CUSOL2 in the water.
	 *
	 * @param cusol2ugl The concentration of copper to set.
	 */
	public void setCusol2ugl(Double cusol2ugl) {
		this.cusol2ugl = cusol2ugl;
	}


	/**
	 * Retrieves the concentration of iron in the water.
	 *
	 * @return The {@code fesol1ugl} value.
	 */
	public Double getFesol1ugl() {
		return fesol1ugl;
	}

	/**
	 * Sets the iron concentration in the water.
	 *
	 * @param fesol1ugl The iron concentration to set.
	 */
	public void setFesol1ugl(Double fesol1ugl) {
		this.fesol1ugl = fesol1ugl;
	}

	/**
	 * Retrieves the zinc concentration in the water.
	 *
	 * @return The {@code znsolugl} value.
	 */
	public Double getZnsolugl() {
		return znsolugl;
	}

	/**
	 * Sets the zinc concentration in the water.
	 *
	 * @param znsolugl The zinc concentration to set.
	 */
	public void setZnsolugl(Double znsolugl) {
		this.znsolugl = znsolugl;
	}


	/**
	 * Sets the timestamp for the water quality reading.
	 * 
	 * @param timestamp The {@link LocalDateTime} timestamp.
	 */
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	/**
	 * Retrieves the timestamp of the water quality reading.
	 * 
	 * @return The {@code timestamp} as a {@link LocalDateTime}.
	 */
	public LocalDateTime getTimestamp() {
		return LocalDateTime.parse(this.timestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}
}





