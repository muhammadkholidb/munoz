package hu.pe.munoz.commonwebfaces;

import java.util.Arrays;
import java.util.List;

import org.omnifaces.util.Faces;

public abstract class DefaultPagePermissionByUrl {

	private static final List<String> TRAILING_URLS_MODIFY = Arrays.asList("edit.xhtml", "add.xhtml");
	private static final List<String> TRAILING_URLS_VIEW = Arrays.asList("view.xhtml", "list.xhtml");
	
	private boolean allowed;
	
	public DefaultPagePermissionByUrl() {
		String requestUri = Faces.getRequest().getRequestURI();
		for (String trailing : TRAILING_URLS_MODIFY) {
			if (requestUri.toLowerCase().endsWith(trailing)) {
				
			}
		}
	}
	
	public boolean isAllowed() {
		return allowed;
	}
	
	abstract protected String getMenuCode();
	
}
