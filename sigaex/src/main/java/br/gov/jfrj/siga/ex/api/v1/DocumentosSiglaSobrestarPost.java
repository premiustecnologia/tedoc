package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaSobrestarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaSobrestarPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaSobrestarPost;
import br.gov.jfrj.siga.ex.bl.Ex;

public class DocumentosSiglaSobrestarPost implements IDocumentosSiglaSobrestarPost {

	@Override
	public void run(DocumentosSiglaSobrestarPostRequest req, DocumentosSiglaSobrestarPostResponse resp)
			throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ctx.assertAcesso("");

				DpPessoa cadastrante = ctx.getCadastrante();
				DpPessoa titular = cadastrante;
				DpLotacao lotaCadastrante = cadastrante.getLotacao();
				DpLotacao lotaTitular = ctx.getLotaTitular();

				ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp,
						"Documento a Sobrestar");

				if (!Ex.getInstance().getComp().podeSobrestar(titular, lotaTitular, mob)) {
					throw new PresentableUnloggedException(
							"O documento " + mob.getSigla() + " não pode ser sobrestado por "
									+ titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta());
				}

				ctx.assertAcesso(mob, titular, lotaTitular);

				Ex.getInstance().getBL().sobrestar(cadastrante, lotaCadastrante, mob, null, null, titular);

				resp.sigla = mob.doc().getCodigo();
				resp.status = "OK";
			} catch (Exception e) {
				ctx.rollback(e);
				throw e;
			}
		}
	}

	@Override
	public String getContext() {
		return "Arquivar no Corrente";
	}

}