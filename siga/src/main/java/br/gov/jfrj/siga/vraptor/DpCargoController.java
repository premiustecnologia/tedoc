package br.gov.jfrj.siga.vraptor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpCargoDaoFiltro;
import br.gov.jfrj.siga.model.Selecionavel;

@Controller
public class DpCargoController extends
		SigaSelecionavelControllerSupport<DpCargo, DpCargoDaoFiltro> {
	
	private Long orgaoUsu;


	/**
	 * @deprecated CDI eyes only
	 */
	public DpCargoController() {
		super();
	}

	@Inject
	public DpCargoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}
	
	protected boolean temPermissaoParaExportarDados() {
		return Boolean.valueOf(Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(), getTitular().getLotacao(),"SIGA;GI;CAD_CARGO;EXP_DADOS"));
	}
	
	@Get
	@Post
	@Path({"/app/cargo/buscar","/cargo/buscar.action"})
	public void busca(String nome, Long idOrgaoUsu, Integer paramoffset, String postback) throws Exception{
		if (postback == null)
			orgaoUsu = getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu();
		else
			orgaoUsu = idOrgaoUsu;
		
		this.getP().setOffset(paramoffset);
		
		if (nome == null)
			nome = "";
		
		super.aBuscar(nome, postback);
		
		result.include("param", getRequest().getParameterMap());
		result.include("request",getRequest());
		result.include("itens",getItens());
		result.include("tamanho",getTamanho());
		result.include("orgaosUsu", getOrgaosUsu());
		result.include("idOrgaoUsu",orgaoUsu);
		result.include("nome",nome);
		result.include("postbak",postback);
		result.include("offset",paramoffset);
	}

	@Override
	protected DpCargoDaoFiltro createDaoFiltro() {
		final DpCargoDaoFiltro flt = new DpCargoDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(getNome()));
		flt.setIdOrgaoUsu(orgaoUsu);
		try{
			flt.setIdCargoIni(Long.valueOf(getNome()));
		}catch(Exception e){
			flt.setIdCargoIni(null);
		}
		if (flt.getIdOrgaoUsu() == null)
			flt.setIdOrgaoUsu(getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu());
		return flt;
	}
	
	@Override
	protected Selecionavel selecionarPorNome(final DpCargoDaoFiltro flt)
			throws AplicacaoException {
		// Procura por nome
		flt.setNome(Texto.removeAcentoMaiusculas(flt.getSigla()));
		flt.setSigla(null);
		final List l = dao().consultarPorFiltro(flt);
		if (l != null)
			if (l.size() == 1)
				return (DpCargo) l.get(0);
		return null;
	}

	@Get
	@Post
	@Path({"/app/cargo/selecionar","/cargo/selecionar.action"})
	public void selecionar(String sigla) {
		this.setNome(sigla);
		String resultado =  super.aSelecionar(sigla);
		
		if (resultado == "ajax_retorno"){
			result.include("sel", getSel());
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_retorno.jsp");
		}else{
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_vazio.jsp");
		}
	}
	
	@Get("app/cargo/listar")
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
			DpCargoDaoFiltro dpCargo = new DpCargoDaoFiltro();
			if(paramoffset == null) {
				paramoffset = 0;
			}
			dpCargo.setIdOrgaoUsu(idOrgaoUsu);
			dpCargo.setNome(Texto.removeAcento(nome));
			setItens(CpDao.getInstance().consultarPorFiltro(dpCargo, paramoffset, 15));
			result.include("itens", getItens());
			result.include("tamanho", dao().consultarQuantidade(dpCargo));
			
			result.include("idOrgaoUsu", idOrgaoUsu);
			result.include("nome", nome);
			
			if (getItens().size() == 0) result.include("mensagemPesquisa", "Nenhum resultado encontrado.");
		}
		setItemPagina(15);
		result.include("currentPageNumber", calculaPaginaAtual(paramoffset));		
		result.include("temPermissaoParaExportarDados", temPermissaoParaExportarDados());
	}	
		
	@Post
	@Path("app/cargo/exportarCsv")
	public Download exportarCsv(Long idOrgaoUsu, String nome) throws Exception {				
			
 		if(idOrgaoUsu != null) {
			DpCargoDaoFiltro dpCargo = new DpCargoDaoFiltro(nome, idOrgaoUsu);																
															
			List <DpCargo> lista = CpDao.getInstance().consultarPorFiltro(dpCargo, 0, 0);
			
			if (lista.size() > 0) {				
				InputStream inputStream = null;
				StringBuffer texto = new StringBuffer();
				texto.append("Cargo" + System.lineSeparator());
				
				for (DpCargo cargo : lista) {
					texto.append(cargo.getNomeCargo() + ";");										
					texto.append(System.lineSeparator());
				}
				
				inputStream = new ByteArrayInputStream(texto.toString().getBytes("ISO-8859-1"));									
				
				return new InputStreamDownload(inputStream, "text/csv", "cargos.csv");
				
			} else {
				
				if(CpConfiguracaoBL.SIGLAS_ORGAOS_ADMINISTRADORES.contains(getTitular().getOrgaoUsuario().getSigla())) {
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
					result.use(Results.page()).forwardTo("/WEB-INF/page/dpCargo/lista.jsp");
			}							
		}					

		return null;
	}
	
	@Get("/app/cargo/editar")
	public void edita(final Long id){
		if (id != null) {
			DpCargo cargo = dao().consultar(id, DpCargo.class, false);
			result.include("nmCargo",cargo.getNomeCargo());
			result.include("idOrgaoUsu", cargo.getOrgaoUsuario().getId());
			result.include("nmOrgaousu", cargo.getOrgaoUsuario().getNmOrgaoUsu());
			
			List<DpPessoa> list = CpDao.getInstance().consultarPessoasComCargo(id);
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
	}

	@Transacional
	@Post("/app/cargo/gravar")
	public void editarGravar(final Long id, 
							 final String nmCargo, 
							 final Long idOrgaoUsu) throws Exception{
		assertAcesso("GI:Módulo de Gestão de Identidade;CAD_CARGO: Cadastrar Cargo");
		
		if(nmCargo == null)
			throw new AplicacaoException("Nome do cargo não informado");
		
		if(idOrgaoUsu == null)
			throw new AplicacaoException("Órgão não informada");
		
		if(nmCargo != null && !nmCargo.matches("[a-zA-ZàáâãéêíóôõúçÀÁÂÃÉÊÍÓÔÕÚÇ 0-9-/.]+")) 			                              
			throw new AplicacaoException("Nome com caracteres não permitidos");
				
		List<DpPessoa> listPessoa = null;
		
		DpCargo cargo = new DpCargo();
		cargo.setNomeCargo(Texto.removeAcento(Texto.removerEspacosExtra(nmCargo).trim()));
		CpOrgaoUsuario ou = new CpOrgaoUsuario();
		ou.setIdOrgaoUsu(idOrgaoUsu);
		cargo.setOrgaoUsuario(ou);
		
		cargo = CpDao.getInstance().consultarPorNomeOrgao(cargo);
		
		if(cargo != null && !cargo.getId().equals(id)) {
			throw new AplicacaoException("Nome do cargo já cadastrado!");
		}
		
		cargo = new DpCargo();
		
		if (id == null) {
			cargo = new DpCargo();
			Date data = new Date(System.currentTimeMillis());
			cargo.setDataInicio(data);
		} else {
			cargo = dao().consultar(id, DpCargo.class, false);
			listPessoa = CpDao.getInstance().consultarPessoasComCargo(id);
			
		}
		cargo.setDescricao(Texto.removerEspacosExtra(nmCargo).trim());
		
		if (idOrgaoUsu != null && idOrgaoUsu != 0 && (listPessoa == null || listPessoa.size() == 0)) {
			CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
			orgaoUsuario = dao().consultar(idOrgaoUsu, CpOrgaoUsuario.class, false);	
			cargo.setOrgaoUsuario(orgaoUsuario);
		}
		
		try {
			dao().iniciarTransacao();
			dao().gravar(cargo);
			if(cargo.getIdCargoIni() == null && cargo.getId() != null) {
				cargo.setIdCargoIni(cargo.getId());
				cargo.setIdeCargo(cargo.getId().toString());
				dao().gravar(cargo);
			}
			dao().commitTransacao();
			this.result.include("mensagem", "Operação realizada com sucesso!");
			this.result.redirectTo(this).lista(0, null, "");
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}
	}

	@Get("/app/cargo/carregarExcel")
	public void carregarExcel() {
		if(CpConfiguracaoBL.SIGLAS_ORGAOS_ADMINISTRADORES.contains(getTitular().getOrgaoUsuario().getSigla())) {
			result.include("orgaosUsu", dao().listarOrgaosUsuarios());
		} else {
			result.include("nmOrgaousu", getTitular().getOrgaoUsuario().getNmOrgaoUsu());	
		}
		
		result.use(Results.page()).forwardTo("/WEB-INF/page/dpCargo/cargaCargo.jsp");
	}
	
	@Transacional
	@Post("/app/cargo/carga")
	public Download carga( final UploadedFile arquivo, Long idOrgaoUsu) throws Exception {
		InputStream inputStream = null;
		try {
			String nomeArquivo = arquivo.getFileName();
			String extensao = nomeArquivo.substring(nomeArquivo.lastIndexOf("."), nomeArquivo.length());
			
			java.nio.file.Path tempFilePath = Files.createTempFile(UUID.randomUUID().toString(), extensao);
			Files.copy(arquivo.getFile(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);
			
			CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
			if(idOrgaoUsu != null && !"".equals(idOrgaoUsu)) {
				orgaoUsuario.setIdOrgaoUsu(idOrgaoUsu);
			} else {
				orgaoUsuario = getTitular().getOrgaoUsuario();
			}
			
			CpBL cpbl = new CpBL();
			inputStream = cpbl.uploadCargo(tempFilePath.toFile(), orgaoUsuario, extensao);
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