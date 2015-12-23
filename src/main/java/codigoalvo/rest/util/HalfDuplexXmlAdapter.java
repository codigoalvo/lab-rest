package codigoalvo.rest.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class HalfDuplexXmlAdapter extends XmlAdapter<String, String> {

	@Override
	public String unmarshal(String v) throws Exception {
		return v;
	}

	@Override
	public String marshal(String value) throws Exception {
		//ignore marshall so you have half duplex
		return null;
	}

}
