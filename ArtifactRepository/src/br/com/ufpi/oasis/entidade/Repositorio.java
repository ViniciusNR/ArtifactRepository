package br.com.ufpi.oasis.entidade;

import org.eclipse.jgit.api.Git;

public class Repositorio {
	private String name;
	private String remoteUri;
	private String localUri;
	private Git gitObject;
	
	public Repositorio(){}
	
	public Repositorio(String name, String remoteUri, String localUri, Git gitObject){
		this.name = name;
		this.remoteUri = remoteUri;
		this.localUri = localUri;
		this.gitObject = gitObject;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemoteUri() {
		return remoteUri;
	}
	public void setRemoteUri(String remoteUri) {
		this.remoteUri = remoteUri;
	}
	public String getLocalUri() {
		return localUri;
	}
	public void setLocalUri(String localUri) {
		this.localUri = localUri;
	}
	public Git getGitObject() {
		return gitObject;
	}
	public void setGitObject(Git gitObject) {
		this.gitObject = gitObject;
	}
}