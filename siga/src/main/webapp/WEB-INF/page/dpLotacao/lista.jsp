<%@ page language="java" contentType="text/html; charset=UTF-8"
         buffer="64kb" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<siga:pagina titulo="Listar Lota&ccedil;&atilde;o">
    <link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection"/>
    <link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css"
          media="screen, projection"/>
    <script type="text/javascript" language="Javascript1.1">
        function sbmt(offset) {
            if (offset == null) {
                offset = 0;
            }
            frm.elements["paramoffset"].value = offset;
            frm.elements["p.offset"].value = offset;
            frm.submit();
        }
    </script>
    <!-- main content -->
    <div class="container-fluid">
        <form name="frm" action="listar" id="listar" class="form100" method="GET">
            <input type="hidden" name="paramoffset" value="0"/>
            <input type="hidden" name="p.offset" value="0"/>
            <div class="card bg-light mb-3">
                <div class="card-header">
                    <h5>Cadastro de <fmt:message key="usuario.lotacao"/></h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-sm">
                            <div class="alert alert-info  mensagem-pesquisa" role="alert" style="display: none;">
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                    <span aria-hidden="true">×</span>
                                </button>
                                <i class="fas fa-info-circle"></i> ${mensagemPesquisa}
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>Órgão</label>
                                <select name="idOrgaoUsu" value="${idOrgaoUsu}" class="form-control  siga-select2">
                                    <c:forEach items="${orgaosUsu}" var="item">
                                        <option value="${item.idOrgaoUsu}"
                                            ${item.idOrgaoUsu == idOrgaoUsu ? 'selected' : ''}>
                                                ${item.nmOrgaoUsu}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>Nome</label>
                                <input type="text" id="nome" name="nome" value="${nome}" maxlength="100"
                                       class="form-control"/>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="form-group">
                                <input value="Pesquisar" class="btn btn-primary" onclick="javascript: sbmt(0);"/>
                                <c:if test="${temPermissaoParaExportarDados}">
                                    <button type="button" class="btn btn-outline-success" id="exportarCsv"
                                            title="Exportar para CSV"
                                            onclick="javascript:csv('listar', '/siga/app/lotacao/exportarCsv');"><i
                                            class="fa fa-file-csv"></i> Exportar
                                    </button>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <c:if test="${not empty mensagem}">
                <div style="align-items: center">
                    <div>
                        <p id="mensagem" style="text-align: center" class="alert alert-success">${mensagem}</p>
                        <script>
                            setTimeout(function () {
                                $('#mensagem').fadeTo(1000, 0, function () {
                                    $('#mensagem').slideUp(1000);
                                });
                            }, 5000);
                        </script>
                    </div>
                </div>
            </c:if>
            <h3 class="gt-table-head"><fmt:message key="usuario.lotacoes"/> cadastradas</h3>
            <table border="0" class="table table-sm table-striped">
                <thead class="${thead_color}">
                <tr>
                    <th align="left">Nome</th>
                    <th align="left">Sigla</th>
                    <th hidden="true" align="left">Externa</th>
                    <th align="left">Unidade Receptora</th>
                    <th align="left">Suspensa</th>
                    <th colspan="2" align="center">Op&ccedil;&otilde;es</th>
                </tr>
                </thead>

                <tbody>
                <siga:paginador maxItens="15" maxIndices="10" totalItens="${tamanho}"
                                itens="${itens}" var="lotacao">
                    <tr>
                        <td align="left">${lotacao.descricao}</td>
                        <td align="left">${lotacao.sigla}</td>
                        <td align="left" style="${lotacao.unidadeReceptora == true ? 'color : red' : ''}">${lotacao.unidadeReceptora == true ? 'SIM' : 'NÃO'}</td>
                        <td hidden="true" align="left">${lotacao.isExternaLotacao == 1 ? 'SIM' : 'NÃO'}</td>
                        <td align="left" style="${lotacao.isSuspensa == 1 ? 'color : red' : ''}">${lotacao.isSuspensa == 1 ? 'SIM' : 'NÃO'}</td>
                        <td align="left">
                            <c:url var="url" value="/app/lotacao/editar">
                                <c:param name="id" value="${lotacao.id}"></c:param>
                            </c:url>
                            <c:url var="urlAtivarInativar" value="/app/lotacao/ativarInativar">
                                <c:param name="id" value="${lotacao.id}"></c:param>
                            </c:url>
                            <c:url var="urlSuspender" value="/app/lotacao/suspender">
                                <c:param name="id" value="${lotacao.id}"></c:param>
                            </c:url>

                            <div class="btn-group">
                                <c:choose>
                                    <c:when test="${empty lotacao.dataFimLotacao}">
                                        <a href="${urlAtivarInativar}"
                                           onclick='javascript:atualizarUrl("javascript:submitPost(\"${urlAtivarInativar}\")","Deseja inativar o cadastro selecionado?");return false;'
                                           class="btn btn-primary" role="button"
                                           aria-pressed="true" data-siga-modal-abrir="confirmacaoModal"
                                           style="min-width: 80px;">Inativar</a>
                                        <button type="button"
                                                class="btn btn-primary dropdown-toggle dropdown-toggle-split"
                                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            <span class="sr-only"></span>
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="javascript:submitPost('${urlAtivarInativar}')" class="btn btn-danger"
                                           role="button" aria-pressed="true" style="min-width: 80px;">Ativar</a>
                                        <button type="button"
                                                class="btn btn-danger dropdown-toggle dropdown-toggle-split"
                                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            <span class="sr-only"></span>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                                <div class="dropdown-menu">
                                    <a href="${url}" class="dropdown-item" role="button" aria-pressed="true">Alterar</a>
                                </div>
                            </div>
                        </td>
                    </tr>
                </siga:paginador>
                </tbody>
            </table>
            <div class="gt-table-buttons">
                <c:url var="url" value="/app/lotacao/editar"></c:url>
                <c:url var="urlAtivarInativar" value="/app/lotacao/ativarInativar"></c:url>
                <input type="button" value="Incluir" onclick="javascript:window.location.href='${url}'"
                       class="btn btn-primary">
            </div>
        </form>
    </div>

    <script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
    <script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
    <script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            if ('${mensagemPesquisa}'.length > 0) $('.mensagem-pesquisa').css({'display': 'block'});
        });

        function csv(id, action) {
            var frm = document.getElementById(id);
            frm.method = "POST";
            sbmtAction(id, action);

            $('.mensagem-pesquisa').alert('close');

            frm.action = 'listar';
            frm.method = "GET";
        }

        function sbmtAction(id, action) {
            var frm = document.getElementById(id);
            frm.action = action;
            frm.submit();
            return;
        }

        function submitPost(url) {
            var frm = document.getElementById('listar');
            frm.method = "POST";
            sbmtAction('listar', url);
        }

        function atualizarUrl(url, msg) {
            $('.btn-confirmacao').attr("href", url);
            document.getElementById("msg").innerHTML = msg;
        }
    </script>
</siga:pagina>
