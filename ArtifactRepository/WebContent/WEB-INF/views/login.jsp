<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>SaR</title>

<link href="resources/css/bootstrap.min.css" rel="stylesheet">

</head>
<body>
	<div class="container-fluid" style="margin-top: 1%; height: 90%;">
		<div class="container col-md-12">

			<div id="info" style="margin-top: 50px;" class="col-md-6">
				<div class="panel panel-warning">
					<div class="panel-heading">
						<h3 class="panel-title">Importante!</h3>
					</div>
					
					<div class="panel-body">
					<p>
						<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
						Forneça corretamente seu nome de usuário no GitHub para começar a usar o sistema.
					</p>
					<p>
						<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
						Certifique-se que o usuário fornecido seja o mesmo logado no GitHub neste navegador.  
					</p>
					<p>
						<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
						Se não houver usuário logado, você será redirecionado para a tela de login do GitHub.  
					</p>
					</div>
				</div>
			</div>

			<div id="loginbox" style="margin-top: 50px;" class="col-md-6">
				<div class="panel panel-primary">
					<div class="panel-heading">
						<div class="panel-title">Acesso ao Sistema</div>
					</div>

					<div style="padding-top: 30px" class="panel-body">
						<div style="display: none" id="login-alert"
							class="alert alert-danger col-sm-12"></div>

						<form:form id="loginform" modelAttribute="login"
							action="loginProcess" method="post" class="form-horizontal"
							role="form">

							<div style="margin-bottom: 25px" class="input-group">
								<span class="input-group-addon"><i
									class="glyphicon glyphicon-user"></i></span>
								<form:input id="username" type="text" class="form-control"
									name="username" path="username" value=""
									placeholder="Nome de usuário no GitHub" />
							</div>

<!-- 							<div style="margin-bottom: 25px" class="input-group"> -->
<!-- 								<span class="input-group-addon"><i -->
<!-- 									class="glyphicon glyphicon-lock"></i></span> -->
<%-- 								<form:password id="password" class="form-control" --%>
<%-- 									name="password" path="password" placeholder="senha" /> --%>
<!-- 							</div> -->

							<!-- 							<div class="input-group"> -->
							<!-- 								<div class="checkbox"> -->
							<!-- 									<label> <input id="login-remember" type="checkbox" -->
							<!-- 										name="remember" value="1"> Remember me -->
							<!-- 									</label> -->
							<!-- 								</div> -->
							<!-- 							</div> -->

							<div style="margin-top: 10px" class="form-group">
								<!-- Button -->
								<div class="col-md-12 controls" style="text-align: center;">
									<form:button id="login" class="btn btn-primary" style="width:100%;">Acessar</form:button>
								</div>
							</div>
						</form:form>
					</div>
				</div>
			</div>

		</div>
	</div>
</body>
</html>