<%@ include file="/init.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<portlet:actionURL name="sorteio" var="sorteios"/>

<div class="container">
    <aui:form action="<%= sorteios %>" name="sorteio" method="post" cssClass="form-sorteio">
        <div class="row align-items-start">
            <div class="col">
                <h2>Sorteio de números</h2>
                <aui:input cssClass="input-inicial" name="inicial" type="number"
                           label="Número inicial <b>(apenas números)</b>"
                           placeholder="1"/>
                <aui:input cssClass="input-final" name="final" type="number"
                           label="Número final <b>(apenas números)</b>"
                           placeholder="100"/>

                <h2>Sorteio de nomes</h2>
                <aui:input cssClass="input-nomes" name="nomes"
                           label="Digite os nomes <b>(separados apenas por vírgulas)</b>"
                           required="false" type="textarea" placeholder="nome1, nome2, nome3"></aui:input>

                <h2>Sorteio por arquivo</h2>
                <aui:input cssClass="input-arquivos" name="arquivos" type="file" required="false"
                           label="Upload de Arquivos <b>(separados apenas por vírgulas)</b>"></aui:input>
                <aui:input name="quantidade" type="number" required="true"
                           label="Quantidade sorteada por rodada" placeholder="5"/>
                <aui:button-row>
                    <aui:button cssClass="button-submit" type="submit" name="sortear"
                                value="Sortear"></aui:button>
                    <aui:button cssClass="button-reset" type="reset" name="resetar"
                                value="Resetar"></aui:button>
                </aui:button-row>
            </div>

            <div class="col">
                <c:if test="${not empty randomName or not empty randomString or not empty listaNumeros}">
                    <h2>Resultados: </h2>
                    <table class="table table-bordered">
                        <c:forEach var="arquivo" items="${randomName}">
                            <tbody>
                            <tr>
                                <td>${arquivo}</td>
                            </tr>
                            </tbody>
                        </c:forEach>
                        <c:forEach var="nome" items="${randomString}">
                            <tbody>
                            <tr>
                                <td>${nome}</td>
                            </tr>
                            </tbody>
                        </c:forEach>
                        <c:forEach var="numero" items="${listaNumeros}">
                            <tbody>
                            <tr>
                                <td>${numero}</td>
                            </tr>
                            </tbody>
                        </c:forEach>
                    </table>
                </c:if>
            </div>
        </div>
    </aui:form>
</div>

<script>

    // Input Valor inicial
    $('.input-inicial').blur(function () {
        if ($(this).val().length != 0) {
            $('.input-nomes').attr('disabled', true);
            $('.input-arquivos').attr('disabled', true);
        } else {
            $('.input-nomes').attr('disabled', false);
            $('.input-arquivos').attr('disabled', false);
        }
    });

    //  Input Valor maximo
    $('.input-final').blur(function () {
        if ($(this).val().length != 0) {
            $('.input-nomes').attr('disabled', true);
            $('.input-arquivos').attr('disabled', true);
        } else {
            $('.input-nomes').attr('disabled', false);
            $('.input-arquivos').attr('disabled', false);
        }
    });

    //     Input Nomes
    $('.input-nomes').blur(function () {
        if ($(this).val().length != 0) {
            $('.input-final').attr('disabled', true);
            $('.input-inicial').attr('disabled', true);
            $('.input-arquivos').attr('disabled', true);
        } else {
            $('.input-final').attr('disabled', false);
            $('.input-inicial').attr('disabled', false);
            $('.input-arquivos').attr('disabled', false);
        }
    });

    // Input arquivos
    $('.input-arquivos').blur(function () {
        if ($(this).val().length != 0) {
            $('.input-final').attr('disabled', true);
            $('.input-inicial').attr('disabled', true);
            $('.input-nomes').attr('disabled', true);
        } else {
            $('.input-final').attr('disabled', false);
            $('.input-inicial').attr('disabled', false);
            $('.input-nomes').attr('disabled', false);
        }
    });

</script>