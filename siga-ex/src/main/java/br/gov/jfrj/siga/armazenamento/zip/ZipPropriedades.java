package br.gov.jfrj.siga.armazenamento.zip;

import static org.apache.commons.io.FileUtils.getUserDirectoryPath;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.startsWithIgnoreCase;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.jfrj.siga.base.AplicacaoException;

public class ZipPropriedades {

	private static final Log log = LogFactory.getLog(ZipPropriedades.class);
	private static final ZipPropriedades INSTANCE = new ZipPropriedades();
	private static final String USER_HOME_MODULE_SUBDIR = ".sigaex";
	private static final String AMBIENTE_PROPRIEDADE = "siga.ambiente";
	private static final String ARMAZENAMENTO_PROPRIEDADE = "sigaex.diretorio.armazenamento.arquivos";
	private static final String ARQUIVO_EXEMPLO_ATIVO_PROPRIEDADE = "sigaex.arquivo.exemplo.ativo";
	private static final String ARQUIVO_EXEMPLO_CAMINHO = "/arquivo-exemplo.zip";

	private Path diretorioBase;

	private ZipPropriedades() {}

	public static final ZipPropriedades getInstance() {
		return INSTANCE;
	}

	public Path obterCaminhoBase() {
		if (this.diretorioBase != null) {
			return this.diretorioBase;
		}

		try {
			final String propertyDiretorioBaseArmazenamento = System.getProperty(ARMAZENAMENTO_PROPRIEDADE);
			this.diretorioBase = isEmpty(propertyDiretorioBaseArmazenamento)
					? this.obterCaminhoBasePadrao()
					: Paths.get(propertyDiretorioBaseArmazenamento);

			FileUtils.forceMkdir(this.diretorioBase.toFile());
			log.debug("Caminho base para armazenamento de arquivos: " + propertyDiretorioBaseArmazenamento);
			return this.diretorioBase;
		} catch (Exception e) {
			throw new AplicacaoException("Não foi possível obter caminho base para armazenamento a partir da propriedade " + ARMAZENAMENTO_PROPRIEDADE, 0, e);
		}
	}

	private Path obterCaminhoBasePadrao() {
		return Paths.get(getUserDirectoryPath(), USER_HOME_MODULE_SUBDIR);
	}

	private boolean isAmbienteDesenvolvimento() {
		final String ambiente = System.getProperty(AMBIENTE_PROPRIEDADE);
		return startsWithIgnoreCase(ambiente, "d");
	}

	public boolean isArquivoExemploAtivo() {
		final String propertyArquivoExemploAtivo = System.getProperty(ARQUIVO_EXEMPLO_ATIVO_PROPRIEDADE);
		return toBoolean(propertyArquivoExemploAtivo);
	}

	public byte[] obterArquivoExemplo() {
		if (!isAmbienteDesenvolvimento()) {
			throw new IllegalStateException("Não é possível buscar arquivo de exemplo em ambientes que não sejam de desenvolvimento");
		}
		if (!isArquivoExemploAtivo()) {
			throw new IllegalStateException("Não é possível buscar arquivo de exemplo porque a propriedade \"" + ARQUIVO_EXEMPLO_ATIVO_PROPRIEDADE + "\" não está ativa");
		}

		final InputStream inputStreamArquivoExemplo = this.getClass().getResourceAsStream(ARQUIVO_EXEMPLO_CAMINHO);
		try {
			return IOUtils.toByteArray(inputStreamArquivoExemplo);
		} catch (IOException e) {
			throw new IllegalStateException("Não foi possível buscar arquivo de exemplo", e);
		}
	}

}
