package codigoalvo.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import codigoalvo.entity.ContaTipo;
import codigoalvo.entity.UsuarioTipo;

public class TipoUtil {

	private static final String TIPOS_USUARIO_JSON = buildTiposUsuarioJson();
	private static final String TIPOS_CONTA_JSON = buildTiposContaJson();

	public static String getTiposUsuarioJson() {
		return TIPOS_USUARIO_JSON;
	}

	public static String getTiposContaJson() {
		return TIPOS_CONTA_JSON;
	}

	private static String buildTiposUsuarioJson() {
		List<String> tipos = new ArrayList<String>();
		for (UsuarioTipo usuarioTipo : UsuarioTipo.values()) {
			tipos.add(usuarioTipo.name());
		}
		return JsonUtil.toJson(tipos.toArray(new String[0]));
	}

	private static String buildTiposContaJson() {
		List<Tipo> tipos = new ArrayList<Tipo>();
		for (ContaTipo contaTipo : ContaTipo.values()) {
			tipos.add(new TipoUtil.Tipo(""+contaTipo.getId(), contaTipo.getDescricao()));
		}
		return JsonUtil.toJson(tipos);
	}

	static class Tipo implements Serializable {
		private static final long serialVersionUID = -2164022635596102758L;
		private String key;
		private String value;
		public Tipo() {}
		public Tipo(String key, String value) {
			super();
			this.key = key;
			this.value = value;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}

}

