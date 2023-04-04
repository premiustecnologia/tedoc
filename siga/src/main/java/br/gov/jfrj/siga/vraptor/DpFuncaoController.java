package br.gov.jfrj.siga.vraptor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.download.InputStreamDownload;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaModal;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpBL;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoBL;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpFuncaoConfiancaDaoFiltro;
import br.gov.jfrj.siga.model.Selecionavel;

@Controller
public class DpFuncaoController extends SigaSelecionavelControllerSupport<DpFuncaoConfianca, DpFuncaoConfiancaDaoFiltro>{

	private Long orgaoUsu;
	

	/**
	 * @deprecated CDI eyes only
	 */
	public DpFuncaoController() {
		super();
	}

	@Inject
	public DpFuncaoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);
		
		setSel(new DpFuncaoConfianca());
		setItemPagina(10);
	}

	@Override
	protected DpFuncaoConfiancaDaoFiltro createDaoFiltro() {
		final DpFuncaoConfiancaDaoFiltro flt = new DpFuncaoConfiancaDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(getNome()));
		flt.setIdOrgaoUsu(orgaoUsu);
		if (flt.getIdOrgaoUsu() == null){
			flt.setIdOrgaoUsu(getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu());
		}
		return flt;
	}
	
	@Override
	protected Selecionavel selecionarPorNome(final DpFuncaoConfiancaDaoFiltro flt)
			throws AplicacaoException {
		// Procura por nome
		flt.setNome(Texto.removeAcentoMaiusculas(flt.getSigla()));
		flt.setSigla(null);
		final List<DpFuncaoConfianca> l = dao().consultarPorFiltro(flt);
		if (l != null)
			if (l.size() == 1)
				return (DpFuncaoConfianca) l.get(0);
		return null;
	}
	
	public boolean temPermissaoParaExportarDados() {
		return Boolean.valueOf(Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(), getTitular().getLotacao(),"SIGA;GI;CAD_FUNCAO;EXP_DADOS"));
	}
	
	@Get
	@Post
	@Path({"/app/funcao/buscar", "/funcao/buscar.action"})
	public void buscar(String nome, String postback, Integer paramoffset, Long idOrgaoUsu) throws Exception {
		if (postback == null){
			orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
		}else{
			orgaoUsu = idOrgaoUsu;
		}
		this.getP().setOffset(paramoffset);
		super.aBuscar(nome,postback);
		
		result.include("param", getRequest().getParameterMap());
		result.include("request",getRequest());
		result.include("itens",getItens());
		result.include("tamanho",getTamanho());
		result.include("orgaosUsu",getOrgaosUsu());
		result.include("idOrgaoUsu",orgaoUsu);
		result.include("nome",nome);
		result.include("postbak",postback);
		result.include("offset",paramoffset);
	}

	@Get("/app/funcao/selecionar")
	public void selecionar(String sigla) {
		String resultado =  super.aSelecionar(sigla);
		if (resultado == "ajax_retorno"){
			result.include("sel", getSel());
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_retorno.jsp");
		}else{
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_vazio.jsp");
		}
	}
	
	@Get("app/funcao/listar")
	public void lista(Integer paramoffset, Long idOrgaoUsu, String nome) throws Exception {
		if(CpConfiguracaoBL.SIGLAS_ORGAOS_ADMINISTRADORES.contains(getTitular().getOrgaoUsuario().getSigla())) {
			result.include("orgaosUsu", dao().listarOrgaosUsuarios());
		} else {
			CpOrgaoUsuario ou = CpDao.getInstance().consultarPorSigla(getTitular().getOrgaoUsuario());
			List<CpOrgaoUsuario> list = new ArrayList<CpOrgaoUsuario>();
			list.add(ou);
			result.include("orgaosUsu", list);
		}
		if(idOrgaoUsu != null) {
			DpFuncaoConfiancaDaoFiltro dpFuncao = new DpFuncaoConfiancaDaoFiltro();
			if(paramoffset == null) {
				paramoffset = 0;
			}
			dpFuncao.setIdOrgaoUsu(idOrgaoUsu);
			dpFuncao.setNome(Texto.removeAcento(nome));
			setItens(CpDao.getInstance().consultarPorFiltro(dpFuncao, paramoffset, 15));
			result.include("itens", getItens());
			result.include("tamanho", dao().consultarQuantidade(dpFuncao));
			
			result.include("idOrgaoUsu", idOrgaoUsu);
			result.include("nome", nome);
			
			if (getItens().size() == 0) result.include("mensagemPesquisa", "Nenhum resultado encontrado.");
		}
		setItemPagina(15);
		result.include("currentPageNumber", calculaPaginaAtual(paramoffset));
		result.include("temPermissaoParaExportarDados", temPermissaoParaExportarDados());
	}
	
	@Post
	@Path("app/funcao/exportarCsv")
	public Download exportarCsv(Long idOrgaoUsu, String nome) throws Exception {				
			
 		if(idOrgaoUsu != null) {
 			DpFuncaoConfiancaDaoFiltro dpFuncao = new DpFuncaoConfiancaDaoFiltro(nome, idOrgaoUsu);																
															
			List <DpFuncaoConfianca> lista = CpDao.getInstance().consultarPorFiltro(dpFuncao, 0, 0);
			
			if (lista.size() > 0) {				
				InputStream inputStream = null;
				StringBuffer texto = new StringBuffer();
				texto.append("Função de Confiança" + System.lineSeparator());
				
				for (DpFuncaoConfianca funcao : lista) {
					texto.append(funcao.getNomeFuncao() + ";");										
					texto.append(System.lineSeparator());
				}
				
				inputStream = new ByteArrayInputStream(texto.toString().getBytes("ISO-8859-1"));									
				
				return new InputStreamDownload(inputStream, "text/csv", "função_de_confiança.csv");
				
			} else {
				
				if(CpConfiguracaoBL.SIGLA_ORGAO_ROOT.contains(getTitular().getOrgaoUsuario().getSigla())) {
					result.include("orgaosUsu", dao().listarOrgaosUsuarios());
				} else {
					CpOrgaoUsuario ou = CpDao.getInstance().consultarPorSigla(getTitular().getOrgaoUsuario());
					List<CpOrgaoUsuario> list = new ArrayList<CpOrgaoUsuario>();
					list.add(ou);
					result.include("orgaosUsu", list);
				}
					result.include("idOrgaoUsu", idOrgaoUsu);			
					result.include("nome", nome);					
					result.include("mensagemPesquisa", "Nenhum resultado encontrado.");		
					result.include("temPermissaoParaExportarDados", temPermissaoParaExportarDados());
					result.use(Results.page()).forwardTo("/WEB-INF/page/dpFuncao/lista.jsp");
			}							
		}					

		return null;
	}
	
	@Get("/app/funcao/editar")
	public void edita(final Long id){
		if (id != null) {
			DpFuncaoConfianca funcao = dao().consultar(id, DpFuncaoConfianca.class, false);
			result.include("nmFuncao",funcao.getDescricao());
			result.include("idOrgaoUsu", funcao.getOrgaoUsuario().getId());
			result.include("nmOrgaousu", funcao.getOrgaoUsuario().getNmOrgaoUsu());
			
			List<DpPessoa> list = CpDao.getInstance().consultarPessoasComFuncaoConfianca(id);
			if(list.size() == 0) {
				result.include("podeAlterarOrgao", Boolean.TRUE);
			}
		}
		
		if(CpConfiguracaoBL.SIGLAS_ORGAOS_ADMINISTRADORES.contains(getTitular().getOrgaoUsuario().getSigla())) {
			result.include("orgaosUsu", dao().listarOrgaosUsuarios());
		} else {
			CpOrgaoUsuario ou = CpDao.getInstance().consultarPorSigla(getTitular().getOrgaoUsuario());
			List<CpOrgaoUsuario> list = new ArrayList<CpOrgaoUsuario>();
			list.add(ou);
			result.include("orgaosUsu", list);
		}
		result.include("request",getRequest());
		result.include("id",id);
		this.result.include("mensagem", "Operação realizada com sucesso!");
	}
	
	@Transacional
	@Post("/app/funcao/gravar")
	public void editarGravar(final Long id, 
							 final String nmFuncao, 
							 final Long idOrgaoUsu) throws Exception{
		assertAcesso("GI:Módulo de Gestão de Identidade;CAD_FUNCAO:Cadastrar Função de Confiança");
		
		if(nmFuncao == null)
			throw new AplicacaoException("Nome da função não informado");
		
		if(idOrgaoUsu == null)
			throw new AplicacaoException("Órgão não informado");
		
		if(nmFuncao != null && !nmFuncao.matches(Texto.FuncaoConfianca.REGEX_CARACTERES_PERMITIDOS)) 
			throw new AplicacaoException("Nome com caracteres não permitidos");
		
		DpFuncaoConfianca funcao = new DpFuncaoConfianca();
		
		funcao.setNomeFuncao(Texto.removeAcento(Texto.removerEspacosExtra(nmFuncao).trim()));
		CpOrgaoUsuario ou = new CpOrgaoUsuario();
		ou.setIdOrgaoUsu(idOrgaoUsu);
		funcao.setOrgaoUsuario(ou);
		
		funcao = CpDao.getInstance().consultarPorNomeOrgao(funcao);
		
		if(funcao != null && !funcao.getId().equals(id)) {
			throw new AplicacaoException("Nome da função já cadastrado!");
		}
		
		funcao = new DpFuncaoConfianca();
		
		List<DpPessoa> listPessoa = null;
		
		funcao = new DpFuncaoConfianca();	
		if (id == null) {
			funcao = new DpFuncaoConfianca();
			Date data = new Date(System.currentTimeMillis());
			funcao.setDataInicio(data);
			
		} else {
			funcao = dao().consultar(id, DpFuncaoConfianca.class, false);
			listPessoa = CpDao.getInstance().consultarPessoasComFuncaoConfianca(id);
			
		}
		funcao.setNomeFuncao(Texto.removerEspacosExtra(nmFuncao).trim());
		
		if (idOrgaoUsu != null && idOrgaoUsu != 0 && (listPessoa == null || listPessoa.size() == 0)) {
			CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
			orgaoUsuario = dao().consultar(idOrgaoUsu, CpOrgaoUsuario.class, false);	
			funcao.setOrgaoUsuario(orgaoUsuario);
		}
		
		try {
			dao().iniciarTransacao();
			dao().gravar(funcao);
			if(funcao.getIdFuncaoIni() == null && funcao.getId() != null) {
				funcao.setIdFuncaoIni(funcao.getId());
				funcao.setIdeFuncao(funcao.getId().toString());
				dao().gravar(funcao);
			}
			dao().commitTransacao();
			this.result.include("mensagem", "Operação realizada com sucesso!");
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}
		this.result.redirectTo(this).lista(0, null, "");
	}
	
	@Get("/app/funcao/carregarExcel")
	public void carregarExcel() {
		if(CpConfiguracaoBL.SIGLAS_ORGAOS_ADMINISTRADORES.contains(getTitular().getOrgaoUsuario().getSigla())) {
			result.include("orgaosUsu", dao().listarOrgaosUsuarios());
		} else {
			result.include("nmOrgaousu", getTitular().getOrgaoUsuario().getNmOrgaoUsu());	
		}
		
		result.use(Results.page()).forwardTo("/WEB-INF/page/dpFuncao/cargaFuncao.jsp");
	}
	
	@Transacional
	@Post("/app/funcao/carga")
	public Download carga( final UploadedFile arquivo, Long idOrgaoUsu) throws Exception {
		InputStream inputStream = null;
		try {
			String nomeArquivo = arquivo.getFileName();
			String extensao = nomeArquivo.substring(nomeArquivo.lastIndexOf("."), nomeArquivo.length());
			
			File file = new File("arq" + extensao);

			file.createNewFile();
			FileUtils.copyInputStreamToFile(arquivo.getFile(), file);
			
			CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
			if(idOrgaoUsu != null && !"".equals(idOrgaoUsu)) {
				orgaoUsuario.setIdOrgaoUsu(idOrgaoUsu);
			} else {
				orgaoUsuario = getTitular().getOrgaoUsuario();
			}
			
			CpBL cpbl = new CpBL();
			inputStream = cpbl.uploadFuncao(file, orgaoUsuario, extensao);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
			
		if(inputStream == null) {
			result.include(SigaModal.ALERTA, SigaModal.mensagem("Arquivo processado com sucesso!").titulo("Sucesso"));
			carregarExcel();
		} else {			
			return new InputStreamDownload(inputStream, "application/text", "inconsistencias.txt");	
		}
		return null;
	}
}
