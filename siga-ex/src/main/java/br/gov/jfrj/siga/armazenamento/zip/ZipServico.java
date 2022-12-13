package br.gov.jfrj.siga.armazenamento.zip;

import static br.gov.jfrj.siga.ex.util.Compactador.adicionarStream;
import static br.gov.jfrj.siga.ex.util.Compactador.compactarStream;
import static br.gov.jfrj.siga.ex.util.Compactador.descompactarStream;
import static br.gov.jfrj.siga.ex.util.Compactador.listarStream;
import static br.gov.jfrj.siga.ex.util.Compactador.removerStream;
import static java.util.Objects.nonNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExArquivoFilesystem;

public abstract class ZipServico {

	private static final Logger log = Logger.getLogger(ZipServico.class);

	private static final int CODIGO_ERRO_ZIP_SERVICO = 1050;

	private ZipServico() {}

	public static File referenciaArquivo(ExArquivoFilesystem exArquivo) {
		try {
			Path caminhoBase = ZipPropriedades.getInstance().obterCaminhoBase();
			File zipFile = exArquivo.getPathConteudo(caminhoBase).toFile();
			FileUtils.forceMkdir(zipFile.getParentFile());
			log.debugf("Capturando referência para arquivo da tabela de \"%s\" ID=%d %s", exArquivo.getNomeTabela(), exArquivo.getId(), zipFile.getAbsolutePath());
			return zipFile;
		} catch (Exception e) {
			final String mensagem = "[ZIP FILE] [I/O READ] "
					+ "Não foi possível fazer referencia ao arquivo registrado na tabela \""
					+ exArquivo.getNomeTabela()
					+ "\" no ID=" + exArquivo.getId();
			throw new AplicacaoException(mensagem, CODIGO_ERRO_ZIP_SERVICO, e);
		}
	}

	public static List<String> nomesItens(@NotNull ExArquivoFilesystem exArquivo) {
		byte[] zipBytes = ler(exArquivo);
		return nomesItens(zipBytes);
	}

	public static List<String> nomesItens(@NotNull byte[] zipBytes) {
		List<String> nomesItens = new ArrayList<>();
		try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
			ZipEntry zipEntry = zipInputStream.getNextEntry();
			while (zipEntry != null) {
				nomesItens.add(zipEntry.getName());
				zipEntry = zipInputStream.getNextEntry();
			}
			return nomesItens;
		} catch (IOException e) {
			throw new AplicacaoException("Não foi possível capturar nomes dos itens ZIP a partir dos bytes repassados", CODIGO_ERRO_ZIP_SERVICO, e);
		}
	}

	public static List<ZipItem> listarItens(@NotNull byte[] zipBytes) {
		List<String> nomes = nomesItens(zipBytes);
		List<ZipItem> itens = new ArrayList<>(nomes.size());
		for (String nome : nomes) {
			itens.add(ZipItem.Tipo.porNomeItem(nome));
		}
		return itens;
	}

	public static byte[] lerItem(@NotNull ExArquivoFilesystem exArquivo, @NotNull ZipItem item) {
		byte[] zipBytes = ler(exArquivo);
		return lerItem(zipBytes, item);
	}

	public static byte[] lerItem(@NotNull byte[] zipBytes, @NotNull ZipItem item) {
		return descompactarStream(zipBytes, item);
	}

	public static byte[] ler(@NotNull ExArquivoFilesystem exArquivo) {
		try {
			File zipFile = referenciaArquivo(exArquivo);
			if (!zipFile.exists()) {
				return null;
			}

			final Instant start = Instant.now();
			final byte[] zipByteArray = FileUtils.readFileToByteArray(zipFile);
			if (log.isDebugEnabled()) {
				log.debug("[ZIP FILE] [I/O READ] [EXECUTION TIME]:  " + Duration.between(start, Instant.now()).getSeconds() + " seconds | " + zipFile.getAbsolutePath());
			}
			return zipByteArray;
		} catch (Exception e) {
			final String mensagem = "[ZIP FILE] [I/O READ] "
					+ "Não foi possível ler os dados do arquivo referenciado a partir do registro na tabela \""
					+ exArquivo.getNomeTabela()
					+ "\" no ID=" + exArquivo.getId();
			throw new AplicacaoException(mensagem, CODIGO_ERRO_ZIP_SERVICO, e);
		}
	}

	public static void gravarItem(@NotNull ExArquivoFilesystem exArquivo, byte[] itemBytes, @NotNull ZipItem item) {
		// Capturando dados do ZIP original (se existir) e atualizando seus itens
		byte[] originalZipBytes = ler(exArquivo);
		byte[] novoZipBytes = criarOuAtualizarZipComItem(originalZipBytes, itemBytes, item);
		gravar(exArquivo, novoZipBytes);
	}

	public static void gravar(@NotNull ExArquivoFilesystem exArquivo, byte[] zipBytes) {
		File zipFile = referenciaArquivo(exArquivo);
		try {
			if (zipBytes == null) {
				apagarArquivo(zipFile);
			} else {
				log.debugf("Despejando arquivo %s no filesystem", zipFile.getAbsolutePath());

				final Instant start = Instant.now();
				FileUtils.writeByteArrayToFile(zipFile, zipBytes, false);
				if (log.isDebugEnabled()) {
					log.debug("[ZIP FILE] [I/O WRITE] [EXECUTION TIME]: " + Duration.between(start, Instant.now()).getSeconds() + " seconds | " + zipFile.getAbsolutePath());
				}
			}
		} catch (Exception e) {
			throw new AplicacaoException("Não foi possível gravar arquivo do documento " + exArquivo.getId() + " no sistema de arquivos", CODIGO_ERRO_ZIP_SERVICO, e);
		}
	}

	public static void apagar(@NotNull ExArquivoFilesystem exArquivo) {
		File zipFile = referenciaArquivo(exArquivo);
		apagarArquivo(zipFile);
	}

	private static void apagarArquivo(File referencia) {
		try {
			if (referencia.exists()) {
				FileUtils.forceDelete(referencia);
			}
		} catch (Exception e) {
			throw new AplicacaoException("Não foi possível apagar os dados do arquivo " + referencia.getAbsolutePath() + " no sistema de arquivos", CODIGO_ERRO_ZIP_SERVICO, e);
		}
	}

	/**
	 * Cria ou altera um arquivo zip a partir dos dados a serem inseridos. Caso o
	 * parâmetro zipOriginal seja nulo, gera os bytes do novo arquivo ZIP. Se
	 * existir, atualiza seu conteúdo a partir do item enviado.
	 * 
	 * @param originalZipBytes
	 * @param zipItemBytes
	 * @param tipo
	 * @return
	 */
	private static byte[] criarOuAtualizarZipComItem(
			byte[] originalZipBytes,
			byte[] zipItemBytes,
			@NotNull ZipItem tipo) {

		final String nome = tipo.getNome();
		if (ArrayUtils.isEmpty(originalZipBytes) || CollectionUtils.isEmpty(listarStream(originalZipBytes))) {
			return nonNull(zipItemBytes) ? compactarStream(nome, zipItemBytes) : null;
		}
		return nonNull(zipItemBytes)
				? adicionarStream(nome, zipItemBytes, originalZipBytes)
				: removerStream(nome, originalZipBytes);
	}

}
