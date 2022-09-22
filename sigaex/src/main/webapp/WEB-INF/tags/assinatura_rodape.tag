<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ attribute name="podeAssinarPorComSenha" required="false"%>

<input type="hidden" id="siglaUsuarioCadastrante" value="${cadastrante.sigla}"/>
<input type="hidden" id="siglaUsuSubscritor" value="${doc.subscritor.sigla}"/>
<input type="hidden" id="siglaUsuTitular" value="${titular.sigla}"/>
<input type="hidden" id="nomeUsuSubscritor" value="${doc.subscritor.nomePessoa}"/>
<input type="hidden" id="podeAssinarPorComSenha" value="${podeAssinarPorComSenha}"/>
<input type="hidden" id="cpfUsuarioCadastrante" value="${cadastrante.cpfFormatado}"/>

<c:forEach items="${doc.getMobilGeral().getMovimentacoesPorTipo(24, true)}" var="currentItem" varStatus="stat">
  <c:set var="cossignatarios" value="${stat.first ? '' : cossignatarios} ${currentItem.subscritor.sigla}" />
</c:forEach>

<input type="hidden" id="siglaUsuCossignatarios" value="${cossignatarios}"/>
