package dev.gokhun.posa;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class PropertiesAdapter extends Properties {

	private static final long serialVersionUID = 1L;

	public PropertiesAdapter(Properties properties) {
		this.putAll(properties);
	}

	@Override
	public Set<Map.Entry<Object, Object>> entrySet() {
		Set<Map.Entry<Object, Object>> sortedSet = new TreeSet<>(
				(o1, o2) -> o1.getKey().toString().compareTo(o2.getKey().toString()));
		sortedSet.addAll(super.entrySet());
		return sortedSet;
	}

	@Override
	public Set<Object> keySet() {
		return new TreeSet<>(super.keySet());
	}

	@Override
	public synchronized Enumeration<Object> keys() {
		return Collections.enumeration(new TreeSet<Object>(super.keySet()));
	}

	@Override
	public void store(OutputStream out, String comments) throws IOException {
		super.store(new StripFirstLineStream(out), comments);
	}

	private static class StripFirstLineStream extends FilterOutputStream {

		private boolean firstlineseen = false;

		public StripFirstLineStream(final OutputStream out) {
			super(out);
		}

		@Override
		public void write(final int b) throws IOException {
			if (firstlineseen) {
				super.write(b);
			} else if (b == '\n') {
				firstlineseen = true;
			}
		}
	}
}
