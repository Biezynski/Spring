<%@ include file="leftMenu.jsp"%>

<%@ include file="header.jsp"%>

<div id="accountInfoPage">
	<table style="width: 99%;">
		<tr>
			<td width="20%">
				<span class="sb-content-desc"> Informacje o koncie </span>
			</td>
		</tr>
	</table>
	<form:form action="accountInfo" method="POST" modelAttribute="account">
		<table style="width: 600px;">
			<tr>
				<td width="20%"><label>Konto: </label></td>
				<td align="left">
					<form:select id="ownerIDSelect" path="ownerID" htmlEscape="false">
						<form:option value="-1">-- Wybierz konto --</form:option>
						<c:if test="${not empty accounts}">
							<form:options items="${accounts}" itemLabel="ownerID" itemValue="ownerID" />
						</c:if>
					</form:select>
				</td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td class="sb-table-button-row" colspan="3" align="right"><input class="sb-button" type="submit" value="Pokaz informacje" /></td>
			</tr>
			<c:if test="${not empty responseDTO.data}">
				<tr>
					<td colspan="2">
						<table class="sb-account-info-table">
							<tr class="even-row-bg">
								<td width="30%">Numer konta:</td>
								<td>${responseDTO.data.ownerID}</td>
							<tr>
							<tr class="odd-row-bg">
								<td>Saldo:</td>
								<td>${responseDTO.data.balance}</td>
							<tr>
							<tr class="even-row-bg">
								<td>Ma oczekujaca porzyczke:</td>
								<td>${responseDTO.data.hasPendingLoan.equals("true") ? "Tak" : "Nie"}</td>
							<tr>
						</table>
					</td>
				</tr>
			</c:if>
			<tr>
				<td colspan="3" align="center">
					<div>
						<div>
							<p>
								<span id="sb-return-message" class="${responseDTO.messageStyle}">
									${responseDTO.message} 
								</span>
							</p>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</form:form>
</div>

<%@ include file="footer.jsp"%>