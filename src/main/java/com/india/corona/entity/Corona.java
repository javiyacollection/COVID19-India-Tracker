package com.india.corona.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CORONA_TB")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Corona {
	@Id
	private String stateName;
	private String active;
	private String confirmed;
	private String deaths;
	private String recovered;
}
