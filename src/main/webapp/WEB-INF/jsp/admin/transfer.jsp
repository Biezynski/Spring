<%@ include file="leftMenu.jsp"%>

<%@ include file="header.jsp"%>

<div id="transferPage">
	<table style="width: 99%;">
		<tr>
			<td width="20%">
				<span class="sb-content-desc"> Przelew pomiedz kontami</span>
			</td>
		</tr>
	</table>
	<form action="transfer" method="POST">
		<table style="width: 600px;">
			<tr>
				<td width="25%"><label>Konto zleceniodawcy: </label></td>
				<td align="left">
					<form:select path="operation.sourceAccount" htmlEscape="false">
						<form:option value="-1">-- Wybierz ilosc --</form:option>
						<c:if test="${not empty accounts}">
							<form:options items="${accounts}" itemLabel="ownerID" itemValue="ownerID" />
						</c:if>
					</form:select>
				</td>
			</tr>
			<tr>
				<td width="20%"><label>Konto odbiorcy: </label></td>
				<td align="left">
					<form:select path="operation.targetAccount" htmlEscape="false">
						<form:option value="-1">-- Wybierz ilosc --</form:option>
						<c:if test="${not empty accounts}">
							<form:options items="${accounts}" itemLabel="ownerID" itemValue="ownerID" />
						</c:if>
					</form:select>
				</td>
			</tr>
			<tr>
				<td><label>Ilosc: </label></td>
				<td align="left">
					<form:input type="text" class="well-formated-input money" id="ammount" path="operation.amount" size="20" maxlength="10"/>
				</td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td class="sb-table-button-row" colspan="3" align="right"><input class="sb-button" type="submit" value="Transfer" /></td>
			</tr>
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
	</form>
</div>

<%@ include file="footer.jsp"%>