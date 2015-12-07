package codigoalvo.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesUtil {

	private static final Logger LOG = Logger.getLogger(PropertiesUtil.class);
	private Properties properties = new Properties();
	private String fileName = "";

	public PropertiesUtil(String fileName) {
		this.fileName = fileName;
		boolean openOk = open();
		if (!openOk) {
			throw new RuntimeException("Não foi possível abrir arquivo de properties! ("+fileName+")");
		}
	}

	public boolean open() {
		boolean openOk = false;
		InputStream fileInStream = null;
		try {
			//ClassLoader loader = Thread.currentThread().getContextClassLoader();
			//fileInStream = loader.getResourceAsStream(fileName);
			fileInStream = getClass().getClassLoader().getResourceAsStream(fileName);
			//fileInStream = new FileInputStream(this.fileName);
			this.properties.load(fileInStream);
			openOk = true;
		} catch (FileNotFoundException exc) {
			LOG.error("Arquivo nao encontrado [" + this.fileName + "]", exc);
			this.properties.clear();
		} catch (Exception exc) {
			LOG.error(exc);
			this.properties.clear();
		} finally {
			if (fileInStream != null) {
				try {
					fileInStream.close();
				} catch (IOException iexc) {
					LOG.error(iexc);
				}
			}
		}
		return openOk;
	}

	public boolean saveProperties() {
		boolean saveOk = false;
		if (this.fileName.trim().length() > 0) {
			FileOutputStream fileOutStream = null;
			try {
				fileOutStream = new FileOutputStream(this.fileName);
				this.properties.store(fileOutStream, "");
				saveOk = true;
			} catch (Exception exc) {
				LOG.error(exc);
			} finally {
				if (fileOutStream != null) {
					try {
						fileOutStream.close();
					} catch (IOException iexc) {
						LOG.error(iexc);
					}
				}
			}
		} else {
			LOG.error("Nome de arquivo nao definido");
		}
		return saveOk;
	}

	public String getProperty(String key) {
		return getProperty(key, "");
	}

	public String getProperty(String key, String defaultValue) {
		return getProperty(key, defaultValue, false);
	}

	public String getProperty(String key, String defaultValue, boolean saveKey) {
		String result = this.properties.getProperty(key);

		if (result == null) {
			result = defaultValue;
			if (saveKey && this.fileName.trim().length() > 0) {
				this.setProperty(key, defaultValue, saveKey);
			}

		} else {
			result = result.trim();
		}

		return result;
	}

	public int getInt(String key) {
		return getInt(key, 0);
	}

	public int getInt(String key, int defaultValue) {
		return getInt(key, defaultValue, false);
	}

	public int getInt(String key, int defaultValue, boolean saveKey) {

		int result;
		String resultString = this.properties.getProperty(key);

		if (resultString == null) {
			result = defaultValue;
			if (saveKey && !this.fileName.trim().isEmpty()) {
				this.setProperty(key, defaultValue, saveKey);
			}
		} else {
			try {
				result = Integer.parseInt(resultString);
			} catch (Exception e) {
				result = defaultValue;
			}
		}

		return result;
	}

	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return getBoolean(key, defaultValue, false);
	}

	public boolean getBoolean(String key, boolean defaultValue, boolean saveKey) {
		boolean result;
		String resultString = this.properties.getProperty(key);
		if (resultString == null) {
			result = defaultValue;
			if (saveKey && !this.fileName.trim().isEmpty()) {
				this.setProperty(key, Boolean.toString(defaultValue), saveKey);
			}
		} else {
			try {
				result = Boolean.parseBoolean(resultString);
			} catch (Exception e) {
				result = defaultValue;
			}
		}
		return result;
	}

	public void setProperty(String key, String value) {
		setProperty(key, value, false);
	}

	public void setProperty(String key, int value) {
		setProperty(key, Integer.toString(value), false);
	}

	public void setProperty(String key, int value, boolean saveKey) {
		setProperty(key, Integer.toString(value), saveKey);
	}

	public void setProperty(String key, String value, boolean saveKey) {
		this.properties.setProperty(key, value.trim());
		if (saveKey && !this.fileName.trim().isEmpty()) {
			this.saveProperties();
		}
	}

	public void removeProperty(String key) {
		removeProperty(key, false);
	}

	public void removeProperty(String key, boolean saveProperties) {
		String result = this.properties.getProperty(key);
		if (result != null) {
			this.properties.remove(key);
			if (saveProperties && !this.fileName.trim().isEmpty()) {
				this.saveProperties();
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("");
		Enumeration<?> e = this.properties.propertyNames();

		result.append(getClass().toString());
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String value = getProperty(key, null);

			if (value != null) {
				result.append(" ");
				result.append(key);
				result.append(" [");
				result.append(value);
				result.append("]");
			}
		}
		return result.toString();
	}
}
