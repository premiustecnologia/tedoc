<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<script type="text/javascript">
	var uriLogoSiga = '${uri_logo_siga_pequeno}';
</script>
<script type="text/javascript" src="/sigaex/public/javascript/assinatura-digital.js?v=20220915160000"></script>
<c:if test="${not empty f:resource('assinador.externo.popup.url')}">
	<script type="text/javascript" src="${f:resource('assinador.externo.popup.url')}/popup-api.js"></script>
</c:if>
