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
		<div class="row">
			<div class="col-md-12">
				<nav class="navbar navbar-default" role="navigation">
					<div class="navbar-header">
						<a class="navbar-brand" href="/ArtifactRepository/index">Software
							Artifact Repository</a>
					</div>
				</nav>
				<div class="col-md-6">
					<div class="panel panel-default">
						<!-- Default panel contents -->
						<div class="panel-heading">Meus Repositórios</div>

						<!-- List group -->
						<ul class="list-group">
							<c:forEach items="${repositorios}" var="repositorio">
								<a
									href="/ArtifactRepository/selecionaRepositorio?repositorio=${repositorio}"
									class="list-group-item">${repositorio}</a>
							</c:forEach>
						</ul>
					</div>
				</div>
				<div class="col-md-6">
					<div class="jumbotron well">
						<form action="/ArtifactRepository/cloneHttp">
							<h4>Clonar Repositório GIT</h4>
							<div class="input-group">
								<span class="input-group-addon" id="basic-addon3">https://github.com/user/repository.git</span>
								<input type="text" name="uriClone" class="form-control"
									id="basic-url" aria-describedby="basic-addon3">
							</div>
							<button type="submit" class="btn btn-default">Clonar</button>
						</form>
						<form action="/ArtifactRepository/autenticar">
							<button type="submit" class="btn btn-default">Autenticar</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>