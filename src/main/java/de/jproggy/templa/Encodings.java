package de.jproggy.templa;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import de.jproggy.templa.impl.IncompatibleEncodingException;
import de.jproggy.templa.impl.TemplaException;
import de.jproggy.templa.spi.Encoding;

public enum Encodings implements Encoding {

	xml {
		@Override
		public void encode(StringBuilder target, String val) {
			for (int i = 0; i < val.length(); i++) {
				char c = val.charAt(i);
				if (c == '<') {
					target.append("&lt;");
				} else if (c == '&') {
					target.append("&amp;");
				} else {
					target.append(c);
				}
			}
		}
		@Override
		public void transcode(StringBuilder target, String value, String encodingName) {
			if (html.name().equals(encodingName)) {
				target.append(value);
			} else {
				encode(target, value);
			}
		}
	},
	html {
		@Override
		public void encode(StringBuilder target, String val) {
			for (int i = 0; i < val.length(); i++) {
				char c = val.charAt(i);
				if (c == '<') {
					target.append("&lt;");
				} else if (c == '&') {
					target.append("&amp;");
				} else if (c == 10) {
					target.append("<br />");
					if (i + 1 < val.length() && val.charAt(i + 1) == 13)
						i++;
				} else if (c == 13) {
					target.append("<br />");
					if (i + 1 < val.length() && val.charAt(i + 1) == 10)
						i++;
				} else {
					target.append(c);
				}
			}
		}
		@Override
		public void transcode(StringBuilder target, String value, String encodingName) {
			if (xml.name().equals(encodingName)) {
				target.append(value);
			} else {
				encode(target, value);
			}
		}
	},
	url {
		@Override
		public void encode(StringBuilder target, String val) {
			try {
				target.append(URLEncoder.encode(val, "utf-8"));
			} catch (UnsupportedEncodingException e) {
				throw new TemplaException(e);
			}
		}
	},
	string {
		@Override
		public void encode(StringBuilder target, String val) {
			for (int i = 0; i < val.length(); i++) {
				char ch = val.charAt(i);
				if (ch < 32) {
					switch (ch) {
					case '\b':
						target.append('\\');
						target.append('b');
						break;
					case '\n':
						target.append('\\');
						target.append('n');
						break;
					case '\t':
						target.append('\\');
						target.append('t');
						break;
					case '\f':
						target.append('\\');
						target.append('f');
						break;
					case '\r':
						target.append('\\');
						target.append('r');
						break;
					default:
						// ignore no practical meaning
						break;
					}
				} else {
					switch (ch) {
					case '\'':
					case '"':
					case '\\':
						target.append('\\');
					}
					target.append(ch);
				}
			}
		}
		@Override
		public void transcode(StringBuilder target, String value, String encodingName) {
			if (html_string.name().equals(encodingName)) {
				target.append(value);
			} else {
				encode(target, value);
			}
		}
	},
	html_string {
		@Override
		public void encode(StringBuilder target, String val) {
			try {
				target.append(URLEncoder.encode(val, "utf-8"));
			} catch (UnsupportedEncodingException e) {
				throw new TemplaException(e);
			}
		}
		@Override
		public void transcode(StringBuilder target, String value, String encodingName) {
			if (xml.name().equals(encodingName) || html.name().endsWith(encodingName)) {
				string.encode(target, value);
			}
			else if (string.name().equals(encodingName)) {
				html.encode(target, value);
			} 
			else {
				super.transcode(target, value, encodingName);
			}
			
		}
	},
	plain {
		@Override
		public void encode(StringBuilder target, String val) {
			target.append(val);
		}
	},
	NULL {
		@Override
		public void encode(StringBuilder target, String val) {
			target.append(val);
		}
		@Override
		public void transcode(StringBuilder target, String value, String encodingName) {
			encode(target, value);
		}
	}
	;

	@Override
	public void transcode(StringBuilder target, String value, String encodingName) {
		if (NULL.equals(encodingName)) {
			encode(target, value);
		}
		throw new IncompatibleEncodingException("can't convert encoding "
				+ encodingName + " into " + name());
	}
	@Override
	public String getName() {
		return name();
	}
}
