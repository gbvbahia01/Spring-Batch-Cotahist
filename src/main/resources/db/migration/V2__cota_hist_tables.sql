CREATE TABLE cotahist_header (
	id int8 NOT NULL,
	cod_ori varchar(255) NULL,
	dat_ger date NULL,
	nom_arq varchar(255) NULL,
	tip_reg int4 NULL,
	CONSTRAINT cotahist_header_pkey PRIMARY KEY (id)
);

CREATE TABLE cotahist_line (
	id int8 NOT NULL,
	cod_neg varchar(255) NULL,
	bdi int4 NULL,
	dt_preg date NULL,
	pre_ult numeric(38, 2) NULL,
	cod_isi varchar(255) NULL,
	dat_ven date NULL,
	dismes int4 NULL,
	especi varchar(255) NULL,
	fat_cot int4 NULL,
	ind_opc varchar(255) NULL,
	mod_ref varchar(255) NULL,
	nom_res varchar(255) NULL,
	prazo_t varchar(255) NULL,
	pre_abe numeric(38, 2) NULL,
	pre_exe numeric(38, 2) NULL,
	pre_max numeric(38, 2) NULL,
	pre_med numeric(38, 2) NULL,
	pre_min numeric(38, 2) NULL,
	pre_ofc numeric(38, 2) NULL,
	pre_ofv numeric(38, 2) NULL,
	pto_exe numeric(38, 2) NULL,
	qua_tot int8 NULL,
	tip_reg int4 NULL,
	tot_neg int4 NULL,
	tp_merc varchar(255) NULL,
	vol_tot int8 NULL,
	header_id int8 NULL,
	CONSTRAINT cotahist_line_cod_neg_dt_preg_prazo_t_key UNIQUE (cod_neg, dt_preg, prazo_t),
	CONSTRAINT cotahist_line_pkey PRIMARY KEY (id),
	CONSTRAINT fk_cotahist_line_header_id FOREIGN KEY (header_id) REFERENCES cotahist_header(id)
);
CREATE INDEX "codnego_cotahist_line_idx" ON cotahist_line USING btree (cod_neg, dt_preg);


CREATE TABLE cotahist_trailer (
	id int8 NOT NULL,
	cod_org varchar(255) NULL,
	dat_ger date NULL,
	nom_arq varchar(255) NULL,
	tip_reg int4 NULL,
	tot_reg int8 NULL,
	header_id int8 NULL,
	CONSTRAINT cotahist_trailer_pkey PRIMARY KEY (id),
	CONSTRAINT fk_cotahist_trailer_header_id FOREIGN KEY (header_id) REFERENCES cotahist_header(id)
);

CREATE SEQUENCE seq_cotahist_header
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1;
	
CREATE SEQUENCE seq_cotahist_line
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1;
	
CREATE SEQUENCE seq_cotahist_trailer
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1;