package codigoalvo.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import codigoalvo.entity.PagamentoTipo;
import codigoalvo.entity.UsuarioTipo;

public class TipoUtil {

	private static final String TIPOS_USUARIO_JSON = buildTiposUsuarioJson();
	private static final String TIPOS_PAGAMENTO_JSON = buildTiposPagamentoJson();

	public static String getTiposUsuarioJson() {
		return TIPOS_USUARIO_JSON;
	}

	public static String getTiposPagamentoJson() {
		return TIPOS_PAGAMENTO_JSON;
	}

	private static String buildTiposUsuarioJson() {
		List<String> tipos = new ArrayList<String>();
		for (UsuarioTipo usuarioTipo : UsuarioTipo.values()) {
			tipos.add(usuarioTipo.name());
		}
		return JsonUtil.toJson(tipos.toArray(new String[0]));
	}

	private static String buildTiposPagamentoJson() {
		List<Tipo> tipos = new ArrayList<Tipo>();
		for (PagamentoTipo pagamentoTipo : PagamentoTipo.values()) {
			tipos.add(new TipoUtil.Tipo(""+pagamentoTipo.getId(), pagamentoTipo.getDescricao()));
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

