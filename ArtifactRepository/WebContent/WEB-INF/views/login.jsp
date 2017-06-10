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
		<div class="container">
			<div id="loginbox" style="margin-top: 50px;"
				class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
				<div class="panel panel-info">
					<div class="panel-heading">
						<div class="panel-title">Entrar</div>
					</div>

					<div style="padding-top: 30px" class="panel-body">

						<div style="display: none" id="login-alert"
							class="alert alert-danger col-sm-12"></div>

						<form id="loginform" class="form-horizontal" role="form">

							<div style="margin-bottom: 25px" class="input-group">
								<span class="input-group-addon"><i
									class="glyphicon glyphicon-user"></i></span> <input
									id="login-username" type="text" class="form-control"
									name="username" value="" placeholder="nome de usuário ou email">
							</div>

							<div style="margin-bottom: 25px" class="input-group">
								<span class="input-group-addon"><i
									class="glyphicon glyphicon-lock"></i></span> <input
									id="login-password" type="password" class="form-control"
									name="password" placeholder="senha">
							</div>

<!-- 							<div class="input-group"> -->
<!-- 								<div class="checkbox"> -->
<!-- 									<label> <input id="login-remember" type="checkbox" -->
<!-- 										name="remember" value="1"> Remember me -->
<!-- 									</label> -->
<!-- 								</div> -->
<!-- 							</div> -->

							<div style="margin-top: 10px" class="form-group">
								<!-- Button -->
								<div class="col-sm-12 controls">
									<a id="btn-login" href="/ArtifactRepository/index?username=${username}" class="btn btn-success">Login</a>
								</div>
							</div>

						</form>
					</div>
				</div>
			</div>
			
		</div>
	</div>
</body>
</html>