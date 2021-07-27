CREATE TABLE siga.ex_boletim_doc (
    id_boletim_doc bigint NOT NULL,
    id_doc bigint,
    id_boletim bigint
);

CREATE SEQUENCE siga.ex_boletim_doc_id_boletim_doc_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_classificacao (
    id_classificacao bigint NOT NULL,
    codificacao character varying(13),
    descr_classificacao character varying(4000) NOT NULL,
    obs character varying(4000),
    his_idc_ini bigint,
    his_idc_fim bigint,
    his_ativo integer NOT NULL,
    his_id_ini bigint,
    his_dt_ini timestamp with time zone,
    his_dt_fim timestamp with time zone
);

CREATE SEQUENCE siga.ex_classificacao_id_classificacao_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_competencia (
    fg_competencia character varying(1) NOT NULL,
    id_pessoa bigint,
    id_cargo bigint,
    id_lotacao bigint,
    dt_ini_vig_competencia timestamp with time zone NOT NULL,
    dt_fim_vig_competencia timestamp with time zone,
    id_competencia bigint,
    id_funcao_confianca bigint,
    id_forma_doc bigint NOT NULL
);

CREATE TABLE siga.ex_configuracao (
    id_configuracao_ex bigint NOT NULL,
    id_tp_mov bigint,
    id_tp_doc bigint,
    id_tp_forma_doc bigint,
    id_forma_doc bigint,
    id_mod bigint,
    id_classificacao bigint,
    id_via bigint,
    id_nivel_acesso bigint,
    id_papel bigint
);

CREATE SEQUENCE siga.ex_configuracao_id_configuracao_ex_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_documento (
    id_doc bigint NOT NULL,
    num_expediente integer,
    ano_emissao integer,
    id_tp_doc bigint NOT NULL,
    id_cadastrante bigint NOT NULL,
    id_lota_cadastrante bigint NOT NULL,
    id_subscritor bigint,
    id_lota_subscritor bigint,
    descr_documento character varying(4000),
    dt_doc timestamp with time zone,
    dt_reg_doc timestamp with time zone NOT NULL,
    nm_subscritor_ext character varying(256),
    num_ext_doc character varying(32),
    id_arq bigint,
    conteudo_blob_doc bytea,
    nm_arq_doc character varying(256),
    conteudo_tp_doc character varying(128),
    id_destinatario bigint,
    id_lota_destinatario bigint,
    nm_destinatario character varying(256),
    dt_finalizacao timestamp with time zone,
    assinatura_blob_doc bytea,
    id_mod bigint,
    id_orgao_usu bigint,
    id_classificacao bigint,
    id_forma_doc bigint,
    id_orgao_destinatario bigint,
    id_orgao bigint,
    obs_orgao_doc character varying(256),
    nm_orgao_destinatario character varying(256),
    nm_funcao_subscritor character varying(128),
    fg_eletronico character varying(1) DEFAULT 'N'::character varying NOT NULL,
    num_antigo_doc character varying(32),
    id_lota_titular bigint,
    id_titular bigint,
    num_aux_doc character varying(32),
    dsc_class_doc character varying(4000),
    id_nivel_acesso bigint,
    id_doc_pai bigint,
    num_via_doc_pai integer,
    id_doc_anterior bigint,
    id_mob_pai bigint,
    num_sequencia smallint,
    num_paginas smallint,
    dt_doc_original timestamp with time zone,
    id_mob_autuado bigint,
    dnm_dt_acesso timestamp with time zone,
    dnm_acesso character varying(4000),
    dnm_id_nivel_acesso bigint,
    his_dt_alt timestamp with time zone DEFAULT '2021-04-22 15:51:43.271529-03'::timestamp with time zone NOT NULL,
    chave_doc character varying(10),
    id_protocolo bigint,
    dt_primeiraassinatura date,
    ordenacao_doc character varying(200),
    descr_documento_ai character varying(4000)
);

CREATE SEQUENCE siga.ex_documento_id_doc_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_documento_numeracao (
    id_documento_numeracao bigint NOT NULL,
    id_orgao_usu bigint NOT NULL,
    id_forma_doc bigint NOT NULL,
    ano_emissao integer NOT NULL,
    nr_documento integer NOT NULL,
    nr_inicial integer DEFAULT 1 NOT NULL,
    nr_final integer,
    fl_ativo smallint DEFAULT 1
);

CREATE SEQUENCE siga.ex_documento_numeracao_id_documento_numeracao_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_email_notificacao (
    id_email_notificacao bigint NOT NULL,
    id_lotacao bigint,
    id_pessoa bigint,
    id_lota_email bigint,
    id_pessoa_email bigint,
    email character varying(60)
);

CREATE SEQUENCE siga.ex_email_notificacao_id_email_notificacao_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_estado_doc (
    id_estado_doc bigint NOT NULL,
    desc_estado_doc character varying(128) NOT NULL,
    ordem_estado_doc smallint
);

CREATE SEQUENCE siga.ex_estado_doc_id_estado_doc_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_estado_tp_mov (
    id_estado_doc bigint NOT NULL,
    id_tp_mov bigint NOT NULL
);

CREATE TABLE siga.ex_forma_documento (
    id_forma_doc bigint NOT NULL,
    descr_forma_doc character varying(64) NOT NULL,
    sigla_forma_doc character varying(3) NOT NULL,
    id_tipo_forma_doc bigint NOT NULL,
    is_composto smallint
);

CREATE SEQUENCE siga.ex_forma_documento_id_forma_doc_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_mobil (
    id_mobil bigint NOT NULL,
    id_doc bigint NOT NULL,
    id_tipo_mobil bigint NOT NULL,
    num_sequencia smallint NOT NULL,
    dnm_ultima_anotacao character varying(500),
    dnm_num_primeira_pagina integer
);

CREATE SEQUENCE siga.ex_mobil_id_mobil_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_modelo (
    id_mod bigint NOT NULL,
    nm_mod character varying(128) NOT NULL,
    desc_mod character varying(256),
    id_arq bigint,
    conteudo_blob_mod bytea,
    conteudo_tp_blob character varying(128),
    nm_arq_mod character varying(256),
    id_classificacao bigint,
    id_forma_doc bigint,
    id_class_criacao_via bigint,
    id_nivel_acesso bigint,
    his_id_ini bigint,
    his_dt_ini timestamp with time zone,
    his_dt_fim timestamp with time zone,
    his_idc_ini bigint,
    his_idc_fim bigint,
    his_ativo integer NOT NULL,
    nm_diretorio character varying(128),
    his_ide character varying(48),
    marca_dagua character varying(13)
);

CREATE SEQUENCE siga.ex_modelo_id_mod_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_modelo_tp_doc_publicacao (
    id_mod bigint NOT NULL,
    id_doc_publicacao bigint NOT NULL
);

CREATE TABLE siga.ex_movimentacao (
    id_mov bigint NOT NULL,
    id_doc bigint,
    id_doc_pai bigint,
    id_tp_mov bigint NOT NULL,
    id_estado_doc bigint,
    id_tp_despacho bigint,
    id_cadastrante bigint,
    id_lota_cadastrante bigint,
    id_subscritor bigint,
    id_lota_subscritor bigint,
    dt_mov timestamp with time zone NOT NULL,
    dt_ini_mov timestamp with time zone NOT NULL,
    num_via smallint,
    id_arq bigint,
    conteudo_blob_mov bytea,
    id_mov_canceladora bigint,
    nm_arq_mov character varying(256),
    conteudo_tp_mov character varying(128),
    dt_fim_mov timestamp with time zone,
    id_lota_resp bigint,
    id_resp bigint,
    descr_mov character varying(500),
    assinatura_blob_mov bytea,
    id_destino_final bigint,
    id_lota_destino_final bigint,
    num_via_doc_pai smallint,
    id_doc_ref bigint,
    num_via_doc_ref smallint,
    obs_orgao_mov character varying(256),
    id_orgao bigint,
    id_mov_ref bigint,
    id_lota_titular bigint,
    id_titular bigint,
    nm_funcao_subscritor character varying(128),
    num_proc_adm integer,
    id_nivel_acesso bigint,
    dt_disp_publicacao timestamp with time zone,
    dt_efetiva_publicacao timestamp with time zone,
    dt_efetiva_disp_publicacao timestamp with time zone,
    pag_publicacao character varying(15),
    num_trf_publicacao integer,
    caderno_publicacao_dje character varying(1),
    id_mobil bigint,
    id_mob_ref bigint,
    num_paginas smallint,
    num_paginas_ori smallint,
    id_papel bigint,
    id_classificacao bigint,
    id_marcador bigint,
    id_identidade_audit bigint,
    ip_audit character varying(256),
    hash_audit character varying(1024),
    dt_timestamp timestamp with time zone DEFAULT '2021-04-22 15:51:43.271529-03'::timestamp with time zone,
    dt_param1 timestamp with time zone,
    dt_param2 timestamp with time zone
);

COMMENT ON COLUMN siga.ex_movimentacao.dt_timestamp IS 'Timestamp para permitir ordenar as movimentacoes sem utilizar o ID.';

COMMENT ON COLUMN siga.ex_movimentacao.dt_param1 IS 'Primeiro parâmetro opcional do tipo data';

COMMENT ON COLUMN siga.ex_movimentacao.dt_param2 IS 'Segundo parâmetro opcional do tipo data';

CREATE SEQUENCE siga.ex_movimentacao_id_mov_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_nivel_acesso (
    id_nivel_acesso bigint NOT NULL,
    nm_nivel_acesso character varying(50) NOT NULL,
    dsc_nivel_acesso character varying(256),
    grau_nivel_acesso integer
);

CREATE SEQUENCE siga.ex_nivel_acesso_id_nivel_acesso_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_numeracao (
    id_orgao_usu bigint NOT NULL,
    id_forma_doc bigint NOT NULL,
    ano_emissao integer NOT NULL,
    num_expediente bigint NOT NULL
);

CREATE TABLE siga.ex_papel (
    id_papel bigint NOT NULL,
    desc_papel character varying(20)
);

CREATE SEQUENCE siga.ex_papel_id_papel_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_preenchimento (
    id_preenchimento bigint NOT NULL,
    id_lotacao bigint NOT NULL,
    id_mod bigint NOT NULL,
    ex_nome_preenchimento character varying(256) NOT NULL,
    id_arq bigint,
    preenchimento_blob bytea
);

CREATE SEQUENCE siga.ex_preenchimento_id_preenchimento_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_protocolo (
    id_protocolo bigint NOT NULL,
    id_doc bigint NOT NULL,
    numero integer NOT NULL,
    data date NOT NULL,
    codigo character varying(12)
);

CREATE SEQUENCE siga.ex_protocolo_id_protocolo_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_sequencia (
    id_seq bigint NOT NULL,
    ano_emissao integer NOT NULL,
    numero integer NOT NULL,
    nr_inicial integer DEFAULT 1 NOT NULL,
    nr_final integer,
    fl_ativo smallint DEFAULT 1,
    tipo_sequencia smallint,
    zerar_inicio_ano character(1) DEFAULT '1'::bpchar
);

CREATE SEQUENCE siga.ex_sequencia_id_seq_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_situacao_configuracao (
    id_sit_configuracao bigint NOT NULL,
    dsc_sit_configuracao character varying(256)
);

CREATE SEQUENCE siga.ex_situacao_configuracao_id_sit_configuracao_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_temporalidade (
    id_temporalidade bigint NOT NULL,
    desc_temporalidade character varying(128) NOT NULL,
    valor_temporalidade bigint,
    id_unidade_medida bigint,
    his_id_ini bigint,
    his_dt_ini timestamp with time zone,
    his_dt_fim timestamp with time zone,
    his_idc_ini bigint,
    his_idc_fim bigint,
    his_ativo integer NOT NULL
);

CREATE SEQUENCE siga.ex_temporalidade_id_temporalidade_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_tipo_despacho (
    id_tp_despacho bigint NOT NULL,
    desc_tp_despacho character varying(256) NOT NULL,
    fg_ativo_tp_despacho character varying(1)
);

CREATE SEQUENCE siga.ex_tipo_despacho_id_tp_despacho_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_tipo_destinacao (
    id_tp_destinacao bigint NOT NULL,
    descr_tipo_destinacao character varying(256) NOT NULL,
    facilitador_dest character varying(4000)
);

CREATE SEQUENCE siga.ex_tipo_destinacao_id_tp_destinacao_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_tipo_documento (
    id_tp_doc bigint NOT NULL,
    descr_tipo_documento character varying(256) NOT NULL
);

CREATE SEQUENCE siga.ex_tipo_documento_id_tp_doc_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_tipo_forma_documento (
    id_tipo_forma_doc bigint NOT NULL,
    desc_tipo_forma_doc character varying(60),
    numeracao_unica smallint
);

CREATE SEQUENCE siga.ex_tipo_forma_documento_id_tipo_forma_doc_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_tipo_mobil (
    id_tipo_mobil bigint NOT NULL,
    desc_tipo_mobil character varying(20)
);

CREATE SEQUENCE siga.ex_tipo_mobil_id_tipo_mobil_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_tipo_movimentacao (
    id_tp_mov bigint NOT NULL,
    descr_tipo_movimentacao character varying(256)
);

CREATE SEQUENCE siga.ex_tipo_movimentacao_id_tp_mov_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_tp_doc_publicacao (
    id_doc_publicacao bigint NOT NULL,
    nm_doc_publicacao character varying(256),
    carater character varying(1)
);

CREATE SEQUENCE siga.ex_tp_doc_publicacao_id_doc_publicacao_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siga.ex_tp_forma_doc (
    id_forma_doc bigint NOT NULL,
    id_tp_doc bigint NOT NULL
);

CREATE TABLE siga.ex_tp_mov_estado (
    id_tp_mov bigint NOT NULL,
    id_estado_doc bigint NOT NULL
);

CREATE TABLE siga.ex_via (
    id_via bigint NOT NULL,
    id_classificacao bigint NOT NULL,
    id_destinacao bigint,
    cod_via character varying(2),
    id_temporal_arq_cor smallint,
    id_temporal_arq_int smallint,
    obs character varying(4000),
    id_destinacao_final smallint,
    his_id_ini bigint,
    his_dt_ini timestamp with time zone,
    his_dt_fim timestamp with time zone,
    his_idc_ini bigint,
    his_idc_fim bigint,
    his_ativo integer NOT NULL
);

CREATE SEQUENCE siga.ex_via_id_via_seq
    AS bigint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE ONLY siga.ex_boletim_doc ALTER COLUMN id_boletim_doc SET DEFAULT nextval('siga.ex_boletim_doc_id_boletim_doc_seq'::regclass);

ALTER TABLE ONLY siga.ex_classificacao ALTER COLUMN id_classificacao SET DEFAULT nextval('siga.ex_classificacao_id_classificacao_seq'::regclass);

ALTER TABLE ONLY siga.ex_configuracao ALTER COLUMN id_configuracao_ex SET DEFAULT nextval('siga.ex_configuracao_id_configuracao_ex_seq'::regclass);

ALTER TABLE ONLY siga.ex_documento ALTER COLUMN id_doc SET DEFAULT nextval('siga.ex_documento_id_doc_seq'::regclass);

ALTER TABLE ONLY siga.ex_documento_numeracao ALTER COLUMN id_documento_numeracao SET DEFAULT nextval('siga.ex_documento_numeracao_id_documento_numeracao_seq'::regclass);

ALTER TABLE ONLY siga.ex_email_notificacao ALTER COLUMN id_email_notificacao SET DEFAULT nextval('siga.ex_email_notificacao_id_email_notificacao_seq'::regclass);

ALTER TABLE ONLY siga.ex_estado_doc ALTER COLUMN id_estado_doc SET DEFAULT nextval('siga.ex_estado_doc_id_estado_doc_seq'::regclass);

ALTER TABLE ONLY siga.ex_forma_documento ALTER COLUMN id_forma_doc SET DEFAULT nextval('siga.ex_forma_documento_id_forma_doc_seq'::regclass);

ALTER TABLE ONLY siga.ex_mobil ALTER COLUMN id_mobil SET DEFAULT nextval('siga.ex_mobil_id_mobil_seq'::regclass);

ALTER TABLE ONLY siga.ex_modelo ALTER COLUMN id_mod SET DEFAULT nextval('siga.ex_modelo_id_mod_seq'::regclass);

ALTER TABLE ONLY siga.ex_movimentacao ALTER COLUMN id_mov SET DEFAULT nextval('siga.ex_movimentacao_id_mov_seq'::regclass);

ALTER TABLE ONLY siga.ex_nivel_acesso ALTER COLUMN id_nivel_acesso SET DEFAULT nextval('siga.ex_nivel_acesso_id_nivel_acesso_seq'::regclass);

ALTER TABLE ONLY siga.ex_papel ALTER COLUMN id_papel SET DEFAULT nextval('siga.ex_papel_id_papel_seq'::regclass);

ALTER TABLE ONLY siga.ex_preenchimento ALTER COLUMN id_preenchimento SET DEFAULT nextval('siga.ex_preenchimento_id_preenchimento_seq'::regclass);

ALTER TABLE ONLY siga.ex_protocolo ALTER COLUMN id_protocolo SET DEFAULT nextval('siga.ex_protocolo_id_protocolo_seq'::regclass);

ALTER TABLE ONLY siga.ex_sequencia ALTER COLUMN id_seq SET DEFAULT nextval('siga.ex_sequencia_id_seq_seq'::regclass);

ALTER TABLE ONLY siga.ex_situacao_configuracao ALTER COLUMN id_sit_configuracao SET DEFAULT nextval('siga.ex_situacao_configuracao_id_sit_configuracao_seq'::regclass);

ALTER TABLE ONLY siga.ex_temporalidade ALTER COLUMN id_temporalidade SET DEFAULT nextval('siga.ex_temporalidade_id_temporalidade_seq'::regclass);

ALTER TABLE ONLY siga.ex_tipo_despacho ALTER COLUMN id_tp_despacho SET DEFAULT nextval('siga.ex_tipo_despacho_id_tp_despacho_seq'::regclass);

ALTER TABLE ONLY siga.ex_tipo_destinacao ALTER COLUMN id_tp_destinacao SET DEFAULT nextval('siga.ex_tipo_destinacao_id_tp_destinacao_seq'::regclass);

ALTER TABLE ONLY siga.ex_tipo_documento ALTER COLUMN id_tp_doc SET DEFAULT nextval('siga.ex_tipo_documento_id_tp_doc_seq'::regclass);

ALTER TABLE ONLY siga.ex_tipo_forma_documento ALTER COLUMN id_tipo_forma_doc SET DEFAULT nextval('siga.ex_tipo_forma_documento_id_tipo_forma_doc_seq'::regclass);

ALTER TABLE ONLY siga.ex_tipo_mobil ALTER COLUMN id_tipo_mobil SET DEFAULT nextval('siga.ex_tipo_mobil_id_tipo_mobil_seq'::regclass);

ALTER TABLE ONLY siga.ex_tipo_movimentacao ALTER COLUMN id_tp_mov SET DEFAULT nextval('siga.ex_tipo_movimentacao_id_tp_mov_seq'::regclass);

ALTER TABLE ONLY siga.ex_tp_doc_publicacao ALTER COLUMN id_doc_publicacao SET DEFAULT nextval('siga.ex_tp_doc_publicacao_id_doc_publicacao_seq'::regclass);

ALTER TABLE ONLY siga.ex_via ALTER COLUMN id_via SET DEFAULT nextval('siga.ex_via_id_via_seq'::regclass);
