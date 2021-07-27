ALTER TABLE corporativo.cp_orgao_usuario ADD COLUMN sigla_orgao_usu_completa VARCHAR(32);

UPDATE corporativo.cp_orgao_usuario SET sigla_orgao_usu_completa = 'ZZZ' WHERE id_orgao_usu = 999999999;
