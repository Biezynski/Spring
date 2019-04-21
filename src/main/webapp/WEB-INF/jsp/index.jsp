<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
	<head>
		<title>BANK</title>
		<link href="resources/css/style.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<div class="sb-index-container">
			<div class="sb-logo">
				<img src="resources/images/bank.png" alt="Sample Bank" />
			</div>

			<div class="sb-login-area">
				<form id="sb-form-login" action="login" method="post">
					<table class="sb-center-table">
						<tr>
							<td>
								<label class="sb-white-label">Login: </label>
							</td>
							<td>
								<form:input type="text" name="username" path="login.username" maxlength="15" />
							</td>
						</tr>
						<tr>
							<td>
								<label class="sb-white-label">Haslo: </label>
							</td>
							<td>
								<form:input type="password" name="password" path="login.password" maxlength="15" />
							</td>
						</tr>
						<tr>
							<td colspan="2" align="right">
								<input class="sb-button" type="submit" value="Zaloguj" />
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div class="sb-footer">
				Prawa autorskie &reg; Biezynski wszelkie prawa zastrzezone!
			</div>
		</div>
	</body>
</html>